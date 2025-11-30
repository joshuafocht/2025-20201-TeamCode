package org.firstinspires.ftc.teamcode.subsystems

import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.tuning.Subsystems

class Shooter(val shooterMotor: MotorEx) {
    init {
        shooterMotor.setRunMode(Motor.RunMode.VelocityControl)
        shooterMotor.setVeloCoefficients(
            Subsystems.Shooter.Kp,
            Subsystems.Shooter.Ki,
            Subsystems.Shooter.Kd
        )
    }

    var tps: Double
        get() = tps
        set(value) {
            tps = value
            shooterMotor.velocity = if (armed) 0.0 else tps
        }

    var armed: Boolean
        get() = armed
        set(value) {
            armed = value
            shooterMotor.velocity = if (armed) 0.0 else tps
        }

    val realTPS: Double
        get() = shooterMotor.velocity

    val realAccel: Double
        get() = shooterMotor.acceleration

    val realCurrent: Double
        get() = shooterMotor.getCurrent(CurrentUnit.MILLIAMPS)
}