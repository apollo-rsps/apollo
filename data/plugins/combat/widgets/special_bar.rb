java_import 'org.apollo.game.message.impl.ConfigMessage'

def update_special_bar(player)
  player.send(ConfigMessage.new(300, player.special_energy * 10)) # special energy
  player.send(ConfigMessage.new(301, player.using_special ? 1 : 0)) # special enabled
end

on :login do |event|
  player = event.player

  update_special_bar player

  schedule 25 do |task|
    unless player.is_active
      task.stop
      next
    end

    player.special_energy = [player.special_energy + 5, 100].min
    update_special_bar player
  end
end
