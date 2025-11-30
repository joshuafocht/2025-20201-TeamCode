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
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems


@Autonomous
class BlueGoalAuto : LinearOpMode() {
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

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        // First 3 Artifacts Pickup Paths
        //
        ///////////////////////////////////////////////////////////////////////////////////////////

        val driveToArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(24.000, 128.000),
                    Pose(56.000, 96.000),
                    Pose(42.000, 84.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(144.0), Math.toRadians(180.0))
            .build()

        val pickupArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(42.000, 84.000),
                    Pose(15.000, 84.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build()

        val driveToGoal1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(15.000, 84.000),
                    Pose(24.000, 128.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(144.0))
            .build();

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        // Second 3 Artifacts Pickup Paths
        //
        ///////////////////////////////////////////////////////////////////////////////////////////

        val driveToArtifact2Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(24.000, 128.000),
                    Pose(72.000, 96.000),
                    Pose(42.000, 60.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(144.0), Math.toRadians(180.0))
            .build()

        val pickupArtifact2Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(42.000, 60.000),
                    Pose(16.000, 60.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build()

        val driveToGoal2Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(16.000, 60.000),
                    Pose(48.000, 96.000),
                    Pose(24.000, 128.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(144.0))
            .build();

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        // Third 3 Artifacts Pickup Paths
        //
        ///////////////////////////////////////////////////////////////////////////////////////////

        val driveToArtifact3Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(24.000, 128.000),
                    Pose(72.000, 72.000),
                    Pose(42.000, 36.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(144.0), Math.toRadians(180.0))
            .build()

        val pickupArtifact3Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(42.000, 36.000),
                    Pose(9.280, 36.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build()

        val driveToGoal3Path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(9.280, 36.000),
                    Pose(48.000, 72.000),
                    Pose(24.000, 128.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(144.0))
            .build();

        waitForStart()
        while (opModeIsActive()) {
            telemetryJ.update()
            follower.update()
            shooter.update()

            when (autoState) {
                0 -> { // Start driveToArtifactPath
                    follower.followPath(driveToArtifact1Path)
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
                    follower.followPath(pickupArtifact1Path)
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
                    follower.followPath(driveToGoal1Path)
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
                10 -> {
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
                11 -> {
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