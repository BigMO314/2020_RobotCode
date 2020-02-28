package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

@SuppressWarnings("unused")
public class Climber {
    
    private VictorSPX mtrClimber_L = new VictorSPX(9);
    private VictorSPX mtrClimber_R = new VictorSPX(10);

    private static final Shooter INSTANCE = new Shooter();
    public static Shooter getInstance() { return INSTANCE; }
    
}