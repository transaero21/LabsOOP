package ru.transaero21.mt.models.core.iterators

import org.junit.jupiter.api.assertThrows
import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.ammo.bullet.Bullet
import ru.transaero21.mt.models.ammo.bullet.BulletImpl
import ru.transaero21.mt.models.core.Headquarter
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.managers.Commander
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AmmoIteratorTest {

    @Test
    fun hasNextShouldReturnTrueWhenNextAmmoExistsTest() {
        val headquarter = createHeadquarterWithAmmunition(1)
        val ammoIterator = AmmoIterator(headquarter)
        val result = ammoIterator.hasNext()
        assertTrue(result)
    }

    @Test
    fun hasNextShouldReturnFalseWhenNoNextAmmoTest() {
        val headquarter = createHeadquarterWithAmmunition(0)
        val ammoIterator = AmmoIterator(headquarter)
        val result = ammoIterator.hasNext()
        assertFalse(result)
    }

    @Test
    fun nextShouldReturnNextAmmo() {
        val headquarter = createHeadquarterWithAmmunition(3)
        val ammoIterator = AmmoIterator(headquarter)
        val nextAmmo = ammoIterator.next()
        assertEquals(1, nextAmmo.key)
    }

    @Test
    fun catchExceptionOnNoNextAmmoTest() {
        val headquarter = createHeadquarterWithAmmunition(0)
        val ammoIterator = AmmoIterator(headquarter)
        assertThrows<NoSuchElementException> { ammoIterator.next() }
    }

    private fun createHeadquarterWithAmmunition(ammoCount: Int): Headquarter {
        val commander = Commander(fullName = "Test Commander", rank = Rank(title = "Test Rank", weight = 0f))

        val headquarter = Headquarter(0F, 0F, commander)

        for (i in 1..ammoCount) {
            val ammo = createAmmunitionStub()
            headquarter.ammunition[i] = ammo
        }

        return headquarter
    }

    private fun createAmmunitionStub(): Ammunition = BulletImpl(x = 0F, y = 0F, angle = 45F, velocity = 10F)
}