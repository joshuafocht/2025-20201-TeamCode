package org.firstinspires.ftc.teamcode.subsystems

import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.tuning.Subsystems
import kotlin.math.abs

class Shooter(val shooterMotor: MotorEx) {
    init {
        shooterMotor.setRunMode(Motor.RunMode.VelocityControl)
        update()
    }

    fun update() {
        shooterMotor.setVeloCoefficients(
            Subsystems.Shooter.Kp,
            Subsystems.Shooter.Ki,
            Subsystems.Shooter.Kd
        )
        shooterMotor.setFeedforwardCoefficients(
            Subsystems.Shooter.Ks,
            Subsystems.Shooter.Kv,
            Subsystems.Shooter.Ka
        )
        if (armed) shooterMotor.velocity = tps
        else shooterMotor.velocity = 0.0
    }

    var tps: Double = Subsystems.Shooter.targetTPS
//        set(value) {
//            field = value
//            shooterMotor.velocity = if (armed) tps else 0.0
//        }

    var armed: Boolean = false
//        set(value) {
//            field = value
//            shooterMotor.velocity = if (armed) tps else 0.0
//        }


    val spunUp: Boolean
        get() = abs(tps) - abs(shooterMotor.velocity) < Subsystems.Shooter.tolerance

    val realTPS: Double
        get() = shooterMotor.velocity

    val realAccel: Double
        get() = shooterMotor.acceleration

    val realCurrent: Double
        get() = shooterMotor.getCurrent(CurrentUnit.MILLIAMPS)
}