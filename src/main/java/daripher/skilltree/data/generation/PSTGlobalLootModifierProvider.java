package daripher.skilltree.data.generation;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.PSTItems;
import daripher.skilltree.loot.modifier.AddItemModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class PSTGlobalLootModifierProvider extends GlobalLootModifierProvider {
  public PSTGlobalLootModifierProvider(DataGenerator generator) {
    super(generator.getPackOutput(), SkillTreeMod.MOD_ID);
  }

  @Override
  protected void start() {
    upgradeMaterial("entities/zombified_piglin", PSTItems.ANCIENT_ALLOY_GILDED);
    upgradeMaterial("entities/phantom", PSTItems.ANCIENT_ALLOY_LIGHTWEIGHT);
    upgradeMaterial("entities/creeper", PSTItems.ANCIENT_ALLOY_CURATIVE);
    upgradeMaterial("entities/spider", PSTItems.ANCIENT_ALLOY_TOXIC);
    upgradeMaterial("entities/wither_skeleton", PSTItems.ANCIENT_ALLOY_ENCHANTED);
    upgradeMaterial("entities/enderman", PSTItems.ANCIENT_ALLOY_SPATIAL);
    upgradeMaterial("entities/pillager", PSTItems.ANCIENT_ALLOY_DURABLE);
  }

  private void upgradeMaterial(String lootTablePath, RegistryObject<Item> item) {
    addItem(lootTablePath, item, 0.05f, 0.05f);
  }

  private void addItem(
      String lootTablePath, RegistryObject<Item> item, float chance, float lootingMultiplier) {
    String modifierName = lootTablePath.replaceAll("/", "_") + item.getId().getPath();
    add(
        modifierName,
        new AddItemModifier(
            new ItemStack(item.get()),
            lootTableCondition(lootTablePath),
            randomChanceCondition(chance, lootingMultiplier)));
  }

  private static LootItemCondition lootTableCondition(String lootTablePath) {
    ResourceLocation lootTableId = new ResourceLocation(lootTablePath);
    return LootTableIdCondition.builder(lootTableId).build();
  }

  @NotNull
  private static LootItemCondition randomChanceCondition(float chance, float lootingMultiplier) {
    return LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(
            chance, lootingMultiplier)
        .build();
  }
}
