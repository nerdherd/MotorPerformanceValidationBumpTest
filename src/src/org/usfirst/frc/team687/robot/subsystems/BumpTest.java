package org.usfirst.frc.team687.robot.subsystems;

import org.usfirst.frc.team687.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Questions for next time!
 * How to get t_1 from y(t_1) w/o viewing graph?
 * How to display motor output in rad/v/s?
 * Need to test values for ideal output.
 */
public class BumpTest extends Subsystem {

	CANTalon motor;
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
    		ideal_motor_output = (1 - (Math.pow(Math.E, -(Timer.getFPGATimestamp() - relative_running_clock)/RobotMap.tau))) * Yss + Yinit;
    	} else if(Timer.getFPGATimestamp() - timer_int> time_intervals){
    		if (negative_counter){
    			relative_running_clock = Timer.getFPGATimestamp();
    			negative_counter = false;
    		}
    		//generate motor output
    		motor.set(RobotMap.offset);
    		
    		//generate exponential decay function to minimum value
    		ideal_motor_output = (Math.pow(Math.E, -(Timer.getFPGATimestamp() - relative_running_clock)/RobotMap.tau)) * Yss + Yinit;
    		if (Timer.getFPGATimestamp() - timer_int>= time_intervals * 2){
    			timer_int= Timer.getFPGATimestamp();
    			negative_counter = true;
    			positive_counter = true;
    		}
    	}
    	SmartDashboard.putNumber("Ideal Motor Speed rad-s", ideal_motor_output);
    	SmartDashboard.putNumber("Motor Output rad-s", RPMtoRadPerSec(motor.getSpeed() * 600 / RobotMap.encoderticks));
    }
    	
    	//To calculate Yinit you need to find K, but to find K, you need Y init???????
    
    public void reset(){
    	positive_counter = true;
    	positive_counter = true;
    	timer_int = Timer.getFPGATimestamp();
    	relative_running_clock = Timer.getFPGATimestamp();
    	Yinit = RobotMap.offset * RobotMap.K; //converts to what?
    	Yss = RobotMap.amplitude * RobotMap.K;//converts to what?
    	time_intervals = 1/RobotMap.frequency;
    	//Yss is the rad/s of amplitude (V) and Yinit is the rad/s of Offset (V)
    	//QUESTION: How to convert Voltage to rad/s
    	
    	//calculation of tuning values
    	y_of_t1 = (1-Math.pow((double)Math.E, (double)-1)) * Yss + Yinit;
    	SmartDashboard.putNumber("y_of_t1", y_of_t1);
    	
    	calculated_K = (Yss - Yinit)/(RobotMap.amplitude - RobotMap.offset);
    	SmartDashboard.putNumber("Calculated K", calculated_K);
    	//don't forget to manually calculate for calculated_tau!
    	//calculated_tau = t_1 - t_0;
    }
    
    public void MotorOff() {
    	motor.set(0);
    }
    
    private double RPMtoRadPerSec(double value){
    	return value * Math.PI / 60;
    }
    
    public void updateDashboard(){
    	//Model Parameters
    	RobotMap.K = SmartDashboard.getNumber("K", RobotMap.K);
    	SmartDashboard.putNumber("K", RobotMap.K);
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