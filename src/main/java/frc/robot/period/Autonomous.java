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
        kCrossLineForward("Cross Line: Drive Foward"),
        kCrossLineBackward("Cross Line: Drive Backward"),
        kThreeBallForward("Three Ball: Drive Forward"),
        kThreeBallBackward("Three Ball: Drive Backward"),
        kFiveBallForward("Five Ball: Drive Forward"),
        kFiveBallBackward("Five Ball: Drive Backward");
        

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

        Robot.disableSystems();
        
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

    private void sequence_kCrossLineForward(){
            
            switch(stage) {

            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kCrossLineForward.label +  "\""); stage++; break;

            case 1 : Console.logMsg("Driving forwards 3'..."); stage++; break;

            case 2 : sysChassis.goToDistance(36.0); tmrTimeout.reset(); stage++; break;

            case 3 : if (sysChassis.isAtDistance() || tmrTimeout.get()> 0.5){ stage++; } break;

            case 4 : Console.logMsg("End Sequence: \"" + Sequence.kCrossLineForward.label + "\""); stage++; break;

            default : Robot.disableSystems();
                
            }
    }

    private void sequence_kCrossLineBackward(){
            
        switch(stage) {

        case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kCrossLineBackward.label +  "\""); stage++; break;

        case 1 : Console.logMsg("Driving backwards 3'..."); stage++; break;

        case 2 : sysChassis.goToDistance(-36.0); tmrTimeout.reset(); stage++; break;

        case 3 : if (sysChassis.isAtDistance() || tmrTimeout.get()> 0.5){ stage++; } break;

        case 4 : Console.logMsg("End Sequence: \"" + Sequence.kCrossLineBackward.label + "\""); stage++; break;

        default : Robot.disableSystems();
            
        }
}

    private void sequence_kThreeBallForward(){

        switch(stage) {

            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kThreeBallForward.label +  "\""); 
                stage++; break;

            case 1 : Console.logMsg("Getting Flywheel up to speed..."); 
                stage++; break;

            case 2 : 
                sysShooter.enableFlywheel(); 
                sysIntake.armExtend(); 
                stage++; break;
            
            case 3 : 
                if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : Console.logMsg("Shooting balls..."); 
                sysShooter.enableHoopper(); 
                sysIntake.enableRoller();
                tmrTimeout.reset(); 
                stage++; break;
            
            case 5 :  
                if (tmrTimeout.get() > 3.0) stage++; break;

            case 6 : Console.logMsg("Driving forward 3'..."); 
                sysShooter.disable(); 
                sysIntake.armRetract(); 
                sysChassis.goToDistance(36.0); 
                tmrTimeout.reset(); 
                stage++; break;

            case 7 : 
                if (sysChassis.isAtDistance() || tmrTimeout.get() > 3.0) stage++; break;

            case 8 : Console.logMsg("End Sequence: \"" + Sequence.kThreeBallForward.label + "\""); 
                stage++; break;

            default : Robot.disableSystems();
            
        }
    }

    private void sequence_kThreeBallBackward(){

        switch(stage) {

            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kThreeBallBackward.label +  "\""); 
                stage++; break;

            case 1 : Console.logMsg("Getting Flywheel up to speed..."); 
                stage++; break;

            case 2 : 
                sysShooter.enableFlywheel(); 
                sysIntake.armExtend(); 
                stage++; break;
            
            case 3 : 
                if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : Console.logMsg("Shooting balls..."); 
                sysShooter.enableHoopper(); 
                sysIntake.enableRoller();
                tmrTimeout.reset(); 
                stage++; break;
            
            case 5 :  
                if (tmrTimeout.get() > 3.0) stage++; break;

            case 6 : Console.logMsg("Driving backward 3'..."); 
                sysShooter.disable(); 
                sysIntake.armRetract(); 
                sysChassis.goToDistance(-36.0); 
                tmrTimeout.reset(); 
                stage++; break;

            case 7 : 
                if (sysChassis.isAtDistance() || tmrTimeout.get() > 3.0) stage++; break;

            case 8 : Console.logMsg("End Sequence: \"" + Sequence.kThreeBallBackward.label + "\""); 
                stage++; break;

            default : Robot.disableSystems();
            
        }
    }

    private void sequence_kFiveBallForward(){

        switch(stage) {

            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kThreeBallForward.label +  "\""); stage++; break;

            case 1 : Console.logMsg("Shooting balls.."); stage++; break;

            case 2 : sysShooter.enableFlywheel(); sysIntake.armExtend(); stage ++; break;
            
            case 3 : if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : sysShooter.enableHoopper(); tmrTimeout.reset(); stage++; break;
            
            case 5 : if (tmrTimeout.get() > 5.0); stage++; break;

            case 6 : Console.logMsg("Driving forward 3'..."); sysShooter.disable(); sysIntake.armRetract(); sysChassis.goToDistance(36.0); tmrTimeout.reset(); stage++; break;

            case 7 : if (sysChassis.isAtDistance() || tmrTimeout.get() > 5.0) stage++; break;

            case 8 : Console.logMsg("End Sequence: \"" + Sequence.kThreeBallForward.label + "\""); stage++; break;

            default : Robot.disableSystems();
            
        }
    }

    private void sequence_kFiveBallBackward(){

        switch(stage){

            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kThreeBallBackward.label +  "\""); stage++; break;

            case 1 : Console.logMsg("Getting Flywheel up to speed..."); stage++; break;

            case 2 : sysShooter.enableFlywheel(); sysIntake.armExtend(); stage ++; break;
            
            case 3 : if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : Console.logMsg("Shooting balls..."); sysShooter.enableHoopper(); tmrTimeout.reset(); stage++; break;
            
            case 5 : if (tmrTimeout.get() > 7.0) ; stage++; break;

            case 6 : Console.logMsg("Driving backward 3'..."); sysShooter.disable(); sysIntake.armRetract(); sysChassis.goToDistance(36.0); tmrTimeout.reset(); stage++; break;

            case 7 : if (sysChassis.isAtDistance() || tmrTimeout.get() > 5.0) stage++; break;

            case 8 : Console.logMsg("End Sequence: \"" + Sequence.kThreeBallBackward.label + "\""); stage++; break;

            default : Robot.disableSystems();
    }
    }

    public void update(){
        
        switch(selectedSequence){

            case kNone : sequence_kNone(); break;

            case kCrossLineForward : sequence_kCrossLineForward(); break;

            case kCrossLineBackward : sequence_kCrossLineBackward(); break ;

            case kThreeBallForward : sequence_kThreeBallForward(); break;

            case kThreeBallBackward : sequence_kThreeBallBackward(); break;

            case kFiveBallForward : sequence_kFiveBallForward(); break;

            case kFiveBallBackward : sequence_kFiveBallBackward(); break;

            default : // Do ABSOLUTELY nothing.
        }

        sysChassis.update();
        sysIntake.update();
        sysShooter.update();
        
        
    }

}