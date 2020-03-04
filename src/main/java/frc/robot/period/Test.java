package frc.robot.period;

import frc.molib.humancontrols.buttons.Button;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class Test {
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    private Button btnRoller = new Button("Test" ,"Roller");
    private Button btnArm = new Button("Test" ,"Intake Arm");
    private Button btnHopper = new Button("Test" ,"Hopper");
    private Button btnFlywheel = new Button("Test" , "Flywheel");

    private Button btnDistance = new Button("Test" , "Drive Distance");
    private Button btnAngle = new Button("Test" , "Drive Angle");

    private boolean lastPressed = false;



    private static final Test INSTANCE = new Test();
    public static Test getInstance() { return INSTANCE; }


    public void init(){

        sysIntake.armExtend();
        
    }
    

    public void update(){

        if (btnRoller.get()){
           sysIntake.enableRoller(); 
        } else sysIntake.disableRoller();

        if (btnArm.get()){
            sysIntake.armExtend();
        } else sysIntake.armRetract();

        if (btnHopper.get()){
            sysShooter.enableHoopper();
        } else sysShooter.disableHopper();

        if (btnFlywheel.get()){
            sysShooter.enableFlywheelPID();
        } else {
            sysShooter.disableFlywheelPID();
            sysShooter.setFlywheel(0.0);
        }


        if (btnDistance.get() && !lastPressed){ sysChassis.goToDistance(22.0, true); }
        if (!btnDistance.get()) sysChassis.disableDistancePID();

        lastPressed = btnDistance.get();

        sysChassis.update();
        sysIntake.update(); 
        sysShooter.update();


        

}

}
