package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.seattlesolvers.solverslib.drivebase.MecanumDrive
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs
import kotlin.math.truncate

@TeleOp
class DucksTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val telemetryM = PanelsTelemetry.telemetry
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        val frontLeftMotor = MotorEx(hardwareMap, "frontLeftMotor")
        val frontRightMotor = MotorEx(hardwareMap, "frontRightMotor")
        val backLeftMotor = MotorEx(hardwareMap, "backLeftMotor")
        val backRightMotor = MotorEx(hardwareMap, "backRightMotor")

        frontLeftMotor.inverted = true
        backLeftMotor.inverted = true

        val drive = MecanumDrive(
            false,
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor
        )

        var moveMult = 1.0
        var turnMult = 1.0

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

        while (!isStopRequested) {
            telemetryM.update()
            telemetryJ.update()

            driverOp.readButtons()
            shooterOp.readButtons()

            shooter.update()

            drive.driveRobotCentric(
                -driverOp.leftX * moveMult,
                driverOp.leftY * moveMult,
                -driverOp.rightX * turnMult
            )

            if (driverOp.wasJustPressed(GamepadKeys.Button.DPAD_UP))
                moveMult += 0.05
            if (driverOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN))
                moveMult -= 0.05
            if (driverOp.wasJustPressed(GamepadKeys.Button.TRIANGLE))
                turnMult += 0.05
            if (driverOp.wasJustPressed(GamepadKeys.Button.CROSS))
                turnMult -= 0.05

            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_UP))
                shooter.tps += Subsystems.Shooter.changeTPS
            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN))
                shooter.tps -= Subsystems.Shooter.changeTPS

            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                shooter.tps = abs(shooter.tps)
                shooter.armed = !shooter.armed
            }

            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                shooter.tps = -abs(shooter.tps)
                shooter.armed = true
            } else if (shooterOp.wasJustReleased(GamepadKeys.Button.DPAD_LEFT)) {
                shooter.tps = abs(shooter.tps)
                shooter.armed = false
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

            telemetryJ.addData("shooter.armed", shooter.armed)
            telemetryJ.addData("shooter.spunUp", shooter.spunUp)
            telemetryJ.addData("shooter.tps", shooter.tps)
            telemetryJ.addData("shooter.realTPS", shooter.realTPS)
            telemetryJ.addData("shooter.realAccel", shooter.realAccel)
            telemetryJ.addData("shooter.realCurrent", shooter.realCurrent)

        }
    }
}