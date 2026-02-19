package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.pause;
import frc.robot.subsystems.armSubsystem;

@SuppressWarnings("unused")
public class extendArmToBar extends Command {

    private final armSubsystem m_armSubsystem;
    private final Timer timer = new Timer();
    private final double duration; // Duration in seconds
    private final double power; // Power to extend the arm (positive value)
    private boolean isDoneYet = false;

    double pauseDuration=2.0; // Duration to pause before retracting the arm (in seconds)
	Timer pauseTimer = new Timer();

    public extendArmToBar(armSubsystem arm1, double duration, double power, boolean idy) {
        m_armSubsystem = arm1;
        this.duration = duration;
        this.power = power;
        this.isDoneYet = idy;
        addRequirements(m_armSubsystem);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    public void pauseInitialize() {
        pauseTimer.reset();
        pauseTimer.start();
    }

    @Override
    public void execute() {
        //the full cycle of extending and retracting the arm should take 12 seconds,
        // so we can use the timer to control the timing of the extension and retraction  
        double currentTime = timer.get();
        if (currentTime < duration) {
            // Extend the arm with the specified power (positive value)
            m_armSubsystem.extendArm(power);
        } else if (currentTime >= duration && !isDoneYet) {
            // Stop extending the arm when the duration is reached and set isDoneYet to true
            m_armSubsystem.stopArm();
            isDoneYet = true;
            this.pauseNow(); // Wait for 2 seconds before retracting the arm
            this.retractArm(); // Call the method to retract the arm after extending
        }
        else {
            // Stop extending the arm when the duration is reached
            m_armSubsystem.stopArm();
        }
    }
public void pauseNow() {
    this.pauseInitialize(); // Start the pause timer
}
    @Override
    public boolean isFinished() {
        // The command finishes when the timer exceeds the specified duration
        return timer.get() >= duration;
    }


    public void retractArm() {
        // Retract the arm with the specified power (negative value)
             double currentTime = timer.get();
        if (currentTime < duration) {
            // Extend the arm with the specified power (positive value)
            m_armSubsystem.extendArm(-power);
        } else if (currentTime >= duration && !isDoneYet) {
            // Stop extending the arm when the duration is reached and set isDoneYet to true
            m_armSubsystem.stopArm();
            isDoneYet = true;
        }
        else {
            // Stop extending the arm when the duration is reached
            m_armSubsystem.stopArm();
        }
    }


    @Override
    public void end(boolean interrupted) {
        // Stop the arm when the command ends
        m_armSubsystem.stopArm();
    }
}