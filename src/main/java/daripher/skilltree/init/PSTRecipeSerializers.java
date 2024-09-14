package daripher.skilltree.init;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.recipe.*;
import daripher.skilltree.recipe.upgrade.ItemUpgradeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSTRecipeSerializers {
  public static final DeferredRegister<RecipeSerializer<?>> REGISTRY =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SkillTreeMod.MOD_ID);

  public static final RegistryObject<RecipeSerializer<?>> GEM_INSERTION =
      REGISTRY.register("gem_insertion", GemInsertionRecipe.Serializer::new);
  public static final RegistryObject<RecipeSerializer<?>> WEAPON_POISONING =
      REGISTRY.register("weapon_poisoning", WeaponPoisoningRecipe.Serializer::new);
  public static final RegistryObject<RecipeSerializer<?>> POTION_MIXING =
      REGISTRY.register("potion_mixing", MixtureRecipe.Serializer::new);
  public static final RegistryObject<RecipeSerializer<?>> SHAPED_CRAFTING =
      REGISTRY.register("crafting_shaped", ShapedSkillRequiringRecipe.Serializer::new);
  public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_CRAFTING =
      REGISTRY.register("crafting_shapeless", ShapelessSkillRequiringRecipe.Serializer::new);
  public static final RegistryObject<RecipeSerializer<?>> QUIVER_FILLING =
      REGISTRY.register("quiver_filling", QuiverFillingRecipe.Serializer::new);
  public static final RegistryObject<RecipeSerializer<?>> GEM_UPGRADE =
      REGISTRY.register("gem_upgrade", GemUpgradeRecipe.Serializer::new);
  public static final RegistryObject<RecipeSerializer<?>> ITEM_UPGRADE =
      REGISTRY.register("item_upgrade", ItemUpgradeRecipe.Serializer::new);
}
