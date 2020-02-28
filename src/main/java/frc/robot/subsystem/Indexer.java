package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Indexer {

    private static VictorSPX mtrRoller = new VictorSPX(4);
    private DoubleSolenoid solArm = new DoubleSolenoid(0 , 1);

    private VictorSPX mtrHopper = new VictorSPX(5);
    

    
    private static final Indexer INSTANCE = new Indexer();
    public static Indexer getInstance() { return INSTANCE; }

    private double hopperPower = 0.0;
    private double rollerPower = 0.0;

    private boolean isArmExtended = false;


    public Indexer() {
        mtrRoller.setInverted(true);
        mtrHopper.setInverted(false);
    }

    // Intake functions

    public void setArm (boolean isArmExtended){
        this.isArmExtended = isArmExtended;
    }

    public void armExtend(){ setArm(true); }

    public void armRetract(){ setArm(false); }

    
    public void setRoller(double rollerPower){
        this.rollerPower = rollerPower;
    }

    public void enableRoller(){ setRoller(1.0); }

    public void disableRoller(){ setRoller(0.0); }

    public void reverseRoller(){ setRoller(-1.0); }


    // Hopper functions
    
    public void setHopper( double hopperPower ){
        this.hopperPower = hopperPower;
    }



    public void enableHoopper(){ setHopper(1.0);}




    public void disableHopper(){ setHopper(0.0); }


    
    public void enable(){
        enableHoopper();
        enableRoller();
        
    }

    public void disable(){
        disableHopper();
        disableRoller();
    }

    

    


    public void update(){
       
        solArm.set(isArmExtended ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        /* This set function allows for the solenoid to read two positions to control the arm. Forward if true,
        reverse if not true.
        */
        mtrRoller.set(ControlMode.PercentOutput , rollerPower);
        mtrHopper.set(ControlMode.PercentOutput , hopperPower);
    }
    
    
}