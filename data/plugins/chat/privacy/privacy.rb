require 'java'

java_import 'org.apollo.game.model.entity.setting.PrivacyState'
java_import 'org.apollo.game.message.impl.SendFriendMessage'

on :message, :privacy_option do |player, message|
  player.chat_privacy = message.chat_privacy
  player.friend_privacy = message.friend_privacy
  player.trade_privacy = message.trade_privacy

  update_friends(player, message.friend_privacy == PrivacyState::OFF ? 0 : player.world_id)
end
