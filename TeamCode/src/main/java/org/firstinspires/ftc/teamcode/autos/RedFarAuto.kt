package org.firstinspires.ftc.teamcode.autos

import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@Autonomous
class RedFarAuto : LinearOpMode() {
    override fun runOpMode() {
        val follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(88.000, 8.000, Math.toRadians(90.0)))
        follower.update()

        val path = follower
            .pathBuilder()
            .addPath(
                BezierCurve(
                    Pose(88.000, 8.000),
                    Pose(96.000, 24.000),
                    Pose(108.000, 8.000)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(90.0), Math.toRadians(90.0))
            .build()

        waitForStart()
        follower.followPath(path)
        while (opModeIsActive() && follower.followingPathChain) {
            follower.update()
        }
    }
}
