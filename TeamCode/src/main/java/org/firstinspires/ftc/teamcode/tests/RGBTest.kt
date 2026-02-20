package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
@Disabled
class RGBTest : LinearOpMode() {
    override fun runOpMode() {
        val leftRGB = hardwareMap.servo.get("leftRGB")
        val rightRGB = hardwareMap.servo.get("rightRGB")

        waitForStart()
        while (opModeIsActive()) {
            if (gamepad1.dpadUpWasPressed()) leftRGB.position += 0.005
            if (gamepad1.dpadDownWasPressed()) leftRGB.position -= 0.005
            telemetry.addData("l", leftRGB.position)
            telemetry.update()
        }
    }
}