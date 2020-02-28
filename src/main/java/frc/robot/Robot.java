/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.molib.DashTable;
import frc.robot.period.TeleOperated;
import frc.robot.period.Autonomous;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Indexer;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

@SuppressWarnings("unused")


public class Robot extends TimedRobot {

    private static final Robot INSTANCE = new Robot();
    public static Robot getInstance() { return INSTANCE; }

    private TeleOperated prdTeleOperated = TeleOperated.getInstance();
    private Autonomous prdAutonomous = Autonomous.getInstance();
  
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake = Intake.getInstance();
    private Indexer sysIndexer = Indexer.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    public static DashTable tblTroubleshooting = new DashTable("Troubleshooting");

    public static void disableSystems(){

        INSTANCE.sysChassis.disable();
        INSTANCE.sysIntake.disableRoller();
        INSTANCE.sysIndexer.disable();
        INSTANCE.sysShooter.disable();

    }
    

    
     
  @Override
     public void robotInit() {
    
  }

 
  @Override
     public void robotPeriodic() {
  }

 
  @Override
    public void autonomousInit() {
    
  }

  
  @Override
    public void autonomousPeriodic() {
 
  }

 
  @Override
    public void teleopPeriodic() {
        
        prdTeleOperated.update();
        
        sysChassis.update();
        sysIntake.update(); 
        sysIndexer.update();
        sysShooter.update();
  }

 
  @Override
    public void testPeriodic() {
    
  }
  
}
