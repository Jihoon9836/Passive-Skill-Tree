package daripher.skilltree.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class CraftableEnchantment extends Enchantment {
  private static final EquipmentSlot[] APPLICABLE_SLOTS = {EquipmentSlot.HEAD};
  private final int maxLevel;

  public CraftableEnchantment(int maxLevel) {
    super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, APPLICABLE_SLOTS);
    this.maxLevel = maxLevel;
  }

  @Override
  public int getMaxLevel() {
    return this.maxLevel;
  }

  @Override
  public boolean isDiscoverable() {
    return false;
  }

  @Override
  public boolean isTradeable() {
    return false;
  }

  @Override
  public boolean isTreasureOnly() {
    return true;
  }

  @Override
  public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
    return false;
  }
}
