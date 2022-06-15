 /* Copyright (c) 2017 FIRST. All rights reserved.
  *
  * Redistribution and use in source and binary forms, with or without modification,
  * are permitted (subject to the limitations in the disclaimer below) provided that
  * the following conditions are met:
  *
  * Redistributions of source code must retain the above copyright notice, this list
  * of conditions and the following disclaimer.
  *
  * Redistributions in binary form must reproduce the above copyright notice, this
  * list of conditions and the following disclaimer in the documentation and/or
  * other materials provided with the distribution.
  *
  * Neither the name of FIRST nor the names of its contributors may be used to endorse or
  * promote products derived from this software without specific prior written permission.
  *
  * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
  * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
  * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  */

 package org.firstinspires.ftc.teamcode;

 import com.acmerobotics.roadrunner.geometry.Pose2d;
 import com.acmerobotics.roadrunner.trajectory.Trajectory;
 import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
 import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
 import com.qualcomm.robotcore.hardware.ColorSensor;
 import com.qualcomm.robotcore.hardware.DcMotor;
 import com.qualcomm.robotcore.hardware.DcMotorEx;
 import com.qualcomm.robotcore.hardware.DcMotorSimple;
 import com.qualcomm.robotcore.hardware.Servo;
 import com.qualcomm.robotcore.util.ElapsedTime;

 import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
 import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
 import org.firstinspires.ftc.teamcode.drive.advanced.DetectionPipeline;
 import org.firstinspires.ftc.teamcode.drive.advanced.SamplePipeline;
 import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
 import org.openftc.easyopencv.OpenCvCamera;
 import org.openftc.easyopencv.OpenCvCameraFactory;
 import org.openftc.easyopencv.OpenCvCameraRotation;
import org.firstinspires.ftc.teamcode.util.RobotUtils;

 @TeleOp(name="NEWDRIVE", group="Linear Opmode")

 public class NewDrive extends LinearOpMode {



     private DcMotorEx intake;
     private Servo cuva;
     private DcMotorEx rotire;
     private DcMotorEx slider;
     private DcMotorEx carusel;
     private ColorSensor color;


     @Override
     public void runOpMode() throws InterruptedException {


         intake =hardwareMap.get(DcMotorEx.class,"intake");
         color = hardwareMap.get(ColorSensor.class, "color");
         cuva = hardwareMap.get(Servo.class,"cuva");
         rotire = hardwareMap.get(DcMotorEx.class,"rotire");
         slider = hardwareMap.get(DcMotorEx.class,"slider");
         carusel = hardwareMap.get(DcMotorEx.class,"carusel");
         int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
         OpenCvCamera webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
         slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

         slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

         rotire.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

         rotire.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

         boolean ok=true;
         RobotUtils robot = new RobotUtils(hardwareMap);

         ElapsedTime runtime = new ElapsedTime(0);

         SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

       //  drive.setPoseEstimate(new Pose2d(-40,0,-90));

         DetectionPipeline detectionPipeline = new DetectionPipeline();
         detectionPipeline.setGridSize(7);

         webcam.setPipeline(detectionPipeline);
         drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

         webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
             @Override
             public void onOpened() {
                 webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
             }

             @Override
             public void onError(int errorCode) {

             }
         });

//         while(!opModeIsActive())
//         {
//             telemetry.addData("Best zone", detectionPipeline.getBestZone());
//
//             for(int i = 1; i <= detectionPipeline.getGridSize() * detectionPipeline.getGridSize();i++)
//             {
//                 double lum = detectionPipeline.getZoneLuminosity(i);
//                 telemetry.addData("Zone " + i, lum);
//             }
//             telemetry.update();
//
//         }
         waitForStart();

         drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
         robot.startIntake(0.5);
         sleep(7000);
         robot.stopIntake();
/*
//         robot.startIntake(0.5);
//         sleep(5000);
//         robot.stopIntake();*/

         while (opModeIsActive()) {
             rotire.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

             telemetry.addData("Red", color.red());
             telemetry.addData("Green", color.green());
             telemetry.addData("Blue", color.blue());
             telemetry.addData("cuva",cuva.getPosition());
             telemetry.addData("slider",slider.getCurrentPosition());
             telemetry.addData("rotire",rotire.getCurrentPosition());
//             telemetry.addData("Best zone", detectionPipeline.getBestZone());
//
//             for(int i = 1; i <= detectionPipeline.getGridSize() * detectionPipeline.getGridSize();i++)
//             {
//                 double lum = detectionPipeline.getZoneLuminosity(i);
//                 telemetry.addData("Zone " + i, lum);
//             }

             telemetry.update();

             ElapsedTime runtime1 = new ElapsedTime(0);


            //am dat reverse la fata/spate

             drive.setWeightedDrivePower(
                     new Pose2d(
                             -gamepad1.left_stick_y,
                            - gamepad1.left_stick_x ,
                             -gamepad1.right_stick_x
                     )

             );
             drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
             drive.update();

            //outtake
             if(gamepad1.square) {
                 intake.setDirection(DcMotorSimple.Direction.FORWARD);
                 intake.setPower(0.7);
            //0.5
             }
             //intake

             if(gamepad1.circle) {
                 intake.setDirection(DcMotorSimple.Direction.REVERSE);
                 intake.setPower(0.7);
                 //0.6

             }
             if(gamepad1.triangle) {
                 intake.setPower(0);
             }

             //slider movement
           /*  if(gamepad2.left_stick_y>0) {
                 slider.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                 slider.setPower(0.9);
             }
             else if(gamepad2.left_stick_y<0)
             {   slider.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                 slider.setPower(-0.9);
             }*/




             //rotation
          /*   if(gamepad2.dpad_left)rotire.setPower(0.3);
             if(gamepad2.dpad_right)rotire.setPower(-0.3);
*/

             /* MY CODE
                  if(gamepad2.dpad_down) {
                 ok=false;   cuva.setPosition(0.5);
                 //runtime.reset();
             }
             else if(color.red() > 40 && color.green() > 40) {
                 counter++;
                 if(counter == 2) {
                     cuva.setPosition(0.09);
                     intake.setPower(0);
                     counter = 0;
                 }
                 //runtime.reset();
             }
             else if(!(color.red() > 40 && color.green() > 40))
             {
                 counter = 0;
                 cuva.setPosition(0.5);
             }

              */
            //170 190 210
             //cuva
             if(color.red() > 60 && color.green() > 60 && ok) {
                 cuva.setPosition(0.09);
                 intake.setPower(0);
                 //runtime.reset();
             }

            /* if(runtime.time() > 2 && !(color.red()>30&&color.green()>30))
             {
                 cuva.setPosition(0.5);
             }*/

             if(gamepad2.dpad_down) {
                 ok=false;   cuva.setPosition(0.5);
                 //runtime.reset();
             }
             if(gamepad2.dpad_up) {

                 ok = true;
                 rotire.setPower(0);

             }

//             if(gamepad1.dpad_left) {
//                 int bestZone = detectionPipeline.getBestZone();
//                 int column = detectionPipeline.getColumn(bestZone);
//                 double degrees = (column - 5) * 8;
//
//                 if(column != 4 && column != 5)
//                     drive.turn(Math.toRadians(-degrees));
//
//
//                 sleep(2000);
//
//                 ElapsedTime acceltime = new ElapsedTime(0);;
//
//                 acceltime.startTime();
//
//                 intake.setPower(-0.4);
//                 intake.setDirection(DcMotorSimple.Direction.REVERSE);
//
//                 boolean breakfrom = false;
//                 while (opModeIsActive() && !breakfrom) {
//                     double accel = Math.min(0.9, acceltime.time() * 0.05);
//                     telemetry.addData("accel",accel);
//                     telemetry.update();
//                     drive.setWeightedDrivePower(
//                             new Pose2d(
//                                     -(0.01 + accel),
//                                     0,
//                                     0
//                             )
//                     );
//
//                     drive.update();
//                     int c = 0;
//                     while (c < 30) {
//                         if (color.red() > 60 && color.green() > 60) {
//                             cuva.setPosition(0.09);
//                             intake.setDirection(DcMotorSimple.Direction.FORWARD);
//                             intake.setPower(0.7);
//                             breakfrom = true;
//                             break;
//                         }
//                         sleep(5);
//                         c++;
//                     }
//                 }
//                 intake.setPower(0);
//
//                 drive.setWeightedDrivePower(
//                         new Pose2d(
//                                 0,
//                                 0,
//                                 0
//                         )
//                 );
//                continue;
//            }

//
//             while(opModeIsActive() && degrees <= 25)
////        {
//            curPos = drive.getPoseEstimate();
//            drive.turn(Math.toRadians(2.5));
//
//            sleep(300);
//
//            duckZone = detectionPipeline.getDuckZone();
//            duckColumn = detectionPipeline.getColumn(duckZone);
//
//            telemetry.addData("DuckZone" , duckZone);
//            telemetry.addData("DuckCol" , duckColumn);
//            if(duckColumn == 5 || duckColumn == 6)
//                break;
//
//            telemetry.update();
//
//            degrees++;
//        }
             //slider auto buttons
             if(gamepad2.cross)
             {
                 slider.setTargetPosition(-1400);
                 slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                 slider.setPower(0.9);
                rotire.setTargetPosition(-1550);
                rotire.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rotire.setPower(-0.7);
                 

             }
             if(gamepad2.square)
             {
                 slider.setTargetPosition(-1200);
                 slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                 slider.setPower(0.9);
                 rotire.setTargetPosition(-1800);
                 rotire.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                 rotire.setPower(-0.7);
             }
             if(gamepad2.circle)
             {
                 slider.setTargetPosition(-200);
                 slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                 slider.setPower(0.9);
                 rotire.setTargetPosition(-1800);
                 rotire.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                 rotire.setPower(-0.7);
             }

             if(gamepad2.triangle)
             {
                 intake.setDirection(DcMotorSimple.Direction.FORWARD);
                 intake.setPower(0);
                 slider.setTargetPosition(0);
                 slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                 slider.setPower(-0.7);
                 rotire.setTargetPosition(-30);
                 rotire.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                 rotire.setPower(0.7);


             }
             if(gamepad1.dpad_right)
             {
                 runtime.reset();
                 while(runtime.time() < 1.2 && !gamepad1.dpad_up) {
                     carusel.setPower(0.5);
                 }

                 carusel.setPower(0.75);
             }
             if(gamepad1.dpad_left)
             {
                 runtime.reset();
                 while(runtime.time() < 1.2 && !gamepad1.dpad_up) {
                     carusel.setPower(-0.5);
                 }

                 carusel.setPower(-0.75);
             }
             if(gamepad1.dpad_up)carusel.setPower(0);

          /*  int runs = 0;

             if(gamepad1.right_bumper)
             {
                 Pose2d curPos = drive.getPoseEstimate();
                 Trajectory stickToWall = drive.trajectoryBuilder(curPos)
                         .lineToSplineHeading(new Pose2d(7 + runs * 7, curPos.getY(), Math.toRadians(90)))
                         .build();

                 drive.followTrajectory(stickToWall);
                 runs++;
             }
             else if(gamepad1.left_bumper)
             {

             }
*/


             //telemetry








         }
     }
 }
