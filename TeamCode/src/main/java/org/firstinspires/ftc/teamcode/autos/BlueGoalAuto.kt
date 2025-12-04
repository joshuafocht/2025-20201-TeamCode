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
        val opModeTimer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        transferMotor.inverted = true

        val shooter = Shooter(MotorEx(hardwareMap, "shooterMotor"))
        shooter.tps = Subsystems.Shooter.targetTPS

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(25.0, 128.0, Math.toRadians(144.0)))

        val driveToArtifactPath = follower
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

        val pickupArtifactPath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(48.000, 84.000),
                    Pose(14.000, 84.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build()

        val driveToGoalPath = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(14.000, 84.000),
                    Pose(48.000, 108.000),
                    Pose(20.000, 128.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(144.0))
            .build()

        val driveOutPath = follower
            .pathBuilder()
            .addPath(
                BezierLine(Pose(25.000, 128.000), Pose(48.000, 72.000))
            )
            .setLinearHeadingInterpolation(Math.toRadians(144.0), Math.toRadians(180.0))
            .build()

        val cycle = ArtifactCycle(
            follower,
            driveToArtifactPath,
            pickupArtifactPath,
            driveToGoalPath,
            shooter,
            intakeMotor,
            transferMotor,
            telemetryJ
        )

        waitForStart()
        opModeTimer.reset()
        while (opModeIsActive()) {
            telemetryJ.update()
            follower.update()
            shooter.update()
            cycle.update()

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
                    cycle.state = 0
                    opModeState++
                }
                3 -> {
                    if (cycle.finished) {
                        opModeState++
                    }
                }
                4 -> {
                    follower.followPath(driveOutPath)
                    opModeState++
                }
                5 -> {
                    if (!follower.followingPathChain) opModeState++
                }
                else -> { break }
            }
        }
    }
}