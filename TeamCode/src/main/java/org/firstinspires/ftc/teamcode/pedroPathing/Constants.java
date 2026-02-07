package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(10.8)
            .forwardZeroPowerAcceleration(-25.564)
            .lateralZeroPowerAcceleration(-60.219)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.1, 0, 0.01, 0.02))
            .headingPIDFCoefficients(new PIDFCoefficients(1.5, 0, 0.1, 0.02))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.03, 0, 0.00001, 0.6, 0.01))
            .centripetalScaling(0.0005);

    // brakingStart: started out 1.2 (bit early), 1.5 seemed fine (heading would drift over time, might be bad floor), trying 1.3 as a middle ground
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRightMotor")
            .rightRearMotorName("backRightMotor")
            .leftRearMotorName("backLeftMotor")
            .leftFrontMotorName("frontLeftMotor")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(64.813)
            .yVelocity(53.179);

//    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
//            .rightFrontMotorName("frontRightMotor")
//            .rightRearMotorName("backRightMotor")
//            .leftRearMotorName("backLeftMotor")
//            .leftFrontMotorName("frontLeftMotor")
//            .leftFrontEncoderDirection(Encoder.REVERSE)
//            .leftRearEncoderDirection(Encoder.REVERSE)
//            .rightFrontEncoderDirection(Encoder.FORWARD)
//            .rightRearEncoderDirection(Encoder.FORWARD)
//            .robotLength(17)
//            .robotWidth(17.5)
//            .forwardTicksToInches(0.006)
//            .strafeTicksToInches(0.0055)
//            .turnTicksToInches(0.0115);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(4.5)
            .strafePodX(4.5)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
//                .driveEncoderLocalizer(localizerConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }

}
