package ru.transaero21.mt.models.ammo.mine

class BasicMine(
    x: Float, y: Float, angle: Float, velocity: Float
) : Mine(x = x, y = y, angle = angle, velocity = velocity) {
    override val hitRange: Float = 8F
    override val defuseTime: Float = 2F
    override val distanceMax: Float = 54F
    override val damage: Float = 15F
}