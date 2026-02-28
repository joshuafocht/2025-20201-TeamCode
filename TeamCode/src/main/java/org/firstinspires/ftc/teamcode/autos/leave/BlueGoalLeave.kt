package org.firstinspires.ftc.teamcode.autos.leave

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.tuning.Subsystems


@Autonomous
class BlueGoalLeave : LinearOpMode() {
    override fun runOpMode() {
        var state = 0
        val timer = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(32.000, 134.500, Math.toRadians(270.0)))

        val leavePath = follower
            .pathBuilder()
            .addPath(
                BezierLine(
                    Pose(32.000, 134.500),
                    Pose(126.000, 100.000)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(270.0))
            .build()


        waitForStart()
        timer.reset()
        while (opModeIsActive()) {
            follower.update()

            when (state) {
                0 -> {
                    if (timer.time() > Subsystems.AutoDelay.leaveDelay) state++
                }
                1 -> {
                    follower.followPath(leavePath)
                    state++
                }
                2 -> {
                    if (!follower.followingPathChain) state++
                }
                else -> { break }
            }
        }
    }
}
