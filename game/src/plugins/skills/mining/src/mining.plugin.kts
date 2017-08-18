import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import kotlin.properties.Delegates

class MiningAction : DistancedAction<Player> {

    var ore: Ore by Delegates.notNull()
    var counter: Int = 0
    var started: Boolean = false

    constructor(player: Player, position: Position, ore: Ore) : super(0, true, player, position, 1 /* ORE SIZE */) {
        this.ore = ore
    }

    override fun executeAction() {
        val level = mob.skillSet.getSkill(Skill.MINING).currentLevel
        val pickaxe = findPickaxe()
        mob.turnTo(position)

        //check that our pick can mine the ore
        if (pickaxe == null || level < pickaxe.level) {
            mob.sendMessage("You do not have a pickaxe for which you have the level to use.")
            stop()
            return
        }

        //check that we can mine the ore
        if (ore.level > level) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
            return
        }

        //start the process of mining
        if (started) {
            if (counter == 0) {
                //TODO: calculate the chance that the player can actually get the ore
                if (mob.inventory.add(ore.id)) {
                    mob.sendMessage("You managed to mine some " + ItemDefinition.lookup(ore.id).name.substring(3).toLowerCase() + ".")
                    mob.skillSet.addExperience(Skill.MINING, ore.exp)
                    //TODO: Expire ore
                }
            }
            counter -= 1
        } else {
            startMine(pickaxe)
        }
    }

    fun findPickaxe(): Pickaxe? {
        var holding = mob.equipment.get(EquipmentConstants.WEAPON)
        for (id in PICKAXE_IDS) {
            if (mob.equipment.get(EquipmentConstants.WEAPON).id == id) {
                return PICKAXES[id];
            } else if (mob.inventory.contains(id)) {
                return PICKAXES[id];
            }
        }
        return null;
    }

    fun startMine(pickaxe: Pickaxe) {
        started = true
        mob.sendMessage("You swing your pick at the rock.")
        mob.playAnimation(pickaxe.animation)
        counter = pickaxe.pulses
    }
}

class ExpiredProspectingAction : DistancedAction<Player> {

    constructor(mob: Player, position: Position) : super(0, true, mob, position, 1 /* ORE SIZE */)

    override fun executeAction() {
        mob.sendMessage("There is currently no ore avalible in this rock.")
    }

}

class ProspectingAction : DistancedAction<Player> {

    var started = false;
    var ore: Ore by Delegates.notNull()

    constructor(mob: Player, position: Position, ore: Ore) : super(3 /* PROSPECT PULSES */, true, mob, position, 1 /* ORE SIZE */) {
        this.ore = ore;
    }

    override fun executeAction() {
        if (started) {
            mob.sendMessage("This rock contains " + ItemDefinition.lookup(ore.id).name.substring(3).toLowerCase() + ".")
        } else {
            started = true
            mob.sendMessage("You examine the rock for ores...")
            mob.turnTo(position)
        }
    }
}

on {ObjectActionMessage::class}
        .where {option == 1 && ORES.contains(id)}
        .then {
            it.startAction(MiningAction(it, this.position, ORES.get(id)!!))
        }

on {ObjectActionMessage::class}
        .where {option == 2}
        .then {
            if (ORES.contains(id)) {

            } else if (EXPIRED_ORES.contains(id)) {
                it.startAction(ExpiredProspectingAction(it, this.position))
            }
        }

//Init the ore dict when we start the server.
start {
    for (ore in ORE_OBJECTS) {
        for (key in ore.objects.keys) {
            ORES.put(key, ore)
            EXPIRED_ORES.put(ore.objects.get(key)!!, true)
        }
    }
}