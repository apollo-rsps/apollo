import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Animation
import org.apollo.game.model.Position
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.mining.*
import kotlin.properties.Delegates

class MiningAction(val player: Player, val p: Position, val ore: Ore) : DistancedAction<Player>(0, true, player, p, 1 /* ORE SIZE */) {

    private var counter: Int = 0
    private var started: Boolean = false

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
        if (level < ore.level) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
            return
        }

        //start the process of mining
        if (started) {
            if (counter == 0) {
                //TODO: calculate the chance that the player can actually get the ore
                if (mob.inventory.add(ore.id)) {

                    mob.sendMessage("You managed to mine some $(ItemDefinition.lookup(ore.id).name.substring(3).toLowerCase()).")
                    mob.skillSet.addExperience(Skill.MINING, ore.exp)
                    //TODO: Expire ore
                }
            }
            counter -= 1
        } else {
            startMine(pickaxe)
        }
    }

    fun findPickaxe(): Pickaxe? { // Find the best pick the player has
        for (id in getPickaxes()) {
            if (mob.equipment.get(EquipmentConstants.WEAPON).id == id) {
                return lookupPickaxe(id);
            } else if (mob.inventory.contains(id)) {
                return lookupPickaxe(id);
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
        mob.sendMessage("There is currently no ore available in this rock.")
    }
}

class ProspectingAction(val m: Player, val p: Position, val ore: Ore) : DistancedAction<Player>(3 /* PROSPECT PULSES */, true, m, p, 1 /* ORE SIZE */) {

    var started = false;

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

//Init the ore dict and add pickaxes
start {
    addPickaxe(1275, 41, Animation(624), 3)  // rune
    addPickaxe(1271, 31, Animation(628), 4) // adamant
    addPickaxe(1273, 21, Animation(629), 5) // mithril
    addPickaxe(1269, 1, Animation(627), 6)  // steel
    addPickaxe(1267, 1, Animation(626), 7)  // iron
    addPickaxe(1265, 1, Animation(625), 8)  // bronze

    addGem(1623, 0) // uncut sapphire
    addGem(1605, 0) // uncut emerald
    addGem(1619, 0) // uncut ruby
    addGem(1617, 0)  // uncut diamond

    for (ore in ORE_OBJECTS) {
        for (key in ore.objects.keys) {
            ORES.put(key, ore)
            EXPIRED_ORES.put(ore.objects.get(key)!!, true)
        }
    }
}