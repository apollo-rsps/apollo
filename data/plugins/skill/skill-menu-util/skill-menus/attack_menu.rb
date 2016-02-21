java_import 'org.apollo.game.model.entity.Skill'

create_menu Skill::ATTACK do
  tab "Bronze" do
    register_item name: :bronze_dagger, level: 1
  end
  
  tab "Iron" do
    register_item name: :iron_dagger, level: 5
    register_item name: :iron_sword, level: 5
  end
  
  tab "Mithril" do
    register_item name: :mithril_dagger, level: 10
  end
  
  tab "Adamant"
  tab "Rune"
  tab "Dragon"
  
end