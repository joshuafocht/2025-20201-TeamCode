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
import org.firstinspires.ftc.teamcode.subsystems.Align
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.tuning.RGB
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs

@TeleOp
class DucksTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val telemetryM = PanelsTelemetry.telemetry
        val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        val leftRGB = hardwareMap.servo.get("leftRGB")
        val rightRGB = hardwareMap.servo.get("rightRGB")

        var leftColor = RGB.OFF
        var rightColor = RGB.OFF

        val follower = Constants.createFollower(hardwareMap)
        follower.activateAllPIDFs()
        follower.setStartingPose(Pose(72.000, 72.000, 90.000))
        follower.update()

        var moveMult = 1.0
        var turnMult = 1.0

        val shooterMotor = MotorEx(hardwareMap, "shooterMotor")
        val transferMotor = MotorEx(hardwareMap, "transferMotor")
        val intakeMotor = MotorEx(hardwareMap, "intakeMotor")

        shooterMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        transferMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        val shooter = Shooter(shooterMotor)

        val align = Align(hardwareMap)

        val intake = Intake(intakeMotor, transferMotor, shooter)

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
            intake.update(driverOp.getButton(GamepadKeys.Button.LEFT_BUMPER))
            align.update()

            follower.setTeleOpDrive(
                driverOp.leftY * moveMult,
                -driverOp.leftX * moveMult,
                (-driverOp.rightX * turnMult) + align.align(20)
            )

            if (driverOp.wasJustPressed(GamepadKeys.Button.DPAD_UP))
                moveMult += 0.05
            if (driverOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN))
                moveMult -= 0.05
            if (driverOp.wasJustPressed(GamepadKeys.Button.TRIANGLE))
                turnMult += 0.05
            if (driverOp.wasJustPressed(GamepadKeys.Button.CROSS))
                turnMult -= 0.05

            if (driverOp.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER))
                align.enable = 1.0
            else if (driverOp.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
                if (align.aligned) {
                    shooter.tps = align.tps
                    shooter.armed = true
                } else {
                    shooter.armed = false
                }
                if (shooter.spunUp) {
                    intakeMotor.set(Subsystems.Shooter.intakeSpeed)
                    transferMotor.set(Subsystems.Shooter.transferSpeed)
                } else {
                    intakeMotor.set(0.0)
                    transferMotor.set(0.0)
                }
            } else if (driverOp.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
                align.enable = 0.0
                shooter.armed = false
                intakeMotor.set(0.0)
                transferMotor.set(0.0)
            }

            if (align.tags > 0 && align.aligned)
                leftColor = RGB.GREEN
            else if (align.tags <= 0 && align.aligned)
                leftColor = RGB.PURPLE
            else if (align.tags > 0 && !align.aligned)
                leftColor = RGB.ORANGE
            else
                leftColor = RGB.RED

            if (shooter.spunUp)
                rightColor = RGB.GREEN
            else
                rightColor = RGB.RED

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

            leftRGB.position = leftColor.pos
            rightRGB.position = rightColor.pos

            telemetryJ.addData("shooter.armed", shooter.armed)
            telemetryJ.addData("shooter.spunUp", shooter.spunUp)
            telemetryJ.addData("shooter.tps", shooter.tps)
            telemetryJ.addData("shooter.realTPS", shooter.realTPS)
            telemetryJ.addData("shooter.realAccel", shooter.realAccel)
            telemetryJ.addData("shooter.realCurrent", shooter.realCurrent)
            telemetryJ.addData("align.targetHeading", align.targetHeading)
            telemetryJ.addData("align.currentHeading", align.currentHeading)
            telemetryJ.addData("align.aligned", align.aligned)
            telemetryJ.addData("align.tags", align.tags)
            telemetryJ.addData("align.dist", align.dist)
        }
    }
}