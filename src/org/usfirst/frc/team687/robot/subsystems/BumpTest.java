package org.usfirst.frc.team687.robot.subsystems;

import org.usfirst.frc.team687.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BumpTest extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	CANTalon motor;
	double y_of_t1;
	
	double kU;
	double Yss;
	double Yinit;
	double calculated_K;
	double ideal_motor_output;
	
	public BumpTest(){
		motor = new CANTalon(1);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void BumpTestRun(){
    	motor.changeControlMode(TalonControlMode.PercentVbus);
    	motor.set(RobotMap.offset + RobotMap.amplitude);
    	ideal_motor_output = RobotMap.K/(RobotMap.tau * time_seconds + 1); //WHAT UNITS ARE THESE IN?
    }
    
    public void Calibration(){
    	Yinit = RobotMap.offset; //THERE IS MORE! convert offset (V) to rad/s
    	Yss = RobotMap.amplitude; //THERE IS MORE! convert amplitude (V) to rad/s
    	//Yss is the rad/s of amplitude (V) and Yinit is the rad/s of Offset (V)
    	//QUESTION: How to convert Voltage to rad/s
    	
    	//calculation of tuning values
    	y_of_t1 = (1-Math.pow((double)Math.E, (double)-1)) * Yss + Yinit; //y(t_1) - yinit should be 63.2% of yss - yinit?
    	SmartDashboard.putNumber("y_of_t1", y_of_t1);
    	
    	calculated_K = (Yss - Yinit)/(RobotMap.amplitude - RobotMap.offset);
    	SmartDashboard.putNumber("Calculated K", calculated_K);
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
    	
    	//Safety Check
    	SmartDashboard.putNumber("Current (A)", motor.getOutputCurrent());
    	
    	//Graphs
    	SmartDashboard.putNumber("Voltage (V)", motor.getOutputVoltage());
    	SmartDashboard.putNumber("Actual RPM", getRPM(motor)); //rad/s!
    	SmartDashboard.putNumber("Speed (rad/s)", ideal_motor_output); // try to get it into rad/s
//    	SmartDashboard.putNumber("RPM Error", getError());
    }   
}