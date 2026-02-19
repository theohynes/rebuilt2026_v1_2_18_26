package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@SuppressWarnings("unused")
public class armSubsystem extends SubsystemBase {

    private final SparkMax armMotor; // Replace with the actual motor controller you are using

    // Create a chooser for selecting the desired drive speed scale
    SendableChooser<Double> armScaleChooser = new SendableChooser<Double>();
    public double CURRENT_ARM_SCALE;

    @SuppressWarnings("deprecation")
    public armSubsystem() {
        // Initialize the arm motor controller (replace with your CAN ID and motor type)
        armMotor = new SparkMax(Constants.ARM_MOTOR_CAN_ID, MotorType.kBrushless);

        // Set motor controller and sensor configurations (e.g., inverted, encoder settings)
        armMotor.setInverted(Constants.ARM_MOTOR_INVERTED); // Adjust as needed

        // Configure any additional settings, such as encoder settings, current limits, etc.
        // armMotor.setParameter(...); // Adjust as needed
        
        // Arm Speed Scale Options
        SmartDashboard.putString("Arm Speed ", "Select Scale");
        armScaleChooser.addOption("100%", 1.0);
        armScaleChooser.setDefaultOption("75%", 0.75);
        armScaleChooser.addOption("50%", 0.5);
        armScaleChooser.addOption("25%", 0.25);

        SmartDashboard.putData("Arm Speed", armScaleChooser);
    }

    public void extendArm(double power) {
        // Extend the arm using the specified power (positive value)
        armMotor.set(power);
    }

    public void stopArm() {
        // Stop extending the arm
        armMotor.set(0.0);
    }

    @Override
    public void periodic() {
        // Implement any periodic tasks or monitoring here, if needed
        CURRENT_ARM_SCALE = armScaleChooser.getSelected(); // Continously update the desired drive scale
    }
}