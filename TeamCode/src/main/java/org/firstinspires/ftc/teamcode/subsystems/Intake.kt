package org.firstinspires.ftc.teamcode.subsystems

import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.tuning.Subsystems

class Intake(val intakeMotor: MotorEx, val transferMotor: MotorEx) {
    init {
        intakeMotor.setRunMode(Motor.RunMode.VelocityControl)
        intakeMotor.setVeloCoefficients(
            Subsystems.Intake.Kp,
            Subsystems.Intake.Ki,
            Subsystems.Intake.Kd
        )

        transferMotor.setRunMode(Motor.RunMode.VelocityControl)
        transferMotor.setVeloCoefficients(
            Subsystems.Transfer.Kp,
            Subsystems.Transfer.Ki,
            Subsystems.Transfer.Kd
        )
    }
}