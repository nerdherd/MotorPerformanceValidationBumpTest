package org.usfirst.frc.team687.robot.subsystems;

import org.usfirst.frc.team687.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 */
public class BumpTest extends Subsystem {

	CANTalon motor;
	double K;
	double timer_int;
	double relative_running_clock;
	double y_of_t1;
	double kU;
	double Yss;
	double Yinit;
	double calculated_K;
	double calculated_tau;
	double ideal_motor_output;
	double time_intervals;
	boolean positive_counter;
	boolean negative_counter;
	
	public BumpTest(){
		motor = new CANTalon(RobotMap.CANTalonPort1);
		motor.changeControlMode(TalonControlMode.Voltage);
		motor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		motor.reverseOutput(true);
		motor.reverseSensor(true);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void BumpTestRun(){
    	SmartDashboard.putNumber("Current (A)", motor.getOutputCurrent());
    	SmartDashboard.putNumber("Voltage (V)", motor.getOutputVoltage());
    	if (Timer.getFPGATimestamp() - timer_int<= time_intervals){
    		if (positive_counter){
    			relative_running_clock = Timer.getFPGATimestamp();
    			positive_counter = false;
    		}
    		//generate motor output
    		motor.set(RobotMap.offset + RobotMap.amplitude);
    		
    		//generate exponential decay function to maximum value
    		ideal_motor_output = ((1 - (Math.pow(Math.E, -(Timer.getFPGATimestamp() - relative_running_clock)/RobotMap.tau))) * Yss + Yinit) / 3;
    		
    	} else if(Timer.getFPGATimestamp() - timer_int> time_intervals){
    		if (negative_counter){
    			relative_running_clock = Timer.getFPGATimestamp();
    			negative_counter = false;
    		}
    		//generate motor output
    		motor.set(RobotMap.offset - RobotMap.amplitude);
    		
    		//generate exponential decay function to minimum value
    		ideal_motor_output = ((Math.pow(Math.E, -(Timer.getFPGATimestamp() - relative_running_clock)/RobotMap.tau)) * Yss + Yinit - (RobotMap.amplitude*K)) / 3;
    		if (Timer.getFPGATimestamp() - timer_int>= time_intervals * 2){
    			timer_int= Timer.getFPGATimestamp();
    			negative_counter = true;
    			positive_counter = true;
    		}
}
    	SmartDashboard.putNumber("Ideal Motor Speed rad-s", ideal_motor_output);
    	SmartDashboard.putNumber("Motor Output rad-s", RPMtoRadPerSec(motor.getSpeed()) * 3);
    }
    
    public void reset(){
		positive_counter = true;
		negative_counter = true;
	   	timer_int = Timer.getFPGATimestamp();
	   	relative_running_clock = Timer.getFPGATimestamp();
	   	
	   	K = (-7.62367 * Math.pow(RobotMap.amplitude, 2)) + (90.75566 * RobotMap.amplitude) + 144.58255;
	   	
	   	Yinit = (RobotMap.offset) * K;
    	Yss = (RobotMap.amplitude + RobotMap.offset) * K;
    	time_intervals = 1/RobotMap.frequency;
    	
    	//calculation of tuning values
    	y_of_t1 = (1-Math.pow((double)Math.E, (double)-1)) * Yss + Yinit;
    	SmartDashboard.putNumber("y_of_t1", y_of_t1);
    	
    	//uncomment for recalibrating K/ retuning
//    	calculated_K = (Yss - Yinit)/(RobotMap.amplitude);
//    	calculated_K = (-7.62367 * Math.pow(RobotMap.amplitude, 2)) + (90.75566 * RobotMap.amplitude) + 144.58255;
//    	SmartDashboard.putNumber("Calculated K", calculated_K);
    	
    	//don't forget to manually calculate for calculated_tau!
    	//calculated_tau = t_1 - t_0;
    }
    
    public void MotorOff() {
    	motor.set(0);
    }
    
    private double RPMtoRadPerSec(double value){
    	return value * 2 * Math.PI / 60;
    }
    
    public void updateDashboard(){
    	//Model Parameters
//    	K = SmartDashboard.getNumber("K", K); //for calibrating K
    	SmartDashboard.putNumber("K", K);
    	RobotMap.tau = SmartDashboard.getNumber("tau", RobotMap.tau);
    	SmartDashboard.putNumber("tau", RobotMap.tau);
    	
    	//Signal Generator
    	RobotMap.amplitude = SmartDashboard.getNumber("Amplitude (V)", RobotMap.amplitude);
    	SmartDashboard.putNumber("Amplitude (V)", RobotMap.amplitude);
    	RobotMap.frequency = SmartDashboard.getNumber("Frequency (Hz)", RobotMap.frequency);
    	SmartDashboard.putNumber("Frequency (Hz)", RobotMap.frequency);
    	RobotMap.offset = SmartDashboard.getNumber("Offset (V)", RobotMap.offset);
    	SmartDashboard.putNumber("Offset (V)", RobotMap.offset);
        	
    }   
}