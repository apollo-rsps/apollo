SPELLBOOKS = {}

def create_spellbook(identifier, interface_id:)
  SPELLBOOKS[interface_id] = identifier
end

create_spellbook :modern, interface_id: 192
