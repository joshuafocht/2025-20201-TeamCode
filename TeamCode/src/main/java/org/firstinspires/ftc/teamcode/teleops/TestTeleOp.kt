package org.firstinspires.ftc.teamcode.teleops

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@TeleOp
class TestTeleOp : NextFTCOpMode() {
    init {
        addComponents(
            PedroComponent(Constants::createFollower)
        )
    }

    override fun onStartButtonPressed() {
        val driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            Gamepads.gamepad1.rightStickX
        )
        driverControlled.update()
        driverControlled()
    }
}