package org.usfirst.frc.team687.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//Model Parameters
	public static double K = 216; // rad/V.s
	public static double tau = 1.2; //amount of time to get 63.2% of yss-yinit
	
	//Signal Generator
	public static double amplitude = 1;
	public static double frequency = 0.2;
	public static double offset = 1;
	
	//Ports
	public static int CANTalonPort1 = 0;
}
