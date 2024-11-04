package daripher.skilltree.enchantment;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.PSTEnchantmentCategories;
import daripher.skilltree.skill.bonus.SkillBonus;
import daripher.skilltree.skill.bonus.condition.damage.FireDamageCondition;
import daripher.skilltree.skill.bonus.event.BlockEventListener;
import daripher.skilltree.skill.bonus.player.IncomingHealingBonus;
import daripher.skilltree.skill.bonus.player.InflictDamageBonus;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SkillTreeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagmaticTouchEnchantment extends CraftableEnchantment implements SkillBonusEnchantment {
  public MagmaticTouchEnchantment() {
    super(3, PSTEnchantmentCategories.SHIELD, new EquipmentSlot[]{EquipmentSlot.OFFHAND});
  }

  @Override
  protected boolean checkCompatibility(@NotNull Enchantment other) {
    return true;
  }

  @Override
  public SkillBonus<?> getBonus(int enchantmentLevel) {
    return new InflictDamageBonus(1f, 2f, new BlockEventListener(), new FireDamageCondition());
  }
}
