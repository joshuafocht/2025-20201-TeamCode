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
    var autoState = 0
    var autoTimer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

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
                    Pose(84.000, 84.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(90.0))
            .build()

        val driveToGoalPath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(84.000, 84.000),
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
                    incState()
                }
                1 -> { // Wait for path to finish
                    if (!follower.followingPathChain) incState()
                }
                2 -> { // Start intakeMotor
                    intakeMotor.set(1.0)
                    incState()
                }
                3 -> { // Start pickupArtifactPath
                    follower.followPath(pickupArtifactPath)
                    incState()
                }
                4 -> { // Wait for path to finish
                    if (!follower.followingPathChain) incState()
                }
                5 -> { // Run transfer for 1 second to bring last ball in
                    if (autoTimer.time() < 500) {
                        transferMotor.set(0.6)
                    } else {
                        transferMotor.set(0.0)
                        incState()
                    }
                }
                6 -> { // Drive to "goal"
                    follower.followPath(driveToGoalPath)
                    incState()
                }
                7 -> { // Wait for path to finish
                    if (!follower.followingPathChain) incState()
                }
                8 -> { // Run transfer backwards to get ball out of shooter
                    if (autoTimer.time() < 750) {
                        intakeMotor.set(0.0)
                        transferMotor.set(-0.6)
                    } else {
                        incState()
                    }
                }
                9 -> { // Spin up shooter
                    shooter.armed = true
                    telemetryJ.addData("shooter.tps", shooter.tps)
                    telemetryJ.addData("shooter.realTPS", shooter.realTPS)
                    if (shooter.spunUp) incState()
                }
                10 -> { // Shoot the balls
                    if (autoTimer.time() < 3000) {
                        intakeMotor.set(1.0)
                        transferMotor.set(1.0)
                    } else {
                        intakeMotor.set(0.0)
                        transferMotor.set(0.0)
                        shooter.armed = false
                        incState()
                    }
                }
                11 -> { // Show that we are done
                    telemetryJ.clearAll()
                    telemetryJ.addLine("Done!")
                }
                else -> {idle()}
            }
        }
    }

    fun incState() {
        autoState++
        autoTimer.reset()
    }
}