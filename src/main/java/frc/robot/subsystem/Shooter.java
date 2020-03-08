package frc.robot.subsystem;

import frc.molib.DashTable;
import frc.molib.PIDController;
import frc.molib.sensors.MagEncoder;
import frc.molib.DashTable.DashEntry;
import static frc.robot.Robot.tblTroubleshooting;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Shooter { 
    private VictorSPX mtrHopper = new VictorSPX(5);
    private TalonFX mtrFlywheel_L = new TalonFX(6);
    private TalonFX mtrFlywheel_R = new TalonFX(7);

    private MagEncoder encFlywheel = new MagEncoder(mtrFlywheel_L);

    private PIDController pidFlywheelSpeed = new PIDController (0.0 , 0.0003 , 0.0);

    
    

    private static final class DebugDashboard {

        public static DashTable tblShooter = tblTroubleshooting.new SubTable("Shooter");
        
        public static DashEntry<Double> FlywheelSpeed = tblShooter.new DashEntry<Double>("Flywheel Speed");

   
    }

    private static final Shooter INSTANCE = new Shooter();
    public static Shooter getInstance() { return INSTANCE; }
    
    private double hopperPower = 0.0;
    private double flywheelPower = 0.0; 

    public Shooter(){
        mtrHopper.setInverted(false);
        mtrFlywheel_L.setInverted(false);
        mtrFlywheel_R.setInverted(true);  

        encFlywheel.configDistancePerPulse(60.0/2048.0); // DPP

        pidFlywheelSpeed.configOutputRange(-1.0, 1.0);

        pidFlywheelSpeed.setTolerance(20.0, 200.0);
    
    }


    public void setHopper( double hopperPower ){
        this.hopperPower = hopperPower;
    }


    public void setFlywheel(double flywheelPower){
        this.flywheelPower = flywheelPower;
    }

    public double getFlywheelSpeed() { return encFlywheel.getRate(); }
    public void setFlywheelSpeed(double speed) { 
        
        enableFlywheelPID();
        pidFlywheelSpeed.setSetpoint(speed);
     }
    public boolean isFlywheelAtSpeed() { return pidFlywheelSpeed.atSetpoint(); }

    public void enableFlywheelPID(){ 
        if(!pidFlywheelSpeed.isEnabled())
            pidFlywheelSpeed.reset();
        pidFlywheelSpeed.enable(); 
    }
    public void disableFlywheelPID(){ pidFlywheelSpeed.disable(); }

 
   
    public void enableHopper(){ setHopper(0.65);}

    public void enableFlywheelNear(){ setFlywheelSpeed(5000.0);}
    public void enableFlywheelFar() { setFlywheelSpeed(5250.0);}

    public void disableHopper(){ setHopper(0.0); }
    public void disableFlywheel(){ setFlywheelSpeed(0.0);}
    


    public void disable(){
        disableHopper();
        setFlywheelSpeed(0.0);
    }

    public void init(){
       
    }

    public void update(){
        
        if (pidFlywheelSpeed.isEnabled()){
            setFlywheel(pidFlywheelSpeed.calculate(encFlywheel.getRate()));
        }
        
        
            
        
        
        mtrFlywheel_L.set(ControlMode.PercentOutput , flywheelPower );
        mtrFlywheel_R.set(ControlMode.PercentOutput , flywheelPower );
        mtrHopper.set(ControlMode.PercentOutput , hopperPower);

        DebugDashboard.FlywheelSpeed.set(getFlywheelSpeed());
        
       
    }
}