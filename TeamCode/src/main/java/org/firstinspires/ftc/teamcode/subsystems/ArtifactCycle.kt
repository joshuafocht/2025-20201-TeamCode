package org.firstinspires.ftc.teamcode.subsystems

import com.bylazar.telemetry.JoinedTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import org.firstinspires.ftc.teamcode.tuning.Subsystems

class ArtifactCycle(
        val follower: Follower,
        val driveToArtifactPath: PathChain,
        val pickupArtifactPath: PathChain,
        val driveToGoalPath: PathChain,
        val shooter: Shooter,
        val intakeMotor: MotorEx,
        val transferMotor: MotorEx,
        val telemetryJ: JoinedTelemetry
    ) {
    var timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    var state = -1
        set(value) {
            field = value
            timer.reset()
        }
    var finished = false

    fun update() {
        telemetryJ.addData("state", state)
        telemetryJ.addData("time", timer.time())
        when (state) {
            0 -> { // Start driveToArtifactPath
                finished = false
                follower.followPath(driveToArtifactPath)
                state++
            }
            1 -> { // Wait for driveToArtifactPath to finish
                if (!follower.followingPathChain) state++
            }
            2 -> { // Start intakeMotor and transferMotor
                intakeMotor.set(Subsystems.ArtifactCycle.intakeRunInPower)
                transferMotor.set(Subsystems.ArtifactCycle.transferRunInPower)
                state++
            }
            3 -> { // Start pickupArtifactPath
                follower.followPath(pickupArtifactPath)
                state++
            }
            4 -> { // Wait for path to finish and stop transfer halfway through
                if (!follower.followingPathChain) state++
                if (timer.time() >= Subsystems.ArtifactCycle.transferRunInTime) transferMotor.set(0.0)
            }
            5 -> { // Let intake run for some extra time at the end to bring the last ball in
//                if (timer.time() >= Subsystems.ArtifactCycle.intakeRunInTime) {
//                    intakeMotor.set(0.0)
//                    state++
//                }
                state++
            }
            6 -> { // Drive to goal
                follower.followPath(driveToGoalPath)
                state++
            }
            7 -> { // Wait for path to finish
                if (!follower.followingPathChain) state++
            }
            8 -> { // Run transfer backwards to get ball out of shooter
                if (timer.time() < Subsystems.ArtifactCycle.transferRunOutTime) {
                    intakeMotor.set(0.0)
                    transferMotor.set(Subsystems.ArtifactCycle.transferRunOutPower)
                } else {
                    transferMotor.set(0.0)
                    state++
                }
            }
            9 -> { // Spin up shooter
                shooter.armed = true
                telemetryJ.addData("shooter.tps", shooter.tps)
                telemetryJ.addData("shooter.realTPS", shooter.realTPS)
                if (shooter.spunUp) state++
            }
            10 -> { // Shoot the balls
                if (timer.time() < Subsystems.ArtifactCycle.shooterTime) {
                    intakeMotor.set(1.0)
                    transferMotor.set(1.0)
                } else {
                    intakeMotor.set(0.0)
                    transferMotor.set(0.0)
                    shooter.armed = false
                    state++
                }
            }
            11 -> { // Show that we are done
                telemetryJ.clearAll()
                telemetryJ.addLine("Done!")
                finished = true
                state++
            }
        }
    }
}