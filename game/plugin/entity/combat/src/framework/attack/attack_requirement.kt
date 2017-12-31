import AttackRequirementResult.Failed
import AttackRequirementResult.Ok
import org.apollo.cache.def.EquipmentDefinition
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Mob

sealed class AttackRequirementResult {
    data class Failed(val message: String) : AttackRequirementResult()
    object Ok : AttackRequirementResult()
}

interface AttackRequirement {
    /**
     * Check if the [mob] initiating the [BasicAttack] meets the requirements.
     */
    fun check(mob: Mob): AttackRequirementResult

    /**
     * Apply this [AttackRequirement] to the [Mob] that initiated the doAttack.  Optionally carrying out an action
     * that removes any resources used up by the doAttack.
     */
    fun apply(mob: Mob)
}

class ItemRequirement(val itemId: Int, val amount: Int = 1) : AttackRequirement {
    override fun check(mob: Mob): AttackRequirementResult {
        if (mob.inventory.getAmount(itemId) < amount) {
            return Failed("You don't have enough items") //@todo -message
        }

        return Ok
    }

    override fun apply(mob: Mob) {
        mob.inventory.remove(itemId, amount)
    }

}

class AmmoRequirement(val ammoType: AmmoType, val amount: Int) : AttackRequirement {
    override fun check(mob: Mob): AttackRequirementResult {
        val weaponItem = mob.equipment[EquipmentConstants.WEAPON]
        val weaponReqLevel = EquipmentDefinition.lookup(weaponItem.id).rangedLevel
        val ammoItem = mob.equipment[EquipmentConstants.ARROWS]
        val ammo = ammoType.items[ammoItem?.id]

        if (ammoItem == null) {
            return Failed("You have no ammo left in your quiver!")
        }

        if (amount > ammoItem.amount) {
            return Failed("You don't have enough ammo left in your quiver!")
        }

        if (ammo == null || ammo.requiredLevel > weaponReqLevel) {
            return Failed("You can't use this ammo with your current weapon.")
        }

        return Ok
    }

    override fun apply(mob: Mob) {
        mob.equipment.removeSlot(EquipmentConstants.ARROWS, amount)
    }
}