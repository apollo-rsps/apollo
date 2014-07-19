
# Intercepts the first npc action event.
on :event, :npc_action do |ctx, player, event|
  if (event.option == 1)
    # TODO check if player is not in pvp area

  end
end

def dialogue(name, &block)

end

dialogue :banker_introduction do
  #
end