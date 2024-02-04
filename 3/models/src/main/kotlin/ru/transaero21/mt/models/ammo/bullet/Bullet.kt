package ru.transaero21.mt.models.ammo.bullet

import ru.transaero21.mt.models.ammo.Ammunition

abstract class Bullet(
    x: Float, y: Float, angle: Float, velocity: Float
) : Ammunition(x = x, y = y, angle = angle, velocity = velocity) {
    var isHitEnd: Boolean = false
        private set(value) { if (!field) field = value }
    @Volatile var state: BulletState = if (velocity != 0F) BulletState.Transit else BulletState.Dispose

    override fun update(delta: Float) {
        super.update(delta)

        if (distanceCurrent >= distanceMax) {
            if (isHitEnd && state == BulletState.Transit) {
                state = BulletState.Dispose
            } else if (!isHitEnd) isHitEnd = true
        }
    }
}
