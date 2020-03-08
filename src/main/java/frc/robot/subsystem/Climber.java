package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;

@SuppressWarnings("unused")
public class Climber {
    
    private VictorSPX mtrWinch = new VictorSPX(9);
    private Solenoid solRelease = new Solenoid(2);

    private double winchPower = 0.0;
    private boolean releaseState = false;

    private static final Climber INSTANCE = new Climber();
    public static Climber getInstance() { return INSTANCE; }

    

    public void init() {

    }

    public void disable() {
        disableWinch();
        setRelease(false);
    }

    public void setWinch(double power) { winchPower = power; }
    public void enableWinch() { setWinch(1.0); }
    public void disableWinch() { setWinch(0.0); }

    public void setRelease(boolean released) { releaseState = released; }
    public void release() { setRelease(true); }

    public void update() {
        mtrWinch.set(ControlMode.PercentOutput, winchPower);
        solRelease.set(releaseState);
    }
    
}