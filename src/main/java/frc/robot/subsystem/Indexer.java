package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Indexer {

    private VictorSPX mtrHopper = new VictorSPX(5);
    private VictorSPX mtrFeed = new VictorSPX(6);

    private static final Indexer INSTANCE = new Indexer();
    public static Indexer getInstance() { return INSTANCE; }

    private double hopperPower = 0.0;
    private double feedPower = 0.0;

    public Indexer() {
        mtrHopper.setInverted(false);
        mtrFeed.setInverted(false);
    }
    
    public void setHopper( double hopperPower ){
        this.hopperPower = hopperPower;
    }

    public void setFeed( double feedPower ){
        this.feedPower = feedPower;
    }

    public void enableHoopper(){ setHopper(1.0);}

    public void enableFeed(){ setFeed(1.0); }


    public void disableHopper(){ setHopper(0.0); }

    public void disableFeed(){ setFeed(0.0); }

    
    public void enable(){
        enableHoopper();
        enableFeed();
    }

    public void disable(){
        disableHopper();
        disableFeed();
    }


    public void update(){
        mtrHopper.set(ControlMode.PercentOutput , hopperPower);
        mtrFeed.set(ControlMode.PercentOutput , feedPower); 
    }
    
    
}