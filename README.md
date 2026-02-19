# rebuilt2026_v1_2_18_26
# rebuilt2026_v1_2_18_26
Welcome to the first version of the code for Rebuilt

Things I did so far (and it's a lot :")  
-> set up the base code from the Swerve drive code  
--> Translate any and all FRC Kitbot Code to be REV friendly
    --adjust constants to match:  
          ~length/width of robot    
          ~RPM of vortex motors  
          ~assigned numbers of Sparkmaxes (10s for swerves/20s for intake and launcher/30s for arm)  
    --set up controller in robotContainer  
        ~left is going to control arm  
        ~right controls launcher  
    --update imports in all classes to match most recent non-depricated libraries  
    --updated the gyro so it is recognized as a pigeon rather than the ADIS IMU  
    --added the FuelSubsystem and armSubsystems and all necessary methods  
    --added commands that run independently of the subsystems (You pick and choose which ones to call on based on the buttons)  
        ~I need to finish POVArmMotorCommand which is a more accurate version of extendArmToBar and seems like it will be used with a camera if we add that  
    --pause created to give motors a min to breathe when switching directions  
    --extendArmtoBar is actually mostly my own code and cycles the robot's arm 12 seconds for now to extend the arm to "full height" and then pause 2 seconds and then reverse the motor so it pulls itself up. Obviously we can adjust the speeds as necessary'  
    --Created 2 examples of autos not including the classic S one that Rev loves sneaking in
        ~1st is just a drive away auto
        ~2nd is drive and shoot for 9 seconds 
