package ru.transaero21.mt.models.core

class MovableImpl : Movable {
    override var x: Float = 0f
    override var y: Float = 0f
    override var futureX: Float? = null
    override var futureY: Float? = null
}
