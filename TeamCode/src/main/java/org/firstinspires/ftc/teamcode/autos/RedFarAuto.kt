package org.firstinspires.ftc.teamcode.autos

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.subsystems.ArtifactCycle
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems


@Autonomous
class RedFarAuto : LinearOpMode() {
    override fun runOpMode() {
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
        var opModeState = 0
        val opModeTimer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

        val shooterLMotor = MotorEx(hardwareMap, "shooterLMotor")
        val shooterRMotor = MotorEx(hardwareMap, "shooterRMotor")
        shooterLMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        shooterRMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        shooterLMotor.inverted = true
        val shooter = Shooter(shooterLMotor, shooterRMotor)
        shooter.tps = Subsystems.RedFarAuto.tps

        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        transferMotor.inverted = true
        val intake = Intake(intakeMotor, transferMotor, shooter)

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(88.000, 9.000, Math.toRadians(90.0)))

        val driveToGoal0Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(88.000, 9.000),
                    Pose(84.000, 16.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(90.0), Math.toRadians(Subsystems.RedFarAuto.angle))
            .build();

        val driveToArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(84.000, 16.000),
                    Pose(96.000, 36.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(Subsystems.RedFarAuto.angle), Math.toRadians(0.0))
            .build();

        val pickupArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(96.000, 36.000),
                    Pose(138.000, 36.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(0.0))
            .build();

        val driveToGoal1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(138.000, 36.000),
                    Pose(84.000, 16.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(Subsystems.RedFarAuto.angle))
            .build()

        val driveToArtifact2Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(84.000, 16.000),
                    Pose(96.000, 60.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(Subsystems.RedFarAuto.angle), Math.toRadians(0.0))
            .build()

        val pickupArtifact2Path = follower.pathBuilder().addPath(
            BezierLine(
                Pose(96.000, 60.000),

                Pose(138.000, 60.000)
            )
        )
            .setConstantHeadingInterpolation(Math.toRadians(0.0))
            .build()

        val driveToGoal2Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(138.000, 60.000),
                    Pose(84.000, 16.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(Subsystems.RedFarAuto.angle))
            .build()

        val driveToArtifact3Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(84.000, 16.000),
                    Pose(96.000, 84.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(Subsystems.RedFarAuto.angle), Math.toRadians(0.0))
            .build()

        val pickupArtifact3Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(96.000, 84.000),
                    Pose(130.000, 84.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(0.0))
            .build()

        val leavePath = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(130.000, 84.000),
                    Pose(96.000, 96.000),
                    Pose(124.000, 72.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(180.0))
            .build()

        val cycle1 = ArtifactCycle(
            follower,
            driveToArtifact1Path,
            pickupArtifact1Path,
            driveToGoal1Path,
            Subsystems.RedFarAuto.angle,
            shooter,
            intake,
            telemetryJ
        )

        val cycle2 = ArtifactCycle(
            follower,
            driveToArtifact2Path,
            pickupArtifact2Path,
            driveToGoal2Path,
            Subsystems.RedFarAuto.tps,
            shooter,
            intake,
            telemetryJ
        )

        waitForStart()
        opModeTimer.reset()
        while (opModeIsActive()) {
            telemetryJ.update()
            follower.update()
            shooter.update()
            intake.update()
            cycle1.update()
            cycle2.update()

            telemetryJ.addData("opModeState", opModeState)
            telemetryJ.addData("folllower.followingPathChain", follower.followingPathChain)

            when (opModeState) {
                0 -> {
                    follower.followPath(driveToGoal0Path)
                    shooter.enabled = true
                    opModeState++
                }
                1 -> {
                    if (!follower.followingPathChain && shooter.spunUp) opModeState++
                }
                2 -> {
                    intakeMotor.set(Subsystems.Shooter.intakeSpeed)
                    transferMotor.set(Subsystems.Shooter.transferSpeed)
                    opModeState++
                    opModeTimer.reset()
                }
                3 -> {
                    if (opModeTimer.time() > Subsystems.Shooter.shootTime) {
                        shooter.enabled = false
                        intakeMotor.set(0.0)
                        transferMotor.set(0.0)
                        opModeState++
                    }
                }
                4 -> {
                    cycle1.enabled = true
                    opModeState++
                }
                5 -> {
                    if (cycle1.finished) opModeState++
                }
                6 -> {
                    cycle2.enabled = true
                    opModeState++
                }
                7 -> {
                    if (cycle2.finished) opModeState++
                }
                8 -> {
                    follower.followPath(driveToArtifact3Path)
                    opModeState++
                }
                9 -> {
                    if (!follower.followingPathChain) opModeState++
                }
                10 -> {
                    intake.enabled = true
                    follower.followPath(pickupArtifact3Path, 0.5, true)
                    opModeState++
                }
                11 -> {
                    if (!follower.followingPathChain) opModeState++
                }
                12 -> {
                    follower.followPath(leavePath)
                    opModeState++
                }
                13 -> {
                    if (!follower.followingPathChain) {
                        intake.enabled = false
                        opModeState++
                    }
                }
                else -> { break }
            }
        }
    }
}
