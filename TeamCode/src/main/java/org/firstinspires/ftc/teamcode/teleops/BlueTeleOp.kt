package org.firstinspires.ftc.teamcode.teleops

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.subsystems.TeleOp
import org.firstinspires.ftc.teamcode.tuning.Subsystems.BlueTeleOp

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
class BlueTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val teleOp = TeleOp(hardwareMap, telemetry, gamepad1, gamepad2, { BlueTeleOp.tagId }, { BlueTeleOp.tagOffset })
        waitForStart()
        while (opModeIsActive()) {
            teleOp.loop()
        }
    }
}