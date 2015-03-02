
java_import 'org.apollo.game.message.impl.ConfigMessage'

# Declares the active prayer attribute.
declare_attribute(:active_prayer, -1, :persistent)


# The hash of button ids to prayers.
PRAYERS = {}

# Intercept the ButtonMessage to toggle a prayer.
on :message, :button do |ctx, player, message|
    button = message.widget_id
	prayer = PRAYERS[button]

    unless prayer.nil?
        if (prayer.level > player.skill_set.get_maximum_level(Skill::PRAYER))
            update_setting(player, prayer, :off)
            next
        end
        player.send_message("after level check")

        previous = player.active_prayer

        unless previous == -1
            update_setting(player, PRAYERS[previous], :off)
        end

        if previous != button
            player.send_message("Previous: #{previous}, new: #{button}.")
            update_setting(player, prayer, :on)
            player.active_prayer = button
        end
    end
end

private

# A Prayer that can be activated by a player.
class Prayer
    attr_reader :name, :level, :button, :setting, :drain

    def initialize(name, level, button, setting, drain)
        @name = name
    	@level = level
    	@button = button
    	@setting = setting
    	@drain = drain
    end

end

def update_setting(player, prayer, state)
    value = (state == :on) ? 1 : 0
    player.send_message("Toggling prayer #{prayer.name}, state: #{state}.")
    player.send(ConfigMessage.new(prayer.setting, value))
end

# Appends a Prayer to the hash.
def append_prayer(properties) 
    raise 'Error: prayer properties hash must contain a name, level, button, setting, and drain.' unless properties.has_keys?(:name, :level, :button, :setting, :drain)

    button = properties[:button]
    PRAYERS[button] = Prayer.new(properties[:name], properties[:level], button, properties[:setting], properties[:drain])
end

# Don't deal with the actual effect here to avoid mess (TODO do it, but with attributes?).
append_prayer name: :thick_skin,          level: 1,  button: 5609, setting: 83, drain: 0.01
append_prayer name: :burst_of_strength,   level: 4,  button: 5610, setting: 84, drain: 0.01
append_prayer name: :clarity_of_thought,  level: 7,  button: 5611, setting: 85, drain: 0.01
append_prayer name: :rock_skin,           level: 10, button: 5612, setting: 86, drain: 0.04
append_prayer name: :superhuman_strength, level: 13, button: 5613, setting: 87, drain: 0.04
append_prayer name: :improved_reflexes,   level: 16, button: 5614, setting: 88, drain: 0.04

append_prayer name: :rapid_restore, level: 19, button: 5615, setting: 89, drain: 0.01
append_prayer name: :rapid_heal,    level: 22, button: 5615, setting: 90, drain: 0.01
append_prayer name: :protect_item,  level: 25, button: 5617, setting: 91, drain: 0.01

append_prayer name: :steel_skin,          level: 28, button: 5618, setting: 92, drain: 0.1
append_prayer name: :ultimate_strength,   level: 31, button: 5619, setting: 93, drain: 0.1
append_prayer name: :incredible_reflexes, level: 34, button: 5620, setting: 94, drain: 0.1

append_prayer name: :protect_from_magic,    level: 37, button: 5621, setting: 95, drain: 0.15
append_prayer name: :protect_from_missiles, level: 40, button: 5622, setting: 96, drain: 0.15
append_prayer name: :protect_from_melee,    level: 43, button: 5633, setting: 97, drain: 0.15

append_prayer name: :retribution, level: 46, button: 683, setting: 98,  drain: 0.15
append_prayer name: :redemption,  level: 49, button: 684, setting: 99,  drain: 0.15
append_prayer name: :smite,       level: 52, button: 685, setting: 100, drain: 0.2