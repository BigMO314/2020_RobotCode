/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.molib.DashTable;
import frc.molib.humancontrols.buttons.ButtonScheduler;
import frc.robot.period.TeleOperated;
import frc.robot.period.Test;
import frc.robot.period.Autonomous;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

@SuppressWarnings("unused")


public class Robot extends TimedRobot {


    private TeleOperated prdTeleOperated = TeleOperated.getInstance();
    private Autonomous prdAutonomous = Autonomous.getInstance();
    private Test prdTest = Test.getInstance();
  
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake = Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    private static final Robot INSTANCE = new Robot();
    public static Robot getInstance() { return INSTANCE; }

    public static DashTable tblTroubleshooting = new DashTable("Troubleshooting");

    public static void disableSystems(){

        INSTANCE.sysChassis.disable();
        INSTANCE.sysIntake.disableRoller();
        INSTANCE.sysShooter.disable();

    }




    @Override
    public void robotInit() {
    }


    @Override
    public void robotPeriodic() {
    
        ButtonScheduler.getInstance().update();

    }


    @Override
    public void autonomousInit() {
        prdAutonomous.init();
    }


    @Override
    public void autonomousPeriodic() {

        prdAutonomous.update();
    }

    @Override
    public void teleopInit() {
        prdTeleOperated.init();
    }
    @Override
    public void teleopPeriodic() {
        prdTeleOperated.update();

    }

    @Override
    public void testInit() {
        prdTest.init();
    }


    @Override
    public void testPeriodic() {
        prdTest.update();
    }

} 
