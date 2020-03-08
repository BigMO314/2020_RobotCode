package frc.robot.period;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.Console;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Climber;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

@SuppressWarnings("unused")
public class Autonomous {
    public enum Sequence { 
        kNone("Do Nothing") ,
        kCrossLineForward("Cross Line: Drive Foward"),
        kCrossLineBackward("Cross Line: Drive Backward"),
        kThreeBallForward("Three Ball: Drive Forward"),
        kThreeBallBackward("Three Ball: Drive Backward"),
        kFiveBallForward("Five Ball: Drive Forward - Incomplete"),
        kFiveBallBackward("Five Ball: Drive Backward - Incomplete"),
        kTrench("Trench");
        
        

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
    private Climber sysClimber = Climber.getInstance();

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
        /*
        for(Position tempPosition : Position.values())
            chsPosition.addOption(tempPosition.label , tempPosition);
        SmartDashboard.putData("Position Selector" , chsPosition);
        */
    }


    public void init(){
        Console.logMsg("Autonomous initializing...");
        Robot.initSystems();
        Robot.disableSystems();
        Console.logMsg("Systems disabled");
        stage = 0;

        selectedSequence = chsSequence.getSelected();
        Console.logMsg("Sequence read");
        tmrTimeout.start();
        Console.logMsg("Timer started");
    }

    private void sequence_kNone(){
        
        switch(stage) {
            
            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kNone.label +  "\""); stage++; break;
            
            case 1 : Console.logMsg("Doing nothing..."); stage++; break;

            case 2 : Console.logMsg("End Sequence: \"" + Sequence.kNone.label + "\""); stage++; break;

            default : Robot.disableSystems();
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
                sysShooter.enableFlywheelNear(); 
                sysIntake.armExtend(); 
                stage++; break;
            
            case 3 : 
                if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : Console.logMsg("Shooting balls..."); 
                sysShooter.enableHopper(); 
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
                sysShooter.enableFlywheelNear(); 
                sysIntake.armExtend(); 
                stage++; break;
            
            case 3 : 
                if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : Console.logMsg("Shooting balls..."); 
                sysShooter.enableHopper(); 
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

            case 2 : sysShooter.enableFlywheelNear(); sysIntake.armExtend(); stage ++; break;
            
            case 3 : if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : sysShooter.enableHopper(); tmrTimeout.reset(); stage++; break;
            
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

            case 2 : sysShooter.enableFlywheelNear(); sysIntake.armExtend(); stage ++; break;
            
            case 3 : if (sysShooter.isFlywheelAtSpeed()) stage++; break;

            case 4 : Console.logMsg("Shooting balls..."); sysShooter.enableHopper(); tmrTimeout.reset(); stage++; break;
            
            case 5 : if (tmrTimeout.get() > 7.0) ; stage++; break;

            case 6 : Console.logMsg("Driving backward 3'..."); sysShooter.disable(); sysIntake.armRetract(); sysChassis.goToDistance(36.0); tmrTimeout.reset(); stage++; break;

            case 7 : if (sysChassis.isAtDistance() || tmrTimeout.get() > 5.0) stage++; break;

            case 8 : Console.logMsg("End Sequence: \"" + Sequence.kThreeBallBackward.label + "\""); stage++; break;

            default : Robot.disableSystems();
    }
    }

    private void sequence_kTrench(){

        switch(stage){

            case 0 : Console.logMsg("Start Sequence: \"" + Sequence.kTrench.label +  "\""); 
                stage++; break;

            case 1 : Console.logMsg("Turning left 32 degrees, getting flywheel up to speed..."); 
                sysChassis.goToAngle(-32.0); 
                sysIntake.armExtend();
                sysShooter.enableFlywheelNear(); 
                tmrTimeout.reset(); 
                stage++; break;

            case 2 : 
                if (sysShooter.isFlywheelAtSpeed() && (sysChassis.isAtAngle() || tmrTimeout.get() > 2.0)) stage ++; break;

            case 3 : Console.logMsg("Shooting balls..."); 
                sysChassis.disableAnglePID();
                sysIntake.enableRoller();
                sysShooter.enableHopper();
                tmrTimeout.reset(); 
                stage++; break;
            
            case 4 : 
                if (tmrTimeout.get() > 2.0) stage++; break; // Was 2.5
            
            case 5 : Console.logMsg("Turning left 155 degrees..."); 
                sysChassis.goToAngle(-155.0); 
                sysIntake.disableRoller(); 
                sysShooter.disable(); 
                tmrTimeout.reset();
                stage++ ; break;

            case 6 :  
                if (sysChassis.isAtAngle() || tmrTimeout.get() > 3.0) stage++; break;

            case 7 : Console.logMsg("Enabling roller, driving forward 12'..."); 
                sysChassis.disableAnglePID();
                sysChassis.goToDistance(144.0); 
                sysIntake.enableRoller();
                tmrTimeout.reset();
                stage++; break;

            case 8 : 
                if (sysChassis.isAtDistance() || tmrTimeout.get() > 2.75) stage++; break;

             case 9 : Console.logMsg("Disabling Intake, turning right 165 degrees..."); 
                sysChassis.disableDistancePID();
                sysChassis.goToAngle(165.0);
                //sysIntake.armRetract();
                sysIntake.disableRoller();
                tmrTimeout.reset(); 
                stage++; break;
            
            case 10 : 
                if (sysChassis.isAtAngle() || tmrTimeout.get() > 3.0) stage++; break;

            case 11 :  Console.logMsg("Driving forward 6.5' and getting flywheel up to speed..."); 
                sysChassis.disableAnglePID();
                sysChassis.goToDistance(78.0);
                sysShooter.enableFlywheelFar();
                tmrTimeout.reset(); 
                stage++; break;

            case 12 : 
                if ((sysChassis.isAtDistance() && sysShooter.isFlywheelAtSpeed())|| tmrTimeout.get() > 3.0) stage++; break;


            case 14 : Console.logMsg("Shooting balls..."); 
                sysChassis.disableDistancePID();
                sysIntake.enableRoller();
                sysShooter.enableHopper(); 
                tmrTimeout.reset(); 
                stage++; break;
            
            case 15 : 
                if (tmrTimeout.get() > 4.0) stage++; break;

            case 16 : Console.logMsg("End Sequence: \"" + Sequence.kTrench.label + "\""); stage++; break;

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

            case kTrench : sequence_kTrench(); break;


            default : // Do ABSOLUTELY nothing.
        }

        sysChassis.update();
        sysIntake.update();
        sysShooter.update();
        sysClimber.update();
        
        
    }

}