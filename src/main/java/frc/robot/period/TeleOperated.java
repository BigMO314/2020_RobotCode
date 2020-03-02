package frc.robot.period;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.molib.Console;
import frc.molib.humancontrols.XboxController;
import frc.molib.humancontrols.buttons.Button;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class TeleOperated {
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    private Joystick ctlDriver_L = new Joystick(0);
    private Joystick ctlDriver_R = new Joystick(1);
    private XboxController ctlOperator = new XboxController(2);

    private Timer tmrIntake = new Timer();
    private Timer tmrShooter = new Timer();

    private Button btnIntake = new Button() { @Override public boolean get() { return ctlOperator.getXButtonPressed(); } };
    private Button btnOuttake = new Button() { @Override public boolean get() { return ctlOperator.getYButtonPressed(); } };
    private Button btnHopper = new Button() { @Override public boolean get() { return ctlDriver_R.getTrigger();} };
    private Button btnFlywheel = new Button() { @Override public boolean get() { return ctlDriver_R.getRawButton(2); } };



    private static final TeleOperated INSTANCE = new TeleOperated();
    public static TeleOperated getInstance() { return INSTANCE; }

    private TeleOperated() {
        tmrIntake.start();
        tmrShooter.start();
    }
    

    public void update(){

        // - DRIVER CONTROLS - (Chassis, Indexing System, Shooter)

        //Chassis
        sysChassis.setDrive(-ctlDriver_L.getY(Hand.kLeft) , -ctlDriver_R.getY(Hand.kRight));

        // Shooter
        if (btnFlywheel.get()){  
            sysShooter.enableFlywheel();
        } else sysShooter.disableFlywheel();
        

            //  - OPERATOR CONTROLS - (Intake)

            // Intake
            if (btnIntake.getPressed()){
                tmrIntake.reset();
            } else if (btnIntake.get()){
                if(tmrIntake.get() > 0.25)
                    sysIntake.enableRoller();
                sysIntake.armExtend();
            } else if (btnOuttake.getPressed()){
                tmrIntake.reset();
            

            // Outtake
            } else if (ctlOperator.getYButton()){
                if (tmrIntake.get() > 0.25){
                    sysIntake.reverseRoller(); 
                } 
                sysIntake.armExtend();
            // Disable Intake if neither buttons are pressed
            } else { 
                sysIntake.armRetract();
                sysIntake.disableRoller();
            }

            // Indexing system
            if (btnHopper.getPressed()){
                tmrShooter.reset();
                Console.logMsg("Timer reset");
            } else if (btnHopper.get()){
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