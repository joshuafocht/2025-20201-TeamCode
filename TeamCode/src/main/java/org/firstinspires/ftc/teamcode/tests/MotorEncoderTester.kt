package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.seattlesolvers.solverslib.hardware.motors.MotorEx

@TeleOp
class MotorEncoderTester : LinearOpMode() {
    override fun runOpMode() {
        val frontLeftMotor: MotorEx = MotorEx(hardwareMap, "frontLeftMotor")
        val frontRightMotor: MotorEx = MotorEx(hardwareMap, "frontRightMotor")
        val backLeftMotor: MotorEx = MotorEx(hardwareMap, "backLeftMotor")
        val backRightMotor: MotorEx = MotorEx(hardwareMap, "backRightMotor")

        frontLeftMotor.inverted = true
        backLeftMotor.inverted = true


        while (!isStopRequested) {
            frontLeftMotor.set(0.1);
            frontRightMotor.set(0.1);
            backLeftMotor.set(0.1);
            backRightMotor.set(0.1);
        }
    }
}