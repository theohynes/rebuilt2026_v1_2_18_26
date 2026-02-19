
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;


/** Wait *******************************************************
 * Waits for a specified number of seconds. Useful for autonomous command groups! */
public class pause extends Command {

	double duration;
	Timer timer = new Timer();

	public pause(double time) {
		duration = time;
	}

	public void initialize() {
		timer.reset();
		timer.start();
	}
	
	public boolean isFinished() {
		return timer.get() >= duration;
	}

	public void end(boolean interrupted) {
		timer.reset();
	}
}