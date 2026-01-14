package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.tuning.Subsystems

class Intake(val intakeMotor: MotorEx, val transferMotor: MotorEx, val shooter: Shooter) {
    enum class IntakeStates {
        IDLE,
        SPIN_BACK,
        BACK_OFF,
        FINISH
    }

    var timer: ElapsedTime = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    var state: IntakeStates = IntakeStates.IDLE
    
    fun update(run: Boolean) {
        when (state) {
            IntakeStates.IDLE -> {
                if (run) state = IntakeStates.IDLE
            }
            IntakeStates.SPIN_BACK -> {
                intakeMotor.set(1.0)
                transferMotor.set(1.0)

                shooter.tps = Subsystems.Intake.shooterBackTPS
                shooter.armed = true

                if (shooter.realTPS < 100) {
                    shooter.armed = false
                    state = IntakeStates.BACK_OFF
                    timer.reset()
                }
            }
            IntakeStates.BACK_OFF -> {
                intakeMotor.set(Subsystems.Intake.transferBackPower)
                transferMotor.set(Subsystems.Intake.transferBackPower)
                if (timer.time() >= Subsystems.Intake.transferBackTime) {
                    state = IntakeStates.FINISH
                    intakeMotor.set(1.0)
                    transferMotor.set(0.0)
                }
            }
            IntakeStates.FINISH -> {
                if (!run) {
                    intakeMotor.set(0.0)
                    state = IntakeStates.IDLE
                }
            }
        }
    }
    
}