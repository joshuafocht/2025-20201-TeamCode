package org.firstinspires.ftc.teamcode.tuning

import com.bylazar.configurables.annotations.Configurable

object Subsystems {
    @Configurable
    object Shooter {
        @JvmField
        var Kp: Double = 7.5

        @JvmField
        var Ki: Double = 0.0

        @JvmField
        var Kd: Double = 0.0

        @JvmField
        var Ks: Double = 0.0

        @JvmField
        var Kv: Double = 1.7

        @JvmField
        var Ka: Double = 0.0

        @JvmField
        var changeTPS: Double = 50.0

        @JvmField
        var targetTPS: Double = 1150.0

        @JvmField
        var tolerance: Double = 30.0
    }

    @Configurable
    object Intake {
        @JvmField
        var shooterBackTPS: Double = -500.0

        @JvmField
        var transferBackPower: Double = -0.5;

        @JvmField
        var transferBackTime: Int = 500;
    }

    @Configurable
    object ArtifactCycle {
        @JvmField
        var transferRunInTime: Int = 800

        @JvmField
        var transferRunInPower: Double = 1.0

        @JvmField
        var intakeRunInTime: Int = 500

        @JvmField
        var intakeRunInPower: Double = 1.0

        @JvmField
        var transferRunOutTime: Int = 500

        @JvmField
        var transferRunOutPower: Double = -0.6

        @JvmField
        var shooterTime: Int = 3000
    }

    @Configurable
    object Align {
        @JvmField
        var Kp: Double = 0.015

        @JvmField
        var Ki: Double = 0.0

        @JvmField
        var Kd: Double = 0.0

        @JvmField
        var Kf: Double = 0.0

        @JvmField
        var m: Double = 6.2

        @JvmField
        var b: Double = 1100.0

        @JvmField
        var tolerance: Double = 2.0
    }
}