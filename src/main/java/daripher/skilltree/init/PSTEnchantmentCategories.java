package daripher.skilltree.init;

import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.Tags;

public class PSTEnchantmentCategories {
  public static EnchantmentCategory SHIELD = EnchantmentCategory.create("shield", item -> item.getDefaultInstance()
      .is(Tags.Items.TOOLS_SHIELDS) || item instanceof ShieldItem || item.getUseAnimation(item.getDefaultInstance()) == UseAnim.BLOCK);
}
