package org.usfirst.frc.team687.robot.commands;

import org.usfirst.frc.team687.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MotorCalibration extends Command {

    public MotorCalibration() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.calibrator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.calibrator.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.calibrator.BumpTestRun();
    	Robot.calibrator.updateDashboard();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.tester.MotorOff();
    }
}
