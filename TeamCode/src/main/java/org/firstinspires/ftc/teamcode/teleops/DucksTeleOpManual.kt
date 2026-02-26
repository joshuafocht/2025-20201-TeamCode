package org.firstinspires.ftc.teamcode.teleops

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
import org.firstinspires.ftc.teamcode.subsystems.Align
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs

@TeleOp
class DucksTeleOpManual : LinearOpMode() {
    override fun runOpMode() {
        val telemetryM = PanelsTelemetry.telemetry
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        val follower = Constants.createFollower(hardwareMap)
        follower.activateAllPIDFs()
        follower.setStartingPose(Pose(72.000, 72.000, 90.000))
        follower.update()

        var moveMult = 1.0
        var turnMult = 1.0

        val shooterLMotor = MotorEx(hardwareMap, "shooterLMotor")
        val shooterRMotor = MotorEx(hardwareMap, "shooterRMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")

        shooterLMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        shooterRMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        transferMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        shooterLMotor.inverted = true
        shooterRMotor.encoder = shooterLMotor.encoder

        val shooter = Shooter(shooterLMotor, shooterRMotor)

        val align = Align(hardwareMap, follower, shooter, intakeMotor, transferMotor, 20, true)

        transferMotor.inverted = true

        val driverOp = GamepadEx(gamepad1)
        val shooterOp = GamepadEx(gamepad2)

        waitForStart()
        follower.startTeleOpDrive()
        while (!isStopRequested) {
            telemetryM.update()
            telemetryJ.update()
            follower.update()

            driverOp.readButtons()
            shooterOp.readButtons()

            shooter.update()

            follower.setTeleOpDrive(
                driverOp.leftY * moveMult,
                -driverOp.leftX * moveMult,
                (-driverOp.rightX * turnMult) + align.power
            )

            if (driverOp.isDown(GamepadKeys.Button.RIGHT_BUMPER))
                align.enabled = true
            else
                align.enabled = true

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
                shooter.enabled = !shooter.enabled
            }

            if (shooterOp.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                shooter.tps = -abs(shooter.tps)
                shooter.enabled = true
            } else if (shooterOp.wasJustReleased(GamepadKeys.Button.DPAD_LEFT)) {
                shooter.tps = abs(shooter.tps)
                shooter.enabled = false
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
                shooter.enabled = true
            } else if (shooterOp.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
                if (shooter.spunUp) {
                    intakeMotor.set(1.0)
                    transferMotor.set(1.0)
                }
            } else if (shooterOp.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
                shooter.enabled = false
                intakeMotor.set(0.0)
                transferMotor.set(0.0)
            }

            telemetryJ.addData("shooter.enabled", shooter.enabled)
            telemetryJ.addData("shooter.spunUp", shooter.spunUp)
            telemetryJ.addData("shooter.tps", shooter.tps)
            telemetryJ.addData("shooter.realTPS", shooter.realTPS)
            telemetryJ.addData("shooter.realAccel", shooter.realAccel)
            telemetryJ.addData("shooter.realCurrent", shooter.realCurrent)
            telemetryJ.addData("align.dist", align.dist)
        }
    }
}