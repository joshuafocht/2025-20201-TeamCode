package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.seattlesolvers.solverslib.controller.PIDFController
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import kotlin.math.abs
import kotlin.math.round

class Align(val hardwareMap: HardwareMap) {
    val aprilTag: AprilTagProcessor? = AprilTagProcessor.Builder().build()
    val imu: IMU = hardwareMap.get(IMU::class.java, "imu")
    val pidf: PIDFController = PIDFController(
        Subsystems.Align.Kp,
        Subsystems.Align.Ki,
        Subsystems.Align.Kd,
        Subsystems.Align.Kf
    )
    var dist = 0.0
    var targetHeading = 0.0
    val currentHeading
        get() = imu.robotYawPitchRollAngles.yaw
    val tags
        get() = aprilTag?.detections?.size!!
    var enable = 0.0
        set(value) {
            field = if (value != 0.0) 1.0 else 0.0
        }

    init {
        val logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP
        val usbDirection = UsbFacingDirection.RIGHT
        val orientationOnRobot = RevHubOrientationOnRobot(logoDirection, usbDirection)
        imu.initialize(IMU.Parameters(orientationOnRobot))

        val builder: VisionPortal.Builder = VisionPortal.Builder()
        builder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
        builder.addProcessor(aprilTag)

        val visionPortal: VisionPortal = builder.build()
    }

    fun update() {
        pidf.setPIDF(
            Subsystems.Align.Kp,
            Subsystems.Align.Ki,
            Subsystems.Align.Kd,
            Subsystems.Align.Kf
        )
    }

    fun align(id: Int): Double {
        for (i in aprilTag?.detections?.indices!!) {
            val tag = aprilTag.detections[i]
            if (tag.id == id) {
                targetHeading = currentHeading + tag.ftcPose.bearing
                dist = tag.ftcPose.range
                break
            }
        }
        return pidf.calculate(
            currentHeading,
            targetHeading
        ) * enable
    }

    val tps
        get() = 6.15 * dist + 1100.0

    val aligned: Boolean
        get() = abs(targetHeading - currentHeading) < Subsystems.Align.tolerance && tags > 0
}