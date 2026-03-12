package org.firstinspires.ftc.teamcode.subsystems

import org.firstinspires.ftc.teamcode.tuning.Subsystems

class EMA {
    var ema: Double = 0.0
    var alpha: Double = 0.0
    var new: Double = 0.0

    fun update(newval: Double) {
        new = newval
        alpha = Subsystems.EMA.alpha
        ema = (new * alpha) + (ema * (1 - alpha))
    }
}