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

class Align(val hardwareMap: HardwareMap, val id: Int) {
    var aprilTag: AprilTagProcessor
    var visionPortal: VisionPortal

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
    var tags = 0
    var offset: Double = 0.0
    var power: Double = 0.0
    var enabled: Boolean = false

    val tps
        get() = Subsystems.Align.m * dist + Subsystems.Align.b

    val aligned: Boolean
        get() = abs(targetHeading - currentHeading) < Subsystems.Align.tolerance && tags > 0

    init {
        val logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP
        val usbDirection = UsbFacingDirection.RIGHT
        val orientationOnRobot = RevHubOrientationOnRobot(logoDirection, usbDirection)
        imu.initialize(IMU.Parameters(orientationOnRobot))

        aprilTag = AprilTagProcessor.Builder().build()

        val builder: VisionPortal.Builder = VisionPortal.Builder()
        builder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
        builder.addProcessor(aprilTag)

        visionPortal = builder.build()
    }

    fun update() {
        pidf.setPIDF(
            Subsystems.Align.Kp,
            Subsystems.Align.Ki,
            Subsystems.Align.Kd,
            Subsystems.Align.Kf
        )
        if (enabled) {
            for (i in aprilTag.detections?.indices!!) {
                val tag = aprilTag.detections[i]
                tags = 0
                if (tag.id == id) {
                    targetHeading = currentHeading - tag.ftcPose.bearing + offset // add if camera upright subtract if upside down
                    dist = tag.ftcPose.range
                    tags++
                    break
                }
            }
            power = pidf.calculate(currentHeading, targetHeading)
        } else {
            power = 0.0
        }
    }
}