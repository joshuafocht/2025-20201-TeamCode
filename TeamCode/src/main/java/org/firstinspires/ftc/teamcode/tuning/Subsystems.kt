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
        var shootTime: Double = 2000.0

        @JvmField
        var changeTPS: Double = 10.0

        @JvmField
        var targetTPS: Double = 1400.0

        @JvmField
        var tolerance: Double = 100.0

        @JvmField
        var transferSpeed: Double = 0.75

        @JvmField
        var intakeSpeed: Double = 1.0
    }

    @Configurable
    object Intake {
        @JvmField
        var spinUpTimeOut: Double = 2000.0
        @JvmField
        var shooterBackTPS: Double = -500.0

        @JvmField
        var intakeInPower: Double = 1.0

        @JvmField
        var transferInPower: Double = 0.1

        @JvmField
        var intakeOutPower: Double = 0.0

        @JvmField
        var transferOutPower: Double = -1.0

        @JvmField
        var transferBackOffTime: Int = 500
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

        @JvmField
        var intakeTimeout: Int = 2500
    }

    @Configurable
    object Align {
        @JvmField
        var Kp: Double = 0.013

        @JvmField
        var Ki: Double = 0.055

        @JvmField
        var Kd: Double = 0.002

        @JvmField
        var Kf: Double = 0.0

        @JvmField
        var m: Double = 6.2

        @JvmField
        var b: Double = 1100.0

        @JvmField
        var tolerance: Double = 2.0

        @JvmField
        var pauseTime: Double = 500.0
    }

    @Configurable
    object BlueTeleOp {
        @JvmField
        var tagOffset = -0.5
    }

    @Configurable
    object RedTeleOp {
        @JvmField
        var tagOffset = 0.5
    }

    @Configurable
    object BlueGoalAuto {
        @JvmField
        var angle = 135.0

        @JvmField
        var tps = 1400.0
    }

    @Configurable
    object RedGoalAuto {
        @JvmField
        var angle = 45.0

        @JvmField
        var tps = 1400.0
    }

    @Configurable
    object BlueFarAuto {
        @JvmField
        var angle = 117.0

        @JvmField
        var tps = 1750.0
    }

    @Configurable
    object RedFarAuto {
        @JvmField
        var angle = 63.0

        @JvmField
        var tps = 1750.0
    }

    @Configurable
    object AutoDelay {
        @JvmField
        var safeDelay = 15000

        @JvmField
        var leaveDelay = 25000
    }
}