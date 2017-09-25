import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.Graphic
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.util.security.PlayerCredentials
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AmmoDslTests {

    private val TEST_AMMO_ID : Int = 0

    @Before
    fun setupTestAmmo() {
        val testItem = ItemDefinition(TEST_AMMO_ID)
        testItem.name = "bronze arrow"

        ItemDefinition.init(arrayOf(testItem))

        ammo("arrow") {
            projectile {
                startHeight = 10
                endHeight = 20
                delay = 30
                lifetime = 40
                pitch = 60
            }

            "bronze" {
                requires level 1
                20% dropChance

                graphics {
                    projectile = 10
                    attack = 20
                }
            }
        }
    }

    @Test
    fun `Ammo variants should inherit projectile parameters from their ammo type`() {
        val ammo = AMMO[TEST_AMMO_ID]!!
        val world = World()
        val target = Player(world, PlayerCredentials("fake", "fake", 1, 1, "fake"), Position(1, 1))
        val source = Position(1, 1)
        val ammoProjectile = ammo.projectileFactory(World(), source, target)

        assertEquals(10, ammoProjectile.startHeight)
        assertEquals(20, ammoProjectile.endHeight)
        assertEquals(30, ammoProjectile.delay)
        assertEquals(40, ammoProjectile.lifetime)
        assertEquals(60, ammoProjectile.pitch)
        assertEquals(10, ammoProjectile.graphic)
    }

    @Test
    fun `Ammo variants should set their level requirements correctly`() {
        val ammo = AMMO[TEST_AMMO_ID]!!

        assertEquals(1, ammo.requiredLevel)
    }

    @Test
    fun `Ammo variants should set their drop chance correctly`() {
        val ammo = AMMO[TEST_AMMO_ID]!!

        assertEquals(ammo.dropChance, 0.2, 0.0)
    }

    @Test
    fun `Ammo variants should set their graphics correctly`() {
        val ammo = AMMO[TEST_AMMO_ID]!!

        assertEquals(20, ammo.attack?.id)
    }
}