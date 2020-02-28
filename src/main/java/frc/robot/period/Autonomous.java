package frc.robot.period;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.Console;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class Autonomous {
    public enum Sequence { 
        kNone("Do Nothing") ,
        kCrossLine("Cross line");
        

        public final String label;
        private Sequence(String label){
            this.label = label;
        }

    }

    public enum Position {
        kLeft("Left") ,
        kCenter("Right") , 
        kRight("Right");


        public final String label;
        private Position(String label){
            this.label = label;
        }
    }

    private Chassis sysChassis = Chassis.getInstance();
    private Intake sysIntake =  Intake.getInstance();
    private Shooter sysShooter = Shooter.getInstance();

    private SendableChooser<Sequence> chsSequence = new SendableChooser<Sequence>();
    private Sequence selectedSequence;
    private SendableChooser<Position> chsPosition = new SendableChooser<Position>();
   
    private  int stage;

    private Timer tmrTimeout = new Timer();


    private static Autonomous INSTANCE = new Autonomous();
    public static Autonomous getInstance() { return INSTANCE; }
    

    public Autonomous(){
        for(Sequence tempSequence : Sequence.values())
            chsSequence.addOption(tempSequence.label, tempSequence);
        chsSequence.setDefaultOption(Sequence.kNone.label , Sequence.kNone);

        SmartDashboard.putData("Autonomous Sequence" , chsSequence);

        for(Position tempPosition : Position.values())
            chsPosition.addOption(tempPosition.label , tempPosition);
        SmartDashboard.putData("Position Selector" , chsPosition);
    }


    public void init(){
        
        stage = 0;

        selectedSequence = chsSequence.getSelected();

        tmrTimeout.start();
    }

    private void sequence_kNone(){
        
        switch(stage) {
            
            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kNone.label +  "\""); stage++; break;
            
            case 1 : Console.logMsg("Doing nothing..."); stage++; break;

            case 2 : Console.logMsg("End Sequence: \"" + Sequence.kNone.label + "\""); stage++; break;

            default : // No.
        }
    }

    private void sequence_kCrossLine(){
            
            switch(stage) {

            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kCrossLine.label +  "\""); stage++; break;

            case 1 : Console.logMsg("Driving backwards 3'..."); stage++; break;

            case 2 : sysChassis.goToDistance(-36.0); tmrTimeout.reset(); stage++; break;

            case 3 : if (sysChassis.isAtDistance() || tmrTimeout.get()> 0.5){ stage++; } break;

            case 4 : Console.logMsg("End Sequence: \"" + Sequence.kCrossLine.label + "\""); stage++; break;

            default : Robot.disableSystems();
                
            }
    }

    public void update(){
        
        switch(selectedSequence){

            case kNone : sequence_kNone(); break;

            case kCrossLine : sequence_kCrossLine(); break;

            default : // Do ABSOLUTELY nothing.
        }

        sysChassis.update();
        sysIntake.update();
        sysShooter.update();
        
        
    }

}