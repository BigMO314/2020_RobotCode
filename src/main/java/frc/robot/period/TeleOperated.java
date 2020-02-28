package frc.robot.period;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.molib.humancontrols.XboxController;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Indexer;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class TeleOperated {
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Indexer sysIndexer = Indexer.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    private Joystick ctlDriver_L = new Joystick(0);
    private Joystick ctlDriver_R = new Joystick(1);

    private Timer tmrOuttake = new Timer();

    private XboxController ctlOperator = new XboxController(2);


    private static final TeleOperated INSTANCE = new TeleOperated();
    public static TeleOperated getInstance() { return INSTANCE; }
    

    public void update(){

        // - DRIVER CONTROLS - (Chassis, Indexing System, Shooter)

        //Chassis
        sysChassis.setDrive(-ctlDriver_L.getY(Hand.kLeft) , -ctlDriver_R.getY(Hand.kRight));

        // Shooter
        if (ctlDriver_R.getTrigger()){  
            sysShooter.enable();
        } else sysShooter.disable();

        // Indexing system
        if (ctlDriver_R.getRawButton(2)){
            sysIndexer.enable();
        } else sysIndexer.disable();
        

        //  - OPERATOR CONTROLS - (Intake)

        // Intake 
        if (ctlOperator.getXButton()){
            sysIntake.armExtend();
            sysIntake.enableRoller();
        } else if (ctlOperator.getYButtonPressed()){
            tmrOuttake.reset();
            sysIntake.armExtend();

        // Outtake
        } else if (ctlOperator.getYButton()){
            if (tmrOuttake.get() > 0.25){
                sysIntake.reverseRoller();
            } 
        // Disable Intake if neither buttons are pressed
        } else { 
            sysIntake.armRetract();
            sysIntake.disableRoller();
        }


        

}

}