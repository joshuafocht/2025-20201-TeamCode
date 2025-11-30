package org.firstinspires.ftc.teamcode.tuning

import com.bylazar.configurables.annotations.Configurable

object Subsystems {
    @Configurable
    object Shooter {
        @JvmField
        var Kp: Double = 1.0

        @JvmField
        var Ki: Double = 0.0

        @JvmField
        var Kd: Double = 0.0

        @JvmField
        var changeTPS: Double = 50.0
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
}