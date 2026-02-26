package org.firstinspires.ftc.teamcode.subsystems

import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs

class Shooter(val shooterLMotor: MotorEx, val shooterRMotor: MotorEx) {
    init {
        shooterRMotor.setRunMode(Motor.RunMode.VelocityControl)
        shooterLMotor.setRunMode(Motor.RunMode.VelocityControl)
        update()
    }

    fun update() {
        shooterRMotor.setVeloCoefficients(
            Subsystems.Shooter.Kp,
            Subsystems.Shooter.Ki,
            Subsystems.Shooter.Kd
        )
        shooterRMotor.setFeedforwardCoefficients(
            Subsystems.Shooter.Ks,
            Subsystems.Shooter.Kv,
            Subsystems.Shooter.Ka
        )
        shooterLMotor.setVeloCoefficients(
            Subsystems.Shooter.Kp,
            Subsystems.Shooter.Ki,
            Subsystems.Shooter.Kd
        )
        shooterLMotor.setFeedforwardCoefficients(
            Subsystems.Shooter.Ks,
            Subsystems.Shooter.Kv,
            Subsystems.Shooter.Ka
        )
        if (enabled) {
            shooterRMotor.velocity = tps
            shooterLMotor.velocity = tps
        }
        else {
            shooterRMotor.velocity = 0.0
            shooterLMotor.velocity = 0.0
        }
    }

    var tps: Double = Subsystems.Shooter.targetTPS
//        set(value) {
//            field = value
//            shooterRMotor.velocity = if (enabled) tps else 0.0
//        }

    var enabled: Boolean = false
//        set(value) {
//            field = value
//            shooterRMotor.velocity = if (enabled) tps else 0.0
//        }


    val spunUp: Boolean
        get() = abs(tps) - abs(shooterRMotor.velocity) < Subsystems.Shooter.tolerance

    val realTPS: Double
        get() = shooterRMotor.velocity

    val realAccel: Double
        get() = shooterRMotor.acceleration

    val realCurrent: Double
        get() = shooterRMotor.getCurrent(CurrentUnit.MILLIAMPS)
}