
# Adds an interception for the ItemOnItem message
add_interception :item_on_item do |used, target, reversible, block|
  interception = ItemOnItemPair.new(used, target)

  ITEM_PAIRS[interception] = block
  ITEM_PAIRS[interception.reverse] = block if reversible == :reversible
end

private

# A hash of ItemOnItemPairs to blocks.
ITEM_PAIRS = {}

# A pair of items that will cause a block to be executed if one (the 'used' item) is used on the other (the 'target' item).
class ItemOnItemPair
  attr_reader :used, :target

  def initialize(used, target)
    @used = used
    @target = target
  end

  # Returns a new ItemOnItemPair that is the reverse of this.
  def reverse
    return ItemOnItemPair.new(@target, @used)
  end

  def eql?(other)
    return (other.kind_of?(ItemOnItemPair) && @used == other.used && @target == other.target)
  end

  def hash
    return @used << 16 | @target
  end

end

# Adds a message handler to the item on item message.
on :message, :item_on_item do |ctx, player, message|
  used, target = message.id, message.target_id
  pair = ItemOnItemPair.new(used, target)
  block = ITEM_PAIRS[pair]

  block.call(ctx, player, message) unless block == nil
end