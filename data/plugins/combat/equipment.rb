java_import 'org.apollo.cache.def.EquipmentDefinition'

EQUIPMENT = {}

def create_equipment(item, &block)
  equipment = Equipment.new
  equipment.instance_eval block

  find_entities :item, item do |equipment_item|
    EQUIPMENT[id] = equipment
  end
end

private

class Equipment
  include Combat::BonusContainer
end