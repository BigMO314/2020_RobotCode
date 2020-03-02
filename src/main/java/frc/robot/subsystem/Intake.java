package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Intake{

    private static VictorSPX mtrRoller = new VictorSPX(4);

    private DoubleSolenoid solArm = new DoubleSolenoid(0 , 1);

    private static final Intake 
        INSTANCE = new Intake();
    public static Intake getInstance() { return INSTANCE; }

    
    private double rollerPower = 0.0;


    private boolean isArmExtended = false;

    public Intake(){
        mtrRoller.setInverted(true);
    }

    public void setArm (boolean isArmExtended){
        this.isArmExtended = isArmExtended;
    }

    public void armExtend(){ setArm(true); }

    public void armRetract(){ setArm(false); }

    
    public void setRoller(double rollerPower){
        this.rollerPower = rollerPower;
    }

    public void enableRoller(){ setRoller(0.75); }

    public void disableRoller(){ setRoller(0.0); }

    public void reverseRoller(){ setRoller(-0.75); }

    // This function is the main update loop for the Intake.
    public void update(){
        /* This set function allows for the solenoid to read two positions to control the arm.
        */
        solArm.set(isArmExtended ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        

        mtrRoller.set(ControlMode.PercentOutput , rollerPower);
    }
    
}