package org.usfirst.frc.team687.robot.commands;

import org.usfirst.frc.team687.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BumpTestCalibrationCalibration extends Command {

    public BumpTestCalibrationCalibration() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.bumpTestCalibrate);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.bumpTestCalibrate.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.bumpTestCalibrate.BumpTestRun();
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
    	Robot.bumpTestCalibrate.MotorOff();
    }
}