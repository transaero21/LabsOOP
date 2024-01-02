package ru.transaero21.mt.models.ammo.bullet

import ru.transaero21.mt.models.ammo.Ammunition

abstract class Bullet(
    x: Float, y: Float, angle: Float, velocity: Float
) : Ammunition(x = x, y = y, angle = angle, velocity = velocity) {
    var isHitEnd: Boolean = false
        private set(value) { if (!field) field = value }
    var state: BulletState = if (velocity != 0F) BulletState.TRANSIT else BulletState.DISPOSE

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (distanceCurrent >= distanceMax) {
            if (isHitEnd && state == BulletState.TRANSIT) {
                state = BulletState.DISPOSE
            } else if (!isHitEnd) isHitEnd = true
        }
    }
}
