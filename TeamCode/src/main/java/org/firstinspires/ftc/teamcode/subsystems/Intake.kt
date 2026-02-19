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
    var enabled: Boolean = false
    var finished: Boolean = false
    
    fun update() {
        when (state) {
            IntakeStates.IDLE -> {
                finished = false
                if (enabled) {
                    state = IntakeStates.SPIN_UP
                    timer.reset()
                }
            }
            IntakeStates.SPIN_UP -> {
                intakeMotor.set(Subsystems.Intake.intakeInPower)
                transferMotor.set(Subsystems.Intake.transferInPower)

                shooter.tps = Subsystems.Intake.shooterBackTPS
                shooter.enabled = true

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
                if (!shooter.spunUp || !enabled) {
                    shooter.enabled = false
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
                finished = true
                if (!enabled) state = IntakeStates.IDLE
            }
        }
    }
    
}