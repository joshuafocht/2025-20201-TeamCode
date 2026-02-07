package org.firstinspires.ftc.teamcode.tests

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.Pose
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.IMU
import com.seattlesolvers.solverslib.controller.PIDFController
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor

@Autonomous
class AlignToBlue : LinearOpMode() {
    override fun runOpMode() {
        val telemetryM = PanelsTelemetry.telemetry
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        val imu: IMU = hardwareMap.get(IMU::class.java, "imu")
        val logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP
        val usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
        val orientationOnRobot = RevHubOrientationOnRobot(logoDirection, usbDirection)
        imu.initialize(IMU.Parameters(orientationOnRobot))
        var targetHeading = 0.0;

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(72.000, 72.000, 90.000))
        follower.update()

        val aprilTag: AprilTagProcessor? = AprilTagProcessor.Builder().build()

        val builder: VisionPortal.Builder = VisionPortal.Builder()
        builder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
        builder.addProcessor(aprilTag)

        val visionPortal: VisionPortal = builder.build()

        val pidf: PIDFController = PIDFController(
            Subsystems.Align.Kp,
            Subsystems.Align.Ki,
            Subsystems.Align.Kd,
            Subsystems.Align.Kf
        )

        waitForStart()
        follower.startTeleOpDrive()
        while (opModeIsActive()) {
            telemetryJ.update()
            telemetryM.update()
            follower.update()

            pidf.setPIDF(
                Subsystems.Align.Kp,
                Subsystems.Align.Ki,
                Subsystems.Align.Kd,
                Subsystems.Align.Kf
            )

            val orientation = imu.robotYawPitchRollAngles

            var enabled = 0.0
            for (i in aprilTag?.detections?.indices!!) {
                val tag = aprilTag.detections[i]
                if (tag.id == 20) {
                    enabled = 1.0
                    targetHeading = orientation.yaw + tag.ftcPose.bearing
                    telemetryJ.addData("dist", tag.ftcPose.range)
                    break
                }
            }

            follower.setTeleOpDrive(
                0.0,
                0.0,
                pidf.calculate(
                    orientation.yaw,
                    targetHeading
                ) * enabled
            )

            telemetryJ.addData("heading", orientation.yaw)
            telemetryJ.addData("target", targetHeading)
            telemetryJ.addData("tags", aprilTag.detections?.size)
        }
    }
}