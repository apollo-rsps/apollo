require 'java'

java_import 'org.apollo.game.model.entity.Mob'

module MobExtension
  MOB_EXTENSIONS = []

  def self.register(extension)
    fail 'Provided extension object is not a module' unless extension.is_a?(Module)

    new_mixins = extension.public_instance_methods
    current_mixins = MOB_EXTENSIONS.map { |e| {e.to_s => e.public_instance_methods} }

    current_mixins.each do |ext, methods|
      methods.each {|m| fail "Extension #{ext} already provides method #{m}" if new_mixins.include?(m) }
    end

    Mob.include(extension)
  end
end