package frc.robot.period;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.molib.Console;
import frc.molib.DashTable.DashEntry;
import frc.molib.humancontrols.XboxController;
import frc.molib.humancontrols.buttons.Button;
import frc.molib.humancontrols.buttons.ButtonScheduler;
import frc.molib.vision.Limelight;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class TeleOperated {
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    

    private XboxController ctlDriver = new XboxController(0);

    private Timer tmrIntake = new Timer();
    private Timer tmrShooter = new Timer();

    private Button btnAlign = new Button("TeleOp" , "Align") { @Override public boolean get() { return ctlDriver.getAButton(); } };
    private Button btnIntake = new Button("TeleOp" , "Intake") { @Override public boolean get() { return ctlDriver.getBumper(Hand.kLeft); } };
    private Button btnHopper = new Button("TeleOp" , "Hopper") { @Override public boolean get() { return ctlDriver.getBumper(Hand.kRight);} };
    private Button btnFlywheel = new Button("TeleOp" , "Flywheel") { @Override public boolean get() { return ctlDriver.getTriggerButton(Hand.kRight); } };



    private static final TeleOperated INSTANCE = new TeleOperated();
    public static TeleOperated getInstance() { return INSTANCE; }

    private TeleOperated() {
        tmrIntake.start();
        tmrShooter.start();
    }

    public void init(){
        sysChassis.init();
        sysIntake.init();
        sysShooter.init();
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
            sysChassis.setArcade(-ctlDriver.getY(Hand.kLeft) * 0.75, ctlDriver.getX(Hand.kRight) * 0.375);
        }

        // Shooter
        if (ctlDriver.getTriggerButton(Hand.kRight)){  
            sysShooter.enableFlywheel();
        } else sysShooter.disableFlywheel();
        

            //  - OPERATOR CONTROLS - (Intake)

            // Intake
            if (btnIntake.getPressed()){
                tmrIntake.reset();
            } else if (btnIntake.get()){
                if(tmrIntake.get() > 0.50) 
                    sysIntake.enableRoller();
                sysIntake.armExtend();
            } else { 
                sysIntake.armRetract();
                sysIntake.disableRoller();
            }

            // Indexing system
            if (ctlDriver.getBumperPressed(Hand.kRight)){
                tmrShooter.reset();

            } else if (ctlDriver.getBumper(Hand.kRight)){
                if(tmrShooter.get() > 1.0)
                    sysShooter.enableHoopper();
                sysIntake.armExtend();
            } else  
                sysShooter.disableHopper();

        sysChassis.update();
        sysIntake.update(); 
        sysShooter.update();

}

}