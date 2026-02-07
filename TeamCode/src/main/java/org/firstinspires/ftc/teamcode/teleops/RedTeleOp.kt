package org.firstinspires.ftc.teamcode.teleops

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.subsystems.TeleOp
import org.firstinspires.ftc.teamcode.tuning.Subsystems.RedTeleOp

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
class RedTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val teleOp = TeleOp(hardwareMap, telemetry, gamepad1, gamepad2, { RedTeleOp.tagId }, { RedTeleOp.tagOffset })
        waitForStart()
        while (opModeIsActive()) {
            teleOp.loop()
        }
    }
}