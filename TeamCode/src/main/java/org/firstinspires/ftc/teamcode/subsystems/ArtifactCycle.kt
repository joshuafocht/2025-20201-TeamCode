package org.firstinspires.ftc.teamcode.subsystems

import com.bylazar.telemetry.JoinedTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.tuning.Subsystems

class ArtifactCycle(
        val follower: Follower,
        val driveToArtifactPath: PathChain,
        val pickupArtifactPath: PathChain,
        val driveToGoalPath: PathChain,
        val tps: Double,
        val shooter: Shooter,
        val intake: Intake,
        val telemetryJ: JoinedTelemetry
    ) {
    var timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)
    var state = 0
    var enabled = false
    var finished = false

    fun update() {
        telemetryJ.addData("state", state)
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
                if (!follower.followingPathChain && (intake.finished || timer.time() >= Subsystems.ArtifactCycle.intakeTimeout)) {
                    state++
                    intake.enabled = false
                }
            }
            7 -> { // Enable shooter and wait for spinup
                shooter.tps = tps
                shooter.enabled = true
                state++
            }
            8 -> { // Wait for shooter to spin up
                if (shooter.spunUp) {
                    state++
                    timer.reset()
                }
            }
            9 -> { // Start shooting for a time
                intake.intakeMotor.set(Subsystems.Shooter.intakeSpeed)
                intake.transferMotor.set(Subsystems.Shooter.transferSpeed)
                if (timer.time() > Subsystems.Shooter.shootTime) state++
            }
            10 -> { // Stop shooter intake and transfer
                shooter.enabled = false
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