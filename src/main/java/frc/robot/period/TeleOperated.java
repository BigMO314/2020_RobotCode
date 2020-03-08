package frc.robot.period;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.molib.humancontrols.XboxController;
import frc.molib.humancontrols.buttons.Button;
import frc.molib.humancontrols.buttons.ButtonScheduler;
import frc.molib.vision.Limelight;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Climber;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

@SuppressWarnings("unused")
public class TeleOperated {
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();
    private Climber sysClimber = Climber.getInstance();

    private XboxController ctlDriver = new XboxController(0);
    private XboxController ctlOperator = new XboxController(1);

    private Timer tmrIntake = new Timer();
    private Timer tmrShooter = new Timer();

    private Button btnAlign = new Button("TeleOp" , "Align") { @Override public boolean get() { return ctlDriver.getAButton(); } };
    private Button btnHopper = new Button("TeleOp" , "Hopper") { @Override public boolean get() { return ctlOperator.getTriggerButton(Hand.kRight); } };
    private Button btnFlywheelNear = new Button("TeleOp" , "Flywheel Near") { @Override public boolean get() { return ctlOperator.getTriggerButton(Hand.kLeft); } };
    private Button btnFlywheelFar = new Button("TeleOp" , "Flywheel Far") { @Override public boolean get() { return ctlOperator.getBumper(Hand.kLeft); } };
    private Button btnIntake = new Button("TeleOp" , "Intake") { @Override public boolean get() { return ctlOperator.getAButton() || ctlDriver.getBumper(Hand.kLeft) || ctlDriver.getTriggerButton(Hand.kLeft); } };
    private Button btnRetractArm = new Button("TeleOp", "Retract Arm") { @Override public boolean get() { return ctlOperator.getYButton(); } };
    private Button btnOuttake = new Button("TeleOp" , "Outtake") { @Override public boolean get() { return ctlDriver.getBButton(); } };
    private Button btnClimber = new Button("TeleOp" , "Climber") { @Override public boolean get() { return ctlOperator.getStartButton() && ctlOperator.getBackButton(); } };
    
    private Button btnPrecision = new Button("TeleOp" , "Precision") { @Override public boolean get() { return ctlDriver.getTriggerButton(Hand.kRight); } };


    private static final TeleOperated INSTANCE = new TeleOperated();
    public static TeleOperated getInstance() { return INSTANCE; }

    private TeleOperated() {
        tmrIntake.start();
        tmrShooter.start();
    }

    public void init(){
       Robot.initSystems();
       Robot.disableSystems();
    }
    

    public void update(){

        ButtonScheduler.getInstance().update();

        // - DRIVER CONTROLS - (Chassis, Indexing System, Shooter)

        //Chassis
        if(btnAlign.get()) {
            sysChassis.enableVisionPID();
            Limelight.setLEDMode(Limelight.LEDMode.kOn);
        } else {
            sysChassis.disableVisionPID();
            Limelight.setLEDMode(Limelight.LEDMode.kOff);

            if (btnPrecision.get()) 
                sysChassis.setArcade(-ctlDriver.getY(Hand.kLeft) * 0.375, ctlDriver.getX(Hand.kRight) * 0.125);
            else 
                sysChassis.setArcade(-ctlDriver.getY(Hand.kLeft) * 0.875, ctlDriver.getX(Hand.kRight) * 0.375);
            
        }
        

        //  - OPERATOR CONTROLS - (Intake)

        // Intake
        if (btnIntake.getPressed()){
            tmrIntake.reset();
        } else if (btnIntake.get()){
            if(tmrIntake.get() > 0.25) 
                sysIntake.enableRoller();
            sysIntake.armExtend();
        } else if (btnRetractArm.get()) {
            sysIntake.armRetract();
        } else if (btnOuttake.get()){
            sysIntake.reverseRoller();
        } else { 
            //sysIntake.armRetract();
            sysIntake.disableRoller();
        }

        // Indexing system

        if (btnHopper.get()){
            sysShooter.enableHopper();
            //sysIntake.enableRoller();
        } else {
            sysShooter.disableHopper();
        }

        // Shooter
        if (btnFlywheelNear.get()){  
            sysShooter.enableFlywheelNear();
            //sysIntake.armExtend();
        } else if (btnFlywheelFar.get()){
           sysShooter.enableFlywheelFar();
            //sysIntake.armExtend();
        } else sysShooter.disableFlywheel();
            

        // Automated w/ delay (Currently not in use)

        /*  if (btnHopper.getPressed()){
            tmrShooter.reset();

        } else if (btnHopper.get()){
            if(tmrShooter.get() > 1.0)
                sysShooter.enableHopper();
            sysIntake.armExtend();
        } else  
            sysShooter.disableHopper();
        */

        // Climber
        if (btnClimber.get())
            sysClimber.enableWinch();
        else {
            sysClimber.disableWinch();
        }
        sysChassis.update();
        sysIntake.update(); 
        sysShooter.update();
        sysClimber.update();

}

}