
on :message, :item_on_item do |player, message|
  primary = message.id
  secondary = message.target_id

  make_recipe(player, :item, primary, secondary)
end

on :message, :item_on_object do |player, message|
  object = message.objectId
  item = message.id

  make_recipe(player, :object, object, item)
end

on :message, :item_option do |player, message|
  item = message.id
  option = message.option

  make_recipe(player, :click, item, option)
end