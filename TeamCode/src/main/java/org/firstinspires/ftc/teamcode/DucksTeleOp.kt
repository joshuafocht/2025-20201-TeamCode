package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.seattlesolvers.solverslib.drivebase.MecanumDrive
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs

@TeleOp
class DucksTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val telemetryM = PanelsTelemetry.telemetry

        val frontLeftMotor: MotorEx = MotorEx(hardwareMap, "frontLeftMotor")
        val frontRightMotor: MotorEx = MotorEx(hardwareMap, "frontRightMotor")
        val backLeftMotor: MotorEx = MotorEx(hardwareMap, "backLeftMotor")
        val backRightMotor: MotorEx = MotorEx(hardwareMap, "backRightMotor")

        frontLeftMotor.inverted = true;
        backRightMotor.inverted = true;

        frontLeftMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        frontRightMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backLeftMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        backRightMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        val drive: MecanumDrive = MecanumDrive(
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor
        )

        val shooter: Shooter = Shooter(MotorEx(hardwareMap, "shooterMotor"))
        val transferMotor: MotorEx = MotorEx(hardwareMap, "transferMotor")
        val intakeMotor: MotorEx = MotorEx(hardwareMap, "intakeMotor")

        transferMotor.inverted = true

        val driverOp = GamepadEx(gamepad1)
        val shooterOp = GamepadEx(gamepad2)

        waitForStart()
        while (opModeIsActive()) {
            driverOp.readButtons()
            shooterOp.readButtons()

            drive.driveRobotCentric(driverOp.leftX, driverOp.leftY, driverOp.rightX)

            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_UP))
                shooter.tps += Subsystems.Shooter.changeTPS
            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN))
                shooter.tps -= Subsystems.Shooter.changeTPS
            if (shooterOp.isDown(GamepadKeys.Button.DPAD_RIGHT)) {
                shooter.tps = abs(shooter.tps)
                shooter.armed = true
            } else if (shooterOp.isDown(GamepadKeys.Button.DPAD_LEFT)) {
                shooter.tps = -abs(shooter.tps)
                shooter.armed = true
            } else {
                shooter.armed = false
            }

            if (shooterOp.isDown(GamepadKeys.Button.CROSS))
                intakeMotor.set(1.0)
            else if (shooterOp.isDown(GamepadKeys.Button.SQUARE))
                intakeMotor.set(-1.0)
            else if (shooterOp.isDown(GamepadKeys.Button.CIRCLE))
                transferMotor.set(1.0)
            else if (shooterOp.isDown(GamepadKeys.Button.TRIANGLE))
                transferMotor.set(-1.0)

            telemetryM.addData("shooter: TPS", shooter.tps)
            telemetryM.addData("shooter: realTPS", shooter.realTPS)
            telemetryM.addData("shooter: realAccel", shooter.realAccel)
            telemetryM.addData("shooter: realCurrent", shooter.realCurrent)
        }
    }
}