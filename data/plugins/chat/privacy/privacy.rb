require 'java'

java_import 'org.apollo.game.model.setting.PrivacyState'
java_import 'org.apollo.game.event.impl.SendFriendEvent'

on :event, :privacy_option do |ctx, player, event|
  player.chat_privacy = event.chat_privacy
  player.friend_privacy = event.friend_privacy
  player.trade_privacy = event.trade_privacy

  update_friends(player, event.friend_privacy == PrivacyState::OFF ? 0 : player.world_id)
end