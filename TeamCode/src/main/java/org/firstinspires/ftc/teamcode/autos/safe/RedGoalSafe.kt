package org.firstinspires.ftc.teamcode.autos.safe

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
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems

@Autonomous
class RedGoalSafe : LinearOpMode() {
    override fun runOpMode() {
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
        var state = 0
        val timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

        val shooter = Shooter(MotorEx(hardwareMap, "shooterMotor"))
        shooter.tps = Subsystems.BlueFarAuto.tps

        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        transferMotor.inverted = true
        val intake = Intake(intakeMotor, transferMotor, shooter)

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(112.0, 135.0, Math.toRadians(-90.0)))

        val driveToGoal0Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(112.000, 135.000),
                    Pose(96.000, 96.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-90.0), Math.toRadians(Subsystems.BlueFarAuto.angle))
            .build();

        val driveToArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(96.000, 96.000),
                    Pose(96.000, 84.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(Subsystems.BlueFarAuto.angle), Math.toRadians(0.0))
            .build();

        val pickupArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(96.000, 84.000),
                    Pose(128.000, 84.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(0.0))
            .build();

        val leavePath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(128.000, 84.000),
                    Pose(18.000, 100.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(-90.0))
            .build()

        waitForStart()
        timer.reset()
        while (opModeIsActive()) {
            telemetryJ.update()
            follower.update()
            shooter.update()
            intake.update()

            when (state) {
                0 -> {
                    if (timer.time() > Subsystems.AutoDelay.safeDelay) state++
                }
                1 -> {
                    follower.followPath(driveToGoal0Path)
                    shooter.enabled = true
                    state++
                }
                2 -> {
                    if (!follower.followingPathChain && shooter.spunUp) state++
                }
                3 -> {
                    intakeMotor.set(Subsystems.Shooter.intakeSpeed)
                    transferMotor.set(Subsystems.Shooter.transferSpeed)
                    state++
                    timer.reset()
                }
                4 -> {
                    if (timer.time() > Subsystems.Shooter.shootTime) {
                        shooter.enabled = false
                        intakeMotor.set(0.0)
                        transferMotor.set(0.0)
                        state++
                    }
                }
                5 -> {
                    follower.followPath(driveToArtifact1Path)
                    state++
                }
                6 -> {
                    if (!follower.followingPathChain) state++
                }
                7 -> {
                    intake.enabled = true
                    follower.followPath(pickupArtifact1Path, 0.5, true)
                    state++
                }
                8 -> {
                    if (!follower.followingPathChain) state++
                }
                9 -> {
                    follower.followPath(leavePath)
                    state++
                }
                10 -> {
                    if (!follower.followingPathChain) {
                        intake.enabled = false
                        state++
                    }
                }
                else -> { break }
            }
        }
    }
}
