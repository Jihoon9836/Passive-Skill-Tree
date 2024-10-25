package daripher.skilltree.enchantment;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.PSTEnchantments;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SkillTreeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeepThoughtsEnchantment extends CraftableEnchantment {
  public DeepThoughtsEnchantment() {
    super(1);
  }

  @Override
  protected boolean checkCompatibility(@NotNull Enchantment other) {
    return other != Enchantments.MENDING;
  }

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    Player player = event.player;
    if (player.level().isClientSide) return;
    int enchantmentLevel =
        EnchantmentHelper.getEnchantmentLevel(PSTEnchantments.DEEP_THOUGHTS.get(), player);
    if (enchantmentLevel == 0) return;
    // 20 exp per minute (1200 / 20)
    int frequency = 60;
    if (player.tickCount % frequency != 0) return;
    ExperienceOrb expOrb =
        new ExperienceOrb(player.level(), player.getX(), player.getY(), player.getZ(), 1);
    player.level().addFreshEntity(expOrb);
  }
}
