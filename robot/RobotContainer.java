// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;

import frc.robot.subsystems.FuelSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.armSubsystem;

import frc.robot.commands.Eject;
import frc.robot.commands.extendArmToBar;
import frc.robot.commands.Intake;
import frc.robot.commands.Launch;
import frc.robot.commands.LaunchSequence;
import frc.robot.commands.POVArmMotorCommand;
import frc.robot.commands.pause;
import frc.robot.commands.SpinUp;

import frc.robot.autos.ExampleAuto;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Commands;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.Trajectory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;

import java.util.List;



/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
@SuppressWarnings("unused")
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final FuelSubsystem fuelSubsystem = new FuelSubsystem();
  public static final armSubsystem m_armSubsystem = new armSubsystem(); //arm subsystem


  // The driver's controller
   public static CommandXboxController m_driverController =
      new CommandXboxController(OIConstants.kDriverControllerPort);

        // Create a POV trigger for your controller
    private Trigger povTrigger;

    // create instance of POVMotorCommand as a class-level variable
    private POVArmMotorCommand povMotorCommand;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    // Configure default commands
    m_robotDrive.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () ->
                m_robotDrive.drive(
                    -MathUtil.applyDeadband(
                        m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                    -MathUtil.applyDeadband(
                        m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                    -MathUtil.applyDeadband(
                        m_driverController.getRightX(), OIConstants.kDriveDeadband),
                    true),
            m_robotDrive));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {


 // Left Stick Button -> Set swerve to X
    m_driverController.leftStick().onTrue(m_robotDrive.setXCommand());

// WILL BE FOR THE ARM @TODO 
  //Left Trigger -> arm moves up
  m_driverController.leftTrigger(OIConstants.kTriggerButtonThreshold)
  .whileTrue(new extendArmToBar(m_armSubsystem, 5, 0.5, false));


    // While the left bumper on operator controller is held, intake Fuel
  m_driverController.leftBumper().whileTrue(new Intake(fuelSubsystem));

    // While the right bumper on the operator controller is held, spin up for 1
      m_driverController.rightBumper().onTrue(new LaunchSequence(fuelSubsystem));

 // While the A button is held on the operator controller, eject fuel back out
    // the intake
     m_driverController.a().whileTrue(new Eject(fuelSubsystem));

    // Right Trigger -> Run coral intake, set to leave out when idle
    m_driverController
        .rightTrigger()
        .onTrue(new LaunchSequence(fuelSubsystem));;

    // Start Button -> Zero swerve heading
   // m_driverController.start().onTrue(m_robotDrive.zeroHeadingCommand());
   // Create a POV trigger for your controller
    //povTrigger = new Trigger(() -> m_driverController.getPOV() != -1);
    //new POVMotorCommand(m_armSubsystem, m_driverController, povTrigger);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(
        AutoConstants.kMaxSpeedMetersPerSecond,
        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(DriveConstants.kDriveKinematics);

    // An example trajectory to follow. All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(3, 0, new Rotation2d(0)),
        config);

    var thetaController = new ProfiledPIDController(
        AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
        exampleTrajectory,
        m_robotDrive::getPose, // Functional interface to feed supplier
        DriveConstants.kDriveKinematics,

        // Position controllers
        new PIDController(AutoConstants.kPXController, 0, 0),
        new PIDController(AutoConstants.kPYController, 0, 0),
        thetaController,
        m_robotDrive::setModuleStates,
        m_robotDrive);

    // Reset odometry to the starting pose of the trajectory.
    m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false));
  }



}