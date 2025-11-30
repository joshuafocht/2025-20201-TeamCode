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
import org.firstinspires.ftc.teamcode.subsystems.ArtifactCycle
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems


@Autonomous
class TestAuto2 : LinearOpMode() {
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
        while (opModeIsActive()) {
            telemetryJ.update()
            follower.update()
            shooter.update()
            cycle.update()
        }
    }
}