package daripher.skilltree.init;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.enchantment.DeepThoughtsEnchantment;
import daripher.skilltree.enchantment.RapidRecoveryEnchantment;
import daripher.skilltree.enchantment.MagicFlowEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSTEnchantments {
  public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SkillTreeMod.MOD_ID);

  public static final RegistryObject<Enchantment> DEEP_THOUGHTS = REGISTRY.register("deep_thoughts", DeepThoughtsEnchantment::new);
  public static final RegistryObject<Enchantment> MAGIC_FLOW = REGISTRY.register("magic_flow", MagicFlowEnchantment::new);
  public static final RegistryObject<Enchantment> RAPID_RECOVERY = REGISTRY.register("rapid_recovery", RapidRecoveryEnchantment::new);
}
