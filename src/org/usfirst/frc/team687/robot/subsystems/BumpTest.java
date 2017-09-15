package org.usfirst.frc.team687.robot.subsystems;

import org.usfirst.frc.team687.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BumpTest extends Subsystem {

	CANTalon motor;
	Timer timer;
	Timer running_clock;
	double y_of_t1;
	
	double kU;
	double Yss;
	double Yinit;
	double calculated_K;
	double ideal_motor_output;
	double time_intervals;
	
	public BumpTest(){
		motor = new CANTalon(1);
    	motor.changeControlMode(TalonControlMode.Voltage);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void BumpTestRun(){
    	SmartDashboard.putNumber("Current (A)", motor.getOutputCurrent());
    	if (timer.get() <= time_intervals){ //timer.get() double?
    		motor.set(RobotMap.offset + RobotMap.amplitude);
    		ideal_motor_output = (Math.pow((double)Math.E, (double)(-running_clock.get()/RobotMap.tau))) * Yss + Yinit;
        	SmartDashboard.putNumber("Speed (rad/s)", ideal_motor_output);
    	} else if(timer.get() > time_intervals){
    		motor.set(RobotMap.offset);
    		ideal_motor_output = (1-(Math.pow((double)Math.E, (double)(-running_clock.get()/RobotMap.tau)))) * Yss + Yinit;
        	SmartDashboard.putNumber("Speed (rad/s)", ideal_motor_output);
    		if (timer.get() >= time_intervals * 2){
    			timer.reset();
    		}
    	}
    }
    	
    	//To calculate Yinit you need to find K, but to find K, you need Y init???????
    
    public void reset(){
    	timer.start();
    	running_clock.start();
    	Yinit = RobotMap.offset * RobotMap.K; //THERE IS MORE! convert offset (V) to rad/s
    	Yss = RobotMap.amplitude * RobotMap.K; //THERE IS MORE! convert amplitude (V) to rad/s
    	time_intervals = 1/RobotMap.frequency;
    	//Yss is the rad/s of amplitude (V) and Yinit is the rad/s of Offset (V)
    	//QUESTION: How to convert Voltage to rad/s
    	
    	//calculation of tuning values
//    	y_of_t1 = (1-Math.pow((double)Math.E, (double)-1)) * Yss + Yinit; //y(t_1) - yinit should be 63.2% of yss - yinit?
//    	SmartDashboard.putNumber("y_of_t1", y_of_t1);
    	
//    	calculated_K = (Yss - Yinit)/(RobotMap.amplitude - RobotMap.offset);
//    	SmartDashboard.putNumber("Calculated K", calculated_K);
    	//don't forget to manually calculate for calculated_tau!
//    	calculated_tau = t_1 - t_0;
    }

//    private double getRPM(CANTalon m_motor){
//    	m_motor.
//    	m_motor.changeControlMode(TalonControlMode.Speed);
//    	return m_motor.getSpeed();
//    }
    
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
        	
    	//Graphs
    	SmartDashboard.putNumber("Voltage (V)", motor.getOutputVoltage());
    	SmartDashboard.putNumber("Actual RPM", ideal_motor_output); //rad/s!
//    	SmartDashboard.putNumber("RPM Error", getError());
    }   
}