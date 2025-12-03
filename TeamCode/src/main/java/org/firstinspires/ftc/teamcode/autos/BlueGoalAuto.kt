package org.firstinspires.ftc.teamcode.autos

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.subsystems.ArtifactCycle
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems


@Autonomous
class BlueGoalAuto : LinearOpMode() {
    override fun runOpMode() {
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
        var opModeState = 0
        var opModeTimer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        transferMotor.inverted = true

        val shooter = Shooter(MotorEx(hardwareMap, "shooterMotor"))
        shooter.tps = Subsystems.Shooter.targetTPS

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(25.0, 128.0, Math.toRadians(144.0)))

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        // First 3 Artifacts Pickup Paths
        //
        ///////////////////////////////////////////////////////////////////////////////////////////

        val driveToArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(25.000, 128.000),
                    Pose(72.000, 108.000),
                    Pose(48.000, 84.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(144.0), Math.toRadians(180.0))
            .build()

        val pickupArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(48.000, 84.000),
                    Pose(16.000, 84.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build()

        val driveToGoal1Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(16.000, 84.000),
                    Pose(36.000, 108.000),
                    Pose(25.000, 128.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(144.0))
            .build();

        val cycle1 = ArtifactCycle(
            follower,
            driveToArtifact1Path,
            pickupArtifact1Path,
            driveToGoal1Path,
            shooter,
            intakeMotor,
            transferMotor,
            telemetryJ
        )

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        // Second 3 Artifacts Pickup Paths
        //
        ///////////////////////////////////////////////////////////////////////////////////////////

        val driveToArtifact2Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(25.000, 128.000),
                    Pose(72.000, 84.000),
                    Pose(48.000, 50.000)
                )
            )

            .setLinearHeadingInterpolation(Math.toRadians(144.0), Math.toRadians(180.0))
            .build()

        val pickupArtifact2Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(48.000, 50.000),
                    Pose(16.000, 50.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build()

        val driveToGoal2Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(16.000, 50.000),
                    Pose(36.000, 84.000),
                    Pose(20.000, 120.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(144.0))
            .build();

        val cycle2 = ArtifactCycle(
            follower,
            driveToArtifact2Path,
            pickupArtifact2Path,
            driveToGoal2Path,
            shooter,
            intakeMotor,
            transferMotor,
            telemetryJ
        )

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        // Third 3 Artifacts Pickup Paths
        //
        ///////////////////////////////////////////////////////////////////////////////////////////

//        val driveToArtifact3Path = follower
//            .pathBuilder()
//            .addPath(
//                BezierCurve(
//                    Pose(25.000, 128.000),
//                    Pose(72.000, 60.000),
//                    Pose(42.000, 36.000)
//                )
//            )
//            .setLinearHeadingInterpolation(Math.toRadians(144.0), Math.toRadians(180.0))
//            .build()
//
//        val pickupArtifact3Path = follower
//            .pathBuilder()
//            .addPath(
//                BezierLine(
//                    Pose(42.000, 36.000),
//                    Pose(16.000, 36.000)
//                )
//            )
//            .setConstantHeadingInterpolation(Math.toRadians(180.0))
//            .build()
//
//        val driveToGoal3Path = follower
//            .pathBuilder()
//            .addPath(
//                BezierCurve(
//                    Pose(16.000, 36.000),
//                    Pose(36.000, 60.000),
//                    Pose(25.000, 128.000)
//                )
//            )
//            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(144.0))
//            .build();
//
//        val cycle3 = ArtifactCycle(
//            follower,
//            driveToArtifact3Path,
//            pickupArtifact3Path,
//            driveToGoal3Path,
//            shooter,
//            intakeMotor,
//            transferMotor,
//            telemetryJ
//        )

        waitForStart()
        opModeTimer.reset()
        while (opModeIsActive()) {
            telemetryJ.update()
            follower.update()
            shooter.update()
            cycle1.update()
            cycle2.update()
//            cycle3.update()

            when (opModeState) {
                0 -> {
                    shooter.armed = true
                    telemetryJ.addData("shooter.tps", shooter.tps)
                    telemetryJ.addData("shooter.realTPS", shooter.realTPS)
                    if (shooter.spunUp) opModeState++
                }
                1 -> {
                    if (opModeTimer.time() < Subsystems.ArtifactCycle.shooterTime) {
                        intakeMotor.set(1.0)
                        transferMotor.set(1.0)
                    } else {
                        intakeMotor.set(0.0)
                        transferMotor.set(0.0)
                        shooter.armed = false
                        opModeState++
                    }
                }
                2 -> {
                    cycle1.state = 0
                    opModeState++
                }
                3 -> {
                    if (cycle1.finished) {
                        cycle2.state = 0
                        opModeState++
                    }
                }
                4 -> {
                    if (cycle2.finished) {
//                        cycle3.state = 0
                        opModeState++
                    }
                }
//                5 -> {
//                    if (cycle3.finished) {
//                        opModeState++
//                    }
//                }
                else -> { idle() }
            }
        }
    }
}