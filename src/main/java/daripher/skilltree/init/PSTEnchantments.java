package daripher.skilltree.init;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.enchantment.CraftableEnchantment;
import daripher.skilltree.enchantment.DeepThoughtsEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSTEnchantments {
  public static final DeferredRegister<Enchantment> REGISTRY =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SkillTreeMod.MOD_ID);

  public static final RegistryObject<Enchantment> DEEP_THOUGHTS =
      REGISTRY.register("deep_thoughts", DeepThoughtsEnchantment::new);
}
