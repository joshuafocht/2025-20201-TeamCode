package org.firstinspires.ftc.teamcode.subsystems

import android.net.wifi.aware.ParcelablePeerHandle
import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import com.seattlesolvers.solverslib.hardware.servos.ServoEx
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.tuning.RGB
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs

class TeleOp(val hardwareMap: HardwareMap, val telemetry: Telemetry, gamepad1: Gamepad, gamepad2: Gamepad, val tagId: Int, val closeTagOffset: () -> Double, val farTagOffset: () -> Double, val farDist: () -> Double, var idle: Boolean) {
    val telemetryM = PanelsTelemetry.telemetry
    val telemetryJ = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

    val leftRGB = hardwareMap.servo.get("leftRGB")
    val rightRGB = hardwareMap.servo.get("rightRGB")

    var leftColor = RGB.OFF
    var rightColor = RGB.OFF

    val follower = Constants.createFollower(hardwareMap)
    var moveMult = 1.0
    var turnMult = 1.0

    val shooterMotor = MotorEx(hardwareMap, "shooterMotor")
    val transferMotor = MotorEx(hardwareMap, "transferMotor")
    val intakeMotor = MotorEx(hardwareMap, "intakeMotor")
    val antiJamServo = ServoEx(hardwareMap, "antiJamServo")

    val shooter = Shooter(shooterMotor)
    val align = Align(hardwareMap, follower, shooter, intakeMotor, transferMotor,tagId, true)
    val intake = Intake(intakeMotor, transferMotor, shooter, antiJamServo, idle)

    val driverOp = GamepadEx(gamepad1)
    var auto = false
    var timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    var resetema = false

    init {
        follower.setStartingPose(Pose(72.000, 72.000, 90.000))
        follower.update()

        shooterMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        transferMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        shooter.tps = Subsystems.Shooter.idleSpeed

        transferMotor.inverted = true
    }

    fun start() {
        follower.startTeleOpDrive(true)
        follower.update()
    }

    fun loop() {
        telemetryM.update()
        telemetryJ.update()
        follower.update()

        driverOp.readButtons()

        shooter.update()
        intake.update()
        align.update()


        shooter.enabled = true

        if (!auto) follower.setTeleOpDrive(
            driverOp.leftY * moveMult,
            -driverOp.leftX * moveMult,
            (-driverOp.rightX * turnMult) + align.power
        )

        if (driverOp.wasJustPressed(GamepadKeys.Button.DPAD_UP))
            moveMult += 0.05
        if (driverOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN))
            moveMult -= 0.05
        if (driverOp.wasJustPressed(GamepadKeys.Button.TRIANGLE))
            turnMult += 0.05
        if (driverOp.wasJustPressed(GamepadKeys.Button.CROSS))
            turnMult -= 0.05

        intake.enabled = driverOp.isDown(GamepadKeys.Button.LEFT_BUMPER)

        if (intake.finished && idle) shooter.tps = Subsystems.Shooter.preSpeed

        if (driverOp.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
            intake.finished = false
            align.enabled = true
            resetema = false
            timer.reset()
        } else if (driverOp.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
            if (!resetema && align.tags > 0) {
                align.ema.ema = align.ema.new
                resetema = true
            }
            if (align.dist > farDist()) {
                align.offset = farTagOffset()
            } else {
                align.offset = closeTagOffset()
            }
            antiJamServo.set(Subsystems.AntiJam.shootPos)
            shooter.tps = align.stps
            if (shooter.spunUp && align.aligned && (timer.time() > Subsystems.AntiJam.moveTime)) {
                intakeMotor.set(Subsystems.Shooter.intakeSpeed)
                transferMotor.set(Subsystems.Shooter.transferSpeed)
            } else {
                intakeMotor.set(0.0)
                transferMotor.set(0.0)
            }
        } else if (driverOp.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
            align.enabled = false
            if (idle) shooter.tps = Subsystems.Shooter.idleSpeed
            else shooter.enabled = false
            antiJamServo.set(Subsystems.AntiJam.blockPos)
            intakeMotor.set(0.0)
            transferMotor.set(0.0)
        }

        if (shooter.realTPS < Subsystems.Shooter.clearSpeed) {
            antiJamServo.set(Subsystems.AntiJam.blockPos)
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

        leftRGB.position = leftColor.pos
        rightRGB.position = rightColor.pos

        telemetryJ.addData("shooter.enabled", shooter.enabled)
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
        telemetryJ.addData("align.sdist", align.sdist)
        telemetryJ.addData("align.offset", align.offset)
    }
}