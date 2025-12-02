package org.firstinspires.ftc.teamcode.autos

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems


@Autonomous
class TestAuto : LinearOpMode() {
    var autoTimer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    var autoState = 0
        set(value) {
            field = value
            autoTimer.reset()
        }

    override fun runOpMode() {
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        transferMotor.inverted = true

        val shooter = Shooter(MotorEx(hardwareMap, "shooterMotor"))
        shooter.tps = Subsystems.Shooter.targetTPS

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(63.5, 56.5, Math.toRadians(90.0)))

        val driveToArtifactPath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(63.5, 56.5),
                    Pose(84.0, 56.5)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(90.0))
            .build()

        val pickupArtifactPath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(84.000, 56.500),
                    Pose(84.000, 97.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(90.0))
            .build()

        val driveToGoalPath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(84.000, 87.000),
                    Pose(60.000, 84.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(90.0))
            .build();

        waitForStart()
        while (opModeIsActive()) {
            telemetryJ.update()
            follower.update()
            shooter.update()

            when (autoState) {
                0 -> { // Start driveToArtifactPath
                    follower.followPath(driveToArtifactPath)
                    autoState++
                }
                1 -> { // Wait for path to finish
                    if (!follower.followingPathChain) autoState++
                }
                2 -> { // Start intakeMotor and transferMotor
                    intakeMotor.set(1.0)
                    transferMotor.set(1.0)
                    autoState++
                }
                3 -> { // Start pickupArtifactPath
                    follower.followPath(pickupArtifactPath)
                    autoState++
                }
                4 -> { // Wait for path to finish and stop transfer halfway through
                    if (!follower.followingPathChain) autoState++
                    if (autoTimer.time() < 750) transferMotor.set(0.0)
                }
                5 -> { // Let intake run for some extra time at the end to bring the last ball in
                    if (autoTimer.time() < 750) {
                        intakeMotor.set(0.0)
                        autoState++
                    }
                }
                6 -> { // Drive to goal
                    follower.followPath(driveToGoalPath)
                    autoState++
                }
                7 -> { // Wait for path to finish
                    if (!follower.followingPathChain) autoState++
                }
                8 -> { // Run transfer backwards to get ball out of shooter
                    if (autoTimer.time() < 750) {
                        intakeMotor.set(0.0)
                        transferMotor.set(-0.6)
                    } else {
                        autoState++
                    }
                }
                9 -> { // Spin up shooter
                    shooter.armed = true
                    telemetryJ.addData("shooter.tps", shooter.tps)
                    telemetryJ.addData("shooter.realTPS", shooter.realTPS)
                    if (shooter.spunUp) autoState++
                }
                10 -> { // Shoot the balls
                    if (autoTimer.time() < 3000) {
                        intakeMotor.set(1.0)
                        transferMotor.set(1.0)
                    } else {
                        intakeMotor.set(0.0)
                        transferMotor.set(0.0)
                        shooter.armed = false
                        autoState++
                    }
                }
                11 -> { // Show that we are done
                    telemetryJ.clearAll()
                    telemetryJ.addLine("Done!")
                    autoState++
                }
                else -> { idle() }
            }
        }
    }
}