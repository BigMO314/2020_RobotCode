package frc.robot.subsystem;

import frc.molib.DashTable;
import frc.molib.PIDController;
import frc.molib.sensors.MagEncoder;
import frc.molib.DashTable.DashEntry;
import static frc.robot.Robot.tblTroubleshooting;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class Shooter {
    private TalonFX mtrFlywheel_L = new TalonFX(7);
    private TalonFX mtrFlywheel_R = new TalonFX(8);

    private MagEncoder encFlywheel = new MagEncoder(mtrFlywheel_L);

    private PIDController pidFlywheelSpeed = new PIDController (0 , 0 , 0);

    
    

    private static final class DebugDashboard {

        public static DashTable tblShooter = tblTroubleshooting.new SubTable("Shooter");
        
        public static DashEntry<Double> FlywheelSpeed = tblShooter.new DashEntry<Double>("Flywheel Speed");

   
    }

    private static final Shooter INSTANCE = new Shooter();
    public static Shooter getInstance() { return INSTANCE; }
    
    private double flywheelPower = 0.0;

    public Shooter(){
        mtrFlywheel_L.setInverted(false);
        mtrFlywheel_R.setInverted(true);

        encFlywheel.configDistancePerPulse(1.0/2048.0);

        pidFlywheelSpeed.configOutputRange(-1.0, 1.0);

        pidFlywheelSpeed.enable();
    
    }

    public void init(){
       
    }


    public void setShoot(double flywheelPower){
        this.flywheelPower = flywheelPower;
    }

    public double getSpeed() { return encFlywheel.getRate(); }
    public void setSpeed(double speed) { pidFlywheelSpeed.setSetpoint(speed); }
    public boolean isAtSpeed() { return pidFlywheelSpeed.atSetpoint(); }

    public void enable(){
        setShoot(1.0);
    }

    public void disable(){
        setShoot(0.0);
    }


    public void update(){
        
      
        
        setShoot(pidFlywheelSpeed.calculate(encFlywheel.getRate()));
            
        
        
        mtrFlywheel_L.set(ControlMode.PercentOutput , flywheelPower );
        mtrFlywheel_R.set(ControlMode.PercentOutput , flywheelPower );

        DebugDashboard.FlywheelSpeed.set(getSpeed());
        
       
    }
}