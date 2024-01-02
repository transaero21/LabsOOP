package ru.transaero21.mt.core

import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

object Calculations {
    fun isReached(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float): Boolean {
        return sign(x = x1 - x).let { it != sign(x = x1 - x2) || it == 0F } ||
                sign(x = y1 - y).let { it != sign(x = y1 - y2) || it == 0F }
    }

    fun getLength(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt(x = (x1 - x2).pow(n = 2) + (y1 - y2).pow(n = 2))
    }

    fun circleIntersectRect(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float, r: Float): Boolean {
        if (y < y1) {
            if (x < x1)
                return ((x - x1) * (x - x1) + (y - y1) * (y - y1)) <= r.pow(n = 2)
            if (x > x2)
                return ((x - x2) * (x - x2) + (y - y1) * (y - y1)) <= r.pow(n = 2)
            return (y1 - y) <= r
        }
        if (y > y2) {
            if (x < x1)
                return ((x - x1) * (x - x1) + (y - y2) * (y - y2)) <= r.pow(n = 2)
            if (x > x2)
                return ((x - x2) * (x - x2) + (y - y2) * (y - y2)) <= r.pow(n = 2)
            return (y - y2) <= r
        }
        if (x <= x1)
            return (x1 - x) <= r
        if (x >= x2)
            return (x - x2) <= r
        return true
    }
}
