package frc.robot.period;

import frc.molib.humancontrols.buttons.Button;
import frc.molib.humancontrols.buttons.ButtonScheduler;
import frc.molib.vision.Limelight;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Climber;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class Test {
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();
    private Climber sysClimber = Climber.getInstance();

    private Button btnAlign = new Button("Test", "Align");
    private Button btnRoller = new Button("Test" ,"Roller");
    private Button btnArm = new Button("Test" ,"Intake Arm");
    private Button btnHopper = new Button("Test" ,"Hopper");
    private Button btnFlywheel = new Button("Test" , "Flywheel");

    private Button btnDistance = new Button("Test" , "Drive Distance");
    private Button btnAngle = new Button("Test" , "Drive Angle");

    private static final Test INSTANCE = new Test();
    public static Test getInstance() { return INSTANCE; }


    public void init(){

        sysIntake.armExtend();
        sysChassis.disable();
        
    }
    

    public void update(){

        ButtonScheduler.getInstance().update();

        if (btnAlign.get()) {
            sysChassis.enableVisionPID();
            Limelight.setLEDMode(Limelight.LEDMode.kOn);
        } else {
            sysChassis.disableVisionPID();
            Limelight.setLEDMode(Limelight.LEDMode.kOff);
        }

        if (btnRoller.get()){
           sysIntake.enableRoller(); 
        } else sysIntake.disableRoller();

        if (btnArm.get()){
            sysIntake.armExtend();
        } else sysIntake.armRetract();

        if (btnHopper.get()){
            sysShooter.enableHopper();
        } else sysShooter.disableHopper();

        if (btnFlywheel.get()){
            sysShooter.enableFlywheelPID();
        } else {
            sysShooter.disableFlywheelPID();
            sysShooter.setFlywheel(0.0);
        }

        if (btnDistance.getPressed() && !btnAngle.get()){ sysChassis.goToDistance(22.0, true); }
        else if (btnAngle.getPressed() && !btnDistance.get()){ sysChassis.goToAngle(150.0, true); }

        if (!btnDistance.get()) sysChassis.disableDistancePID();
        if (!btnAngle.get()) sysChassis.disableAnglePID();

        if(!btnDistance.get() && !btnAngle.get() && !btnAlign.get()) sysChassis.disable();


        sysChassis.update();
        sysIntake.update(); 
        sysShooter.update();
        sysClimber.update();


        

}

}
