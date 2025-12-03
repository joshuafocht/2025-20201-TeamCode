package org.firstinspires.ftc.teamcode.tuning

import com.bylazar.configurables.annotations.Configurable

object Subsystems {
    @Configurable
    object Shooter {
        @JvmField
        var Kp: Double = 5.0

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
        var targetTPS: Double = 1000.0
    }

    @Configurable
    object Intake {
        @JvmField
        var Kp: Double = 1.0

        @JvmField
        var Ki: Double = 0.0

        @JvmField
        var Kd: Double = 0.0

        @JvmField
        var tps: Double = 50.0
    }

    @Configurable
    object Transfer {
        @JvmField
        var Kp: Double = 1.0

        @JvmField
        var Ki: Double = 0.0

        @JvmField
        var Kd: Double = 0.0

        @JvmField
        var tps: Double = 50.0
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
}