package org.firstinspires.ftc.teamcode.subsystems

import com.bylazar.telemetry.JoinedTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.hardware.servos.ServoEx
import org.firstinspires.ftc.teamcode.tuning.Subsystems

class ArtifactCycle(
        val follower: Follower,
        val driveToArtifactPath: PathChain,
        val pickupArtifactPath: PathChain,
        val driveToGoalPath: PathChain,
        val tps: Double,
        val shooter: Shooter,
        val intake: Intake,
        val antiJamServo: ServoEx,
        val telemetryJ: JoinedTelemetry,
        val idle: Boolean
    ) {
    var timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    var state = 0
    var enabled = false
    var finished = false

    fun update() {
        telemetryJ.addData("intakeState", state)
        telemetryJ.addData("finished", finished)
        telemetryJ.addData("time", timer.time())
        when (state) {
            0 -> {
                if (enabled) state++
            }
            1 -> { // Start driveToArtifactPath
                finished = false
                follower.followPath(driveToArtifactPath)
                state++
            }
            2 -> { // Wait for driveToArtifactPath to finish
                if (!follower.followingPathChain) state++
            }
            3 -> { // Enable intake and start pickupArtifactPath
                intake.enabled = true
                follower.followPath(pickupArtifactPath, 0.5, true)
                state++
            }
            4 -> { // Wait for path to finish
                if (!follower.followingPathChain) state++
            }
            5 -> { // Drive to goal
                follower.followPath(driveToGoalPath)
                timer.reset()
                state++
            }
            6 -> { // Wait for path to finish
                if (intake.finished && idle) {
                    shooter.tps = Subsystems.Shooter.preSpeed
                }
                if (!follower.followingPathChain && (intake.finished || timer.time() >= Subsystems.ArtifactCycle.intakeTimeout)) {
                    state++
                    timer.reset()
                    intake.enabled = false
                }
            }
            7 -> { // Enable shooter and wait for spinup
                shooter.tps = tps
                antiJamServo.set(Subsystems.AntiJam.shootPos)
                if (!idle) shooter.enabled = true
                if (shooter.spunUp && (!idle || timer.time() > Subsystems.AntiJam.moveTime)) {
                    state++
                    timer.reset()
                }
            }
            8 -> { // Start shooting for a time
                intake.intakeMotor.set(Subsystems.Shooter.intakeSpeed)
                intake.transferMotor.set(Subsystems.Shooter.transferSpeed)
                if (timer.time() > Subsystems.Shooter.shootTime) state++
            }
            9 -> { // Stop shooter intake and transfer
                if (!idle) shooter.enabled = false
                else shooter.tps = Subsystems.Shooter.idleSpeed
                antiJamServo.set(Subsystems.AntiJam.blockPos)
                intake.intakeMotor.set(0.0)
                intake.transferMotor.set(0.0)
                telemetryJ.clearAll()
                telemetryJ.addLine("Done!")
                finished = true
                enabled = false
                state = 0
            }
        }
    }
}