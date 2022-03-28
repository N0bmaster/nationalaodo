package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.advanced.DetectionPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.teamcode.drive.advanced.SamplePipeline;
import org.firstinspires.ftc.teamcode.drive.advanced.DetectionPipeline;

@Autonomous(name = "AUTONOMOUS_redext")
public class auto_redext extends LinearOpMode {
    OpenCvCamera webcam;
    SamplePipeline pipeline;
    DetectionPipeline detectionPipeline;
    private DcMotorEx slider;
    private DcMotorEx intake;
    private DcMotor carusel;
    private Servo cuva;
    private Servo rotire;
    boolean bCameraOpened = false;
    private ColorSensor color;

    //unfinished

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        slider = hardwareMap.get(DcMotorEx.class, "slider");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        carusel = hardwareMap.get(DcMotor.class, "carusel");
        cuva = hardwareMap.get(Servo.class,"cuva");
        rotire = hardwareMap.get(Servo.class,"rotire");
        color = hardwareMap.get(ColorSensor.class, "color");

        carusel.setDirection(DcMotorSimple.Direction.REVERSE);




        pipeline = new SamplePipeline();
        detectionPipeline = new DetectionPipeline();
        webcam.setPipeline(detectionPipeline);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ElapsedTime runtime2 = new ElapsedTime(0);




        Pose2d startPose = new Pose2d(0, 0, 0);

        drive.setPoseEstimate(startPose);

        Trajectory turnDuck = drive.trajectoryBuilder(startPose)
                .lineToSplineHeading(new Pose2d(-1,-20,Math.toRadians(-90)))
                .build();

        Trajectory allignWithHub = drive.trajectoryBuilder(turnDuck.end())
                .lineToSplineHeading(new Pose2d(-40,-20,Math.toRadians(90)))
                .build();


        Trajectory forwardToHub = drive.trajectoryBuilder(allignWithHub.end())
                .forward(15)
                .build();

        TrajectorySequence parkStorage = drive.trajectorySequenceBuilder(forwardToHub.end())
                .back(15)
                .strafeRight(15)
                .build();


        TrajectorySequence parkWarehouse = drive.trajectorySequenceBuilder(parkStorage.end())
                .strafeRight(5)
                .lineToSplineHeading(new Pose2d(0,0,Math.toRadians(90)))
                .forward(80)
                .build();
        //

        //x -9  y 7

        // x -1 y 20 pt duck

        // x -19 y 24 pt duck scan

        // x -37 y12 perpedincular cu shiphubu pt punerea duck

        // x -23 y 29 parking

        //----------------------------------------------------------------------------------------------


        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                bCameraOpened = true;
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        detectionPipeline.setGridSize(2);

        double left_avg,right_avg;

        int zone = 0;
        sleep(5000);
        while (!opModeIsActive() && !isStopRequested()) {
            //telemetry.addData("Zona", pipeline.getZone());
            cuva.setPosition(0.09);

            left_avg = (detectionPipeline.getZoneLuminosity(1) + detectionPipeline.getZoneLuminosity(2)) / 2;
            right_avg = (detectionPipeline.getZoneLuminosity(3) + detectionPipeline.getZoneLuminosity(4)) / 2;

            if (left_avg <= 125)
                zone = 2;
            else if (right_avg <= 125)
                zone = 3;
            else
                zone = 1;

            telemetry.addData("Zone", zone);
            telemetry.addData("Left", left_avg);
            telemetry.addData("Right", right_avg);

            telemetry.update();
        }
        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (!opModeIsActive()) return;

        drive.followTrajectory(turnDuck);
        sleep(500);
        drive.followTrajectory(allignWithHub);
        sleep(500);

        switch (zone)
        {
            case 1:
                slider.setTargetPosition(-200);
                slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slider.setPower(0.6);
                rotire.setPosition(0.95);

                break;
            case 2:
                slider.setTargetPosition(-1200);
                slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                slider.setPower(0.6);
                rotire.setPosition(0.95);
                break;

            case 3:
                slider.setTargetPosition(-1500);
                slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                slider.setPower(0.6);
                rotire.setPosition(0.75);
                break;
        }
        sleep(2000);

        drive.followTrajectory(forwardToHub);

        cuva.setPosition(0.5); // drop cube

        sleep(800);

        drive.followTrajectorySequence(parkStorage);

        //drive.followTrajectorySequence(parkWarehouse);
    }
}