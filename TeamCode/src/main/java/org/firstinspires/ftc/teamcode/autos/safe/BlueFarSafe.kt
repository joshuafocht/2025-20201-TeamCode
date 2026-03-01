package org.firstinspires.ftc.teamcode.autos.safe

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
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems


@Autonomous
class BlueFarSafe : LinearOpMode() {
    override fun runOpMode() {
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
        var state = 0
        val timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

        val shooterLMotor = MotorEx(hardwareMap, "shooterLMotor")
        val shooterRMotor = MotorEx(hardwareMap, "shooterRMotor")
        shooterLMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        shooterRMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        shooterLMotor.inverted = true
        val shooter = Shooter(shooterLMotor, shooterRMotor)
        shooter.tps = Subsystems.BlueFarAuto.tps

        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        transferMotor.inverted = true
        val intake = Intake(intakeMotor, transferMotor, shooter)

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(56.000, 9.000, Math.toRadians(90.0)))

        val driveToGoal0Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(56.000, 9.000),
                    Pose(60.000, 16.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(90.0), Math.toRadians(Subsystems.BlueFarAuto.angle))
            .build();

        val driveToArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(60.000, 16.000),
                    Pose(48.000, 36.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(Subsystems.BlueFarAuto.angle), Math.toRadians(180.0))
            .build();

        val pickupArtifact1Path = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(48.000, 36.000),
                    Pose(6.000, 36.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build();

        val leavePath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(6.000, 36.000),
                    Pose(36.000, 9.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(90.0))
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
