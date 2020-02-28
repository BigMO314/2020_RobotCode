package frc.robot.subsystem;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.kauailabs.navx.frc.AHRS;


import frc.molib.PIDController;
import frc.molib.sensors.MagEncoder;
import frc.molib.vision.Limelight;



public class Chassis {

    private TalonFX mtrDrive_L1 = new TalonFX(0);
    private TalonFX mtrDrive_L2 = new TalonFX(1);

    private TalonFX mtrDrive_R1 = new TalonFX(2);
    private TalonFX mtrDrive_R2 = new TalonFX(3);

    private MagEncoder encDrive = new MagEncoder(mtrDrive_L1);

    private final AHRS gyrDrive = new AHRS(); 

    private PIDController pidDriveAngle = new PIDController(0.0, 0.0, 0.0);
    private PIDController pidDriveDistance = new PIDController(0.0, 0.0, 0.0);
    private PIDController pidDriveStraight = new PIDController(0.0 , 0.0 , 0.0); // Helps to prevent the Drivetrain from drifting
    private PIDController pidDriveVision = new PIDController(0.0 , 0.0 , 0.0); // Limelight PID
   


    private static Chassis INSTANCE = new Chassis();
    public static Chassis getInstance() { return INSTANCE; }

    private double lPower = 0.0;
    private double rPower = 0.0;


    public Chassis(){
        mtrDrive_L1.setInverted(true);
        mtrDrive_L2.setInverted(true);

        mtrDrive_R1.setInverted(false);
        mtrDrive_R2.setInverted(false);
    }

    public void setDrive(double lPower , double rPower){
        this.lPower = lPower;
        this.rPower = rPower;
    }

    public void setArcade(double throttle, double steering) { setDrive(throttle + steering, throttle - steering); }

    public void enableAnglePID(){ 
        pidDriveAngle.enable();
        pidDriveDistance.disable();
        pidDriveStraight.disable();
        pidDriveVision.disable();
    }

    public void enableDistancePID(){ 
        pidDriveAngle.disable();
        pidDriveDistance.enable();
        pidDriveStraight.enable();
        pidDriveVision.disable();
    }

    public void enableVisionPID(){ 
        pidDriveAngle.enable();
        pidDriveDistance.disable();
        pidDriveStraight.disable();
        pidDriveVision.disable();
    }



    // [ Angle PID functions ]
    
    /* Comments to functions here apply to all following sections
    */

    public double getAngle() { return gyrDrive.getAngle(); } // Return the current angle (from gyro)
    public void resetAngle() { gyrDrive.reset(); }
    public boolean isAtAngle() { return pidDriveAngle.atSetpoint();} // Is true only if the desired angle has been calculated
    public void goToAngle(double angle){ goToAngle(angle, false); }
    // Overrides PID to reset if it is not already enabled or reset
    public void goToAngle(double angle, boolean reset) {
        if (!pidDriveAngle.isEnabled() || reset) {
            pidDriveAngle.reset();

            enableAnglePID();
            
            resetAngle();
          
        }
        
        pidDriveAngle.setSetpoint(angle);
    }


    // [ Distance PID functions ]

    public double getDistance() { return encDrive.getDistance(); }
    public void resetDistance() { encDrive.reset(); }
    public boolean isAtDistance() { return pidDriveDistance.atSetpoint() && pidDriveStraight.atSetpoint();}
    public void goToDistance(double distance){ goToDistance(distance, false); }
    public void goToDistance(double distance, boolean reset) {
        if (!pidDriveDistance.isEnabled() || reset) {
            pidDriveDistance.reset();
            pidDriveStraight.reset();

           
            resetAngle();
            resetDistance();
        }

        pidDriveAngle.disable();
        pidDriveDistance.enable();
        pidDriveStraight.enable();
        pidDriveVision.disable();

        pidDriveDistance.setSetpoint(distance);
        pidDriveStraight.setSetpoint(0.0);
    }

    //  [ Vision PID functions ]

    public double getVisionPosX() { return Limelight.getPosX(); }
    public boolean isAtVisionTarget() { return pidDriveVision.atSetpoint();}
    public void goToVisionTarget() {

        pidDriveAngle.disable();
        pidDriveDistance.disable();
        pidDriveStraight.disable();
        pidDriveVision.enable();

        pidDriveVision.setSetpoint(0.0);
    }

    public void disableAnglePID(){
        pidDriveAngle.disable();
    }

    public void disableDistancePID(){
        pidDriveDistance.disable();
    }

    public void disableStraightPID(){
        pidDriveStraight.disable();
    }
    
    public void disableVisionPID(){
        pidDriveVision.disable();
    }

    public void disable(){

        disableAnglePID();
        disableDistancePID();
        disableStraightPID();
        disableVisionPID();

        setDrive( 0.0 , 0.0 );
    }

    public void init(){

    }

    // Main update loop for the Chassis
    public void update(){
        
        // Drive to desired distance
        if (pidDriveDistance.isEnabled()) {
            setArcade(pidDriveDistance.calculate(getDistance()) , pidDriveStraight.calculate(getAngle()));
        } else if (pidDriveAngle.isEnabled()) {
            setArcade(0.0, pidDriveStraight.calculate(getAngle()));
        }  

        mtrDrive_L1.set(ControlMode.PercentOutput , lPower);
        mtrDrive_L2.set(ControlMode.PercentOutput , lPower);
        mtrDrive_R1.set(ControlMode.PercentOutput , rPower);
        mtrDrive_R2.set(ControlMode.PercentOutput , rPower);
    }
}
