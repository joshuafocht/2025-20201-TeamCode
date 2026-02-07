package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.tuning.Subsystems

class
Intake(val intakeMotor: MotorEx, val transferMotor: MotorEx, val shooter: Shooter) {
    enum class IntakeStates {
        IDLE,
        SPIN_UP,
        SPIN_BACK,
        BACK_OFF,
        FINISH
    }

    var timer: ElapsedTime = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    var state: IntakeStates = IntakeStates.IDLE
    
    fun update(run: Boolean) {
        when (state) {
            IntakeStates.IDLE -> {
                if (run) {
                    state = IntakeStates.SPIN_UP
                    timer.reset()
                }
            }
            IntakeStates.SPIN_UP -> {
                intakeMotor.set(Subsystems.Intake.intakeInPower)
                transferMotor.set(Subsystems.Intake.intakeOutPower)

                shooter.tps = Subsystems.Intake.shooterBackTPS
                shooter.armed = true

                if (shooter.spunUp) {
                    state = IntakeStates.SPIN_BACK
                    timer.reset()
                }

                if (!shooter.spunUp && timer.time() > Subsystems.Intake.spinUpTimeOut) {
                    state = IntakeStates.BACK_OFF
                    timer.reset()
                }
            }
            IntakeStates.SPIN_BACK -> {
                if (!shooter.spunUp) {
                    shooter.armed = false
                    state = IntakeStates.BACK_OFF
                    timer.reset()
                }
            }
            IntakeStates.BACK_OFF -> {
                intakeMotor.set(Subsystems.Intake.intakeOutPower)
                transferMotor.set(Subsystems.Intake.transferOutPower)
                if (timer.time() >= Subsystems.Intake.transferBackOffTime) {
                    state = IntakeStates.FINISH
                    intakeMotor.set(0.0)
                    transferMotor.set(0.0)
                }
            }
            IntakeStates.FINISH -> {
                if (!run) state = IntakeStates.IDLE
            }
        }
    }
    
}