package ru.transaero21.mt.models.core.orders

import kotlinx.serialization.Serializable
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.instructions.Defuse
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.Move
import ru.transaero21.mt.models.units.fighters.FieldCommander

@Serializable
class Defuse : Order {
    constructor(x: Float, y: Float, fcId: Int) : super(x, y, fcId)

    override fun getInstructions(fc: FieldCommander): OrderedMap<Int, List<Instruction>> {
        return OrderedMap<Int, List<Instruction>>().also { map ->
            fc.formation.getExpectedPositions(x = x, y = y).forEach { (k, v) ->
                map[k] = listOf(Move(x = v.first, y = v.second), Defuse)
            }
            map[-1] = listOf(Move(x = x, y = y), Defuse)
        }
    }
}
