package frc.robot.period;

import frc.molib.humancontrols.buttons.Button;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class Test {
    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    private Button btnRoller = new Button("Roller");
    private Button btnArm = new Button("Intake Arm");
    private Button btnHopper = new Button("Hopper");
    private Button btnFlywheel = new Button("Flywheel");


    private static final Test INSTANCE = new Test();
    public static Test getInstance() { return INSTANCE; }


    public void init(){
        
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


        sysChassis.update();
        sysIntake.update(); 
        sysShooter.update();


        

}

}
