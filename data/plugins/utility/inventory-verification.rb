java_import 'org.apollo.game.model.Inventory'
java_import 'org.apollo.game.event.impl.ItemActionEvent'

def valid_inventory_action(inventory, event)

  if !inventory.is_a?(Inventory)
    raise "Inventory argument is not an instance of org.apollo.game.model.Inventory."
  end

  if !event.is_a?(ItemActionEvent)
    raise "Event argument is not an instance of org.apollo.game.event.impl.ItemActionEvent."
  end

  slot = event.get_slot()

  if slot < 0 or slot > inventory.capacity()
    return false
  end

  item = inventory.get(slot)
  if item.nil? or item.get_id() != event.get_id()
    return false
  end

  return true
end