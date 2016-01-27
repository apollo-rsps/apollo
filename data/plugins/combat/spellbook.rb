SPELLBOOKS = {}

def create_spellbook(identifier, interface_id:)
  SPELLBOOKS[interface_id] = identifier
end

def spellbook_for(interface_id)
  fail "Could not find spellbook for #{interface_id}" unless SPELLBOOKS.has_key?(interface_id)
  
  return SPELLBOOKS[interface_id]
end