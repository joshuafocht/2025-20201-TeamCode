package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs

@TeleOp
class DucksTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val telemetryM = PanelsTelemetry.telemetry
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(72.0, 72.0))
        follower.update()

        val shooterMotor = MotorEx(hardwareMap, "shooterMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")

        shooterMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        transferMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        val shooter = Shooter(shooterMotor)

        transferMotor.inverted = true

        val driverOp = GamepadEx(gamepad1)
        val shooterOp = GamepadEx(gamepad2)

        waitForStart()

        follower.startTeleOpDrive()

        while (!isStopRequested) {
            follower.update()
            telemetryM.update()
            telemetryJ.update()

            driverOp.readButtons()
            shooterOp.readButtons()

            shooter.update()

            follower.setTeleOpDrive(
                driverOp.leftY,
                driverOp.leftX,
                driverOp.rightX,
                true
            )

            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_UP))
                shooter.tps += Subsystems.Shooter.changeTPS
            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN))
                shooter.tps -= Subsystems.Shooter.changeTPS
            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                shooter.armed = !shooter.armed
            }
            if (shooterOp.isDown(GamepadKeys.Button.DPAD_LEFT)) {
                shooter.tps = -abs(shooter.tps)
            } else {
                shooter.tps = abs(shooter.tps)

            }

            if (shooterOp.isDown(GamepadKeys.Button.CROSS))
                intakeMotor.set(1.0)
            else if (shooterOp.isDown(GamepadKeys.Button.SQUARE))
                intakeMotor.set(-1.0)
            else
                intakeMotor.set(0.0)
            if (shooterOp.isDown(GamepadKeys.Button.CIRCLE))
                transferMotor.set(1.0)
            else if (shooterOp.isDown(GamepadKeys.Button.TRIANGLE))
                transferMotor.set(-1.0)
            else
                transferMotor.set(0.0)

            if (shooterOp.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
                shooter.armed = true
            } else if (shooterOp.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
                if (shooter.spunUp) {
                    intakeMotor.set(1.0)
                    transferMotor.set(1.0)
                }
            } else if (shooterOp.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
                shooter.armed = false
                intakeMotor.set(0.0)
                transferMotor.set(0.0)
            }

            telemetryJ.addData("position.x", follower.pose.x)
            telemetryJ.addData("position.y", follower.pose.y)
            telemetryJ.addData("position.heading", follower.pose.heading)

            telemetryJ.addData("shooter.armed", shooter.armed)
            telemetryJ.addData("shooter.spunUp", shooter.spunUp)
            telemetryJ.addData("shooter.tps", shooter.tps)
            telemetryJ.addData("shooter.realTPS", shooter.realTPS)
            telemetryJ.addData("shooter.realAccel", shooter.realAccel)
            telemetryJ.addData("shooter.realCurrent", shooter.realCurrent)

        }
    }
}