package daripher.skilltree.data.generation.translation;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.*;
import daripher.skilltree.skill.bonus.player.GainedExperienceBonus;
import daripher.skilltree.skill.bonus.player.LootDuplicationBonus;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class PSTEnglishTranslationProvider extends PSTTranslationProvider {
  public PSTEnglishTranslationProvider(DataGenerator gen) {
    super(gen, SkillTreeMod.MOD_ID, "en_us");
  }

  @Override
  protected void addTranslations() {
    // skills
    addSkill(1, "Adventurer");
    addSkill(98, "Hunter");
    addSkill(99, "Miner");
    addSkill(100, "Blacksmith");
    addSkill(101, "Cook");
    addSkill(102, "Enchanter");
    addSkill(103, "Alchemist");
    // skill bonuses
    add(PSTSkillBonuses.DAMAGE.get(), "Damage");
    add(PSTSkillBonuses.CRIT_DAMAGE.get(), "Critical Hit Damage");
    add(PSTSkillBonuses.CRIT_CHANCE.get(), "Critical Hit Chance");
    add(PSTSkillBonuses.BLOCK_BREAK_SPEED.get(), "Block break speed");
    add(PSTSkillBonuses.REPAIR_EFFICIENCY.get(), "Repaired %s: %s");
    add(PSTSkillBonuses.REPAIR_EFFICIENCY.get(), "bonus", "Durability restored");
    add(PSTSkillBonuses.FREE_ENCHANTMENT.get(), "Enchantments: %s");
    add(PSTSkillBonuses.FREE_ENCHANTMENT.get(), "bonus", "Free Enchantment chance");
    add(PSTSkillBonuses.RECIPE_UNLOCK.get(), "Unlocks Recipe: %s");
    add(PSTSkillBonuses.JUMP_HEIGHT.get(), "Jump Height");
    add(PSTSkillBonuses.INCOMING_HEALING.get(), "Incoming Healing");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "Chance to get %s %s");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "multiplier", "+%s%%");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "guaranteed", "You always get %s %s");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "double", "double");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "triple", "triple");
    add(PSTSkillBonuses.GAINED_EXPERIENCE.get(), "Experience from %s");
    add(PSTSkillBonuses.IGNITE.get(), "player", "You catch fire for %s");
    add(PSTSkillBonuses.IGNITE.get(), "player.chance", "Chance to catch fire for %s");
    add(PSTSkillBonuses.IGNITE.get(), "enemy", "Set enemies on fire for %s");
    add(PSTSkillBonuses.IGNITE.get(), "enemy.chance", "Chance to set enemies on fire for %s");
    add(PSTSkillBonuses.ARROW_RETRIEVAL.get(), "Arrow retrieval chance");
    add(PSTSkillBonuses.HEALTH_RESERVATION.get(), "Health Reservation");
    add(PSTSkillBonuses.ALL_ATTRIBUTES.get(), "All Attributes");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "player", "Gain %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "player.chance", "Chance to gain %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "enemy", "Inflict %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "enemy.chance", "Chance to inflict %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "seconds", " for %s seconds");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "minutes", " for %s minutes");
    add(PSTSkillBonuses.CANT_USE_ITEM.get(), "Can not use %s");
    add(PSTSkillBonuses.HEALING.get(), "player", "Recover %s life");
    add(PSTSkillBonuses.HEALING.get(), "player.chance", "Chance to recover %s life");
    add(PSTSkillBonuses.HEALING.get(), "enemy", "Enemies recover %s life");
    add(PSTSkillBonuses.HEALING.get(), "enemy.chance", "Chance for enemies to recover %s life");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "player", "Take %s damage");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "player.chance", "Chance to take %s damage");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "enemy", "Inflict %s damage");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "enemy.chance", "Chance to inflict %s damage");
    add(PSTSkillBonuses.CAN_POISON_ANYONE.get(), "Your poisons can affect any enemies");
    add(PSTSkillBonuses.LETHAL_POISON.get(), "Your poisons are lethal");
    add(PSTSkillBonuses.DAMAGE_TAKEN.get(), "%s taken");
    add(PSTSkillBonuses.DAMAGE_AVOIDANCE.get(), "Chance to avoid %s");
    // item bonuses
    add(PSTItemBonuses.SOCKETS.get(), "+%d Gem Sockets");
    add(PSTItemBonuses.DURABILITY.get(), "Durability");
    add(PSTItemBonuses.QUIVER_CAPACITY.get(), "Capacity");
    add(PSTItemBonuses.POTION_AMPLIFICATION.get(), "Amplification Chance");
    add(PSTItemBonuses.POTION_DURATION.get(), "Duration");
    add(PSTItemBonuses.FOOD_EFFECT.get(), "%s for %s");
    add(PSTItemBonuses.FOOD_SATURATION.get(), "Saturation");
    add(PSTItemBonuses.FOOD_HEALING.get(), "Restores %s Health");
    // experience sources
    add(GainedExperienceBonus.ExperienceSource.MOBS.getDescriptionId(), "Mobs");
    add(GainedExperienceBonus.ExperienceSource.ORE.getDescriptionId(), "Ores");
    add(GainedExperienceBonus.ExperienceSource.FISHING.getDescriptionId(), "Fishing");
    // loot conditions
    add(LootDuplicationBonus.LootType.MOBS.getDescriptionId(), "mobs loot");
    add(LootDuplicationBonus.LootType.FISHING.getDescriptionId(), "fishing loot");
    add(LootDuplicationBonus.LootType.GEMS.getDescriptionId(), "gems from ore");
    // living conditions
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "target.you", "you have");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "target.target", "target has");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "min.1", "%s if %s effects");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "min", "%s if %s at least %d effects");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "max", "%s if %s at most %d effects");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "range", "%s if %s %d to %d effects");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "target.you", "you have");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "target.target", "target has");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "min", "%s if %s at least %s%% health");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "max", "%s if %s at most %s%% health");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "range", "%s if %s %s%% to %s%% health");
    add(PSTLivingConditions.HAS_ITEM_EQUIPPED.get(), "target.you", "with");
    add(PSTLivingConditions.HAS_ITEM_EQUIPPED.get(), "target.target", "if target has");
    add(PSTLivingConditions.HAS_ITEM_EQUIPPED.get(), "%s %s %s equipped");
    add(PSTLivingConditions.HAS_GEMS.get(), "target.you", "you have");
    add(PSTLivingConditions.HAS_GEMS.get(), "target.target", "target has");
    add(PSTLivingConditions.HAS_GEMS.get(), "min.1", "%s if %s gems in %s");
    add(PSTLivingConditions.HAS_GEMS.get(), "min", "%s %s at least %d gems in %s");
    add(PSTLivingConditions.HAS_GEMS.get(), "max", "%s %s at most %d gems in %s");
    add(PSTLivingConditions.HAS_GEMS.get(), "range", "%s %s %d to %d gems in %s");
    add(PSTLivingConditions.HAS_EFFECT.get(), "target.you", "you are");
    add(PSTLivingConditions.HAS_EFFECT.get(), "target.target", "target is");
    add(PSTLivingConditions.HAS_EFFECT.get(), "%s if %s affected by %s");
    add(PSTLivingConditions.HAS_EFFECT.get(), "amplifier", "%s if %s affected by %s or higher");
    add(PSTLivingConditions.BURNING.get(), "target.you", "you are");
    add(PSTLivingConditions.BURNING.get(), "target.target", "target is");
    add(PSTLivingConditions.BURNING.get(), "%s if %s burning");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "target.you", "you have");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "target.target", "target has");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "min", "%s if %s at least %s %s");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "max", "%s if %s at most %s %s");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "range", "%s if %s %s%% to %s %s");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "target.you", "you have");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "target.target", "target has");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "min", "%s if %s at least %s Hunger points");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "max", "%s if %s at most %s Hunger points");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "range", "%s if %s %s%% to %s Hunger points");
    add(PSTLivingConditions.FISHING.get(), "target.you", "you are");
    add(PSTLivingConditions.FISHING.get(), "target.target", "target is");
    add(PSTLivingConditions.FISHING.get(), "%s if %s fishing");
    add(PSTLivingConditions.UNDERWATER.get(), "target.you", "you are");
    add(PSTLivingConditions.UNDERWATER.get(), "target.target", "target is");
    add(PSTLivingConditions.UNDERWATER.get(), "%s if %s under water");
    add(PSTLivingConditions.DUAL_WIELDING.get(), "target.you", "you have");
    add(PSTLivingConditions.DUAL_WIELDING.get(), "target.target", "target has");
    add(PSTLivingConditions.DUAL_WIELDING.get(), "%s if %s %s in both hands");
    add(PSTLivingConditions.HAS_ITEM_IN_HAND.get(), "target.you", "with");
    add(PSTLivingConditions.HAS_ITEM_IN_HAND.get(), "target.target", "if target has");
    add(PSTLivingConditions.HAS_ITEM_IN_HAND.get(), "%s %s %s in hand");
    add(PSTLivingConditions.CROUCHING.get(), "target.you", "while crouching");
    add(PSTLivingConditions.CROUCHING.get(), "target.target", "if target is crouching");
    add(PSTLivingConditions.CROUCHING.get(), "%s %s");
    add(PSTLivingConditions.UNARMED.get(), "target.you", "while unarmed");
    add(PSTLivingConditions.UNARMED.get(), "target.target", "if target is unarmed");
    add(PSTLivingConditions.UNARMED.get(), "%s %s");
    // event listeners
    add(PSTEventListeners.ATTACK.get(), "%s on hit");
    add(PSTEventListeners.ATTACK.get(), "damage", "%s on %s hit");
    add(PSTEventListeners.BLOCK.get(), "%s on block");
    add(PSTEventListeners.BLOCK.get(), "damage", "%s on %s block");
    add(PSTEventListeners.EVASION.get(), "%s on evasion");
    add(PSTEventListeners.ITEM_USED.get(), "%s on %s use");
    add(PSTEventListeners.DAMAGE_TAKEN.get(), "%s when you take %s");
    add(PSTEventListeners.ON_KILL.get(), "%s on kill");
    add(PSTEventListeners.ON_KILL.get(), "damage", "%s on %s kill");
    add(PSTEventListeners.SKILL_LEARNED.get(), "%s when you learn this");
    add(PSTEventListeners.SKILL_REMOVED.get(), "%s when this skill is removed");
    // damage conditions
    add(PSTDamageConditions.PROJECTILE.get(), "Projectile Damage");
    add(PSTDamageConditions.PROJECTILE.get(), "type", "Projectile");
    add(PSTDamageConditions.MELEE.get(), "Melee Damage");
    add(PSTDamageConditions.MELEE.get(), "type", "Melee");
    add(PSTDamageConditions.MAGIC.get(), "Magic Damage");
    add(PSTDamageConditions.MAGIC.get(), "type", "Magic");
    add(PSTDamageConditions.NONE.get(), "Damage");
    add(PSTDamageConditions.FALL.get(), "Fall Damage");
    add(PSTDamageConditions.FALL.get(),"type", "Fall");
    // enchantment conditions
    add(PSTEnchantmentConditions.WEAPON.get(), "Weapon Enchantments");
    add(PSTEnchantmentConditions.ARMOR.get(), "Armor Enchantments");
    add(PSTEnchantmentConditions.NONE.get(), "Enchantments");
    // item conditions
    add(PSTItemConditions.NONE.get(), "Item");
    add(PSTItemConditions.NONE.get(), "plural.type", "Items");
    add(PSTItemConditions.NONE.get(), "plural", "Items");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon", "Weapon");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon.plural.type", "Weapons");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon.plural", "Weapons");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "ranged_weapon", "Ranged Weapon");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "ranged_weapon.plural.type", "Ranged Weapons");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "ranged_weapon.plural", "Ranged Weapons");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow", "Bow");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow.plural.type", "Bows");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow.plural", "Bows");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow", "Crossbow");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow.plural.type", "Crossbows");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow.plural", "Crossbows");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "melee_weapon", "Melee Weapon");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "melee_weapon.plural.type", "Melee Weapons");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "melee_weapon.plural", "Melee Weapons");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword", "Sword");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword.plural.type", "Swords");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword.plural", "Swords");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident", "Trident");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident.plural.type", "Tridents");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident.plural", "Tridents");
    add(PSTTags.RINGS, "Ring");
    add(PSTTags.RINGS, "plural.type", "Rings");
    add(PSTTags.RINGS, "plural", "Rings");
    add(PSTTags.NECKLACES, "Necklace");
    add(PSTTags.NECKLACES, "plural.type", "Necklaces");
    add(PSTTags.NECKLACES, "plural", "Necklaces");
    add(PSTTags.QUIVERS, "Quiver");
    add(PSTTags.QUIVERS, "plural.type", "Quivers");
    add(PSTTags.QUIVERS, "plural", "Quivers");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "armor", "Armor");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet", "Helmet");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet.plural.type", "Helmets");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet.plural", "Helmets");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate", "Chestplate");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate.plural.type", "Chestplates");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate.plural", "Chestplates");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "leggings", "Leggings");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "boots", "Boots");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield", "Shield");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield.plural.type", "Shields");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield.plural", "Shields");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "any", "Equipment");
    add(PSTItemConditions.POTIONS.get(), "any", "Potion");
    add(PSTItemConditions.POTIONS.get(), "any.plural.type", "Potions");
    add(PSTItemConditions.POTIONS.get(), "any.plural", "Potions");
    add(PSTItemConditions.POTIONS.get(), "beneficial", "Beneficial Potion");
    add(PSTItemConditions.POTIONS.get(), "beneficial.plural.type", "Beneficial Potions");
    add(PSTItemConditions.POTIONS.get(), "beneficial.plural", "Beneficial Potions");
    add(PSTItemConditions.POTIONS.get(), "harmful", "Harmful Potion");
    add(PSTItemConditions.POTIONS.get(), "harmful.plural.type", "Harmful Potions");
    add(PSTItemConditions.POTIONS.get(), "harmful.plural", "Harmful Potions");
    add(PSTItemConditions.POTIONS.get(), "neutral", "Neutral Potion");
    add(PSTItemConditions.POTIONS.get(), "neutral.plural.type", "Neutral Potions");
    add(PSTItemConditions.POTIONS.get(), "neutral.plural", "Neutral Potions");
    add(PSTItemConditions.FOOD.get(), "Food");
    add(PSTTags.JEWELRY, "Jewelry");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool", "Tool");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool.plural.type", "Tools");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool.plural", "Tools");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe", "Axe");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe.plural.type", "Axes");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe.plural", "Axes");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe", "Hoe");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe.plural.type", "Hoes");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe.plural", "Hoes");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe", "Pickaxe");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe.plural.type", "Pickaxes");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe.plural", "Pickaxes");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel", "Shovel");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel.plural.type", "Shovels");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel.plural", "Shovels");
    add(PSTItemConditions.ENCHANTED.get(), "Enchanted %s");
    // skill multipliers
    add(PSTLivingMultipliers.EFFECT_AMOUNT.get(), "player", "%s per effect on you");
    add(PSTLivingMultipliers.EFFECT_AMOUNT.get(), "enemy", "%s per effect on enemies");
    add(PSTLivingMultipliers.ATTRIBUTE_VALUE.get(), "player", "%s per %s %s");
    add(PSTLivingMultipliers.ATTRIBUTE_VALUE.get(), "enemy", "%s per %s enemy's %s");
    add(PSTLivingMultipliers.ENCHANTS_AMOUNT.get(), "player", "%s per enchantment on %s");
    add(PSTLivingMultipliers.ENCHANTS_AMOUNT.get(), "enemy", "%s per enchantment on enemy's %s");
    add(PSTLivingMultipliers.ENCHANTS_LEVELS.get(), "player", "%s per enchantment level on %s");
    add(
        PSTLivingMultipliers.ENCHANTS_LEVELS.get(),
        "enemy",
        "%s per enchantment level on enemy's %s");
    add(PSTLivingMultipliers.GEMS_AMOUNT.get(), "player", "%s per gem in %s");
    add(PSTLivingMultipliers.GEMS_AMOUNT.get(), "enemy", "%s per gem in enemy's %s");
    add(PSTLivingMultipliers.FOOD_LEVEL.get(), "player", "%s per hunger point");
    add(PSTLivingMultipliers.FOOD_LEVEL.get(), "enemy", "%s per enemy's hunger point");
    add(
        PSTLivingMultipliers.DISTANCE_TO_TARGET.get(),
        "player",
        "%s per block between you and enemy");
    add(PSTLivingMultipliers.MISSING_HEALTH_POINTS.get(), "player", "%s per %s missing health");
    add(
        PSTLivingMultipliers.MISSING_HEALTH_POINTS.get(),
        "enemy",
        "%s per %s enemy's missing health");
    add(PSTLivingMultipliers.MISSING_HEALTH_PERCENTAGE.get(), "player", "%s per %s missing health");
    add(
        PSTLivingMultipliers.MISSING_HEALTH_PERCENTAGE.get(),
        "enemy",
        "%s per %s enemy's missing health");
    // recipes
    add("recipe.skilltree.weapon_poisoning", "Weapon Poisoning");
    add(
        "recipe.skilltree.weapon_poisoning.info",
        "(Combine a melee weapon and a harmful potion on a crafting bench to poison a weapon)");
    add("recipe.skilltree.potion_mixing", "Potion Mixing");
    add(
        "recipe.skilltree.potion_mixing.info",
        "(Combine two different potions on a crafting bench to create a mixture)");
    add("upgrade_recipe.chance", "Chance: %s%%");
    // potions info
    add("potion.superior", "Superior %s");
    add("item.minecraft.potion.mixture", "Mixture");
    add("item.minecraft.splash_potion.mixture", "Splash Mixture");
    add("item.minecraft.lingering_potion.mixture", "Lingering Mixture");
    addMixture("Diving", MobEffects.NIGHT_VISION, MobEffects.WATER_BREATHING);
    addMixture("Eternal Youth", MobEffects.HEAL, MobEffects.REGENERATION);
    addMixture("Sickness", MobEffects.POISON, MobEffects.WEAKNESS);
    addMixture("Owl", MobEffects.INVISIBILITY, MobEffects.NIGHT_VISION);
    addMixture("Coward", MobEffects.INVISIBILITY, MobEffects.MOVEMENT_SPEED);
    addMixture("Dragon Blood", MobEffects.FIRE_RESISTANCE, MobEffects.REGENERATION);
    addMixture("Demon", MobEffects.FIRE_RESISTANCE, MobEffects.DAMAGE_BOOST);
    addMixture("Assasin", MobEffects.HARM, MobEffects.POISON);
    addMixture("Antigravity", MobEffects.JUMP, MobEffects.SLOW_FALLING);
    addMixture("Aging", MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS);
    addMixture("Athlete", MobEffects.JUMP, MobEffects.MOVEMENT_SPEED);
    addMixture("Thief", MobEffects.INVISIBILITY, MobEffects.LUCK);
    addMixture("Treasure Hunter", MobEffects.LUCK, MobEffects.WATER_BREATHING);
    addMixture("Knight", MobEffects.REGENERATION, MobEffects.DAMAGE_BOOST);
    addMixture("Slow Motion", MobEffects.SLOW_FALLING, MobEffects.MOVEMENT_SLOWDOWN);
    addMixture("Soldier", MobEffects.HEAL, MobEffects.DAMAGE_BOOST);
    addMixture("Ninja", MobEffects.DAMAGE_BOOST, MobEffects.MOVEMENT_SPEED);
    addMixture("Blessing", MobEffects.LUCK, MobEffects.DAMAGE_BOOST);
    addMixture("Plague", MobEffects.POISON, MobEffects.MOVEMENT_SLOWDOWN);
    // gems info
    add("gem.socket", "Empty Socket");
    add("gem.additional_socket_1", "• Has an additional socket");
    add("gem.disabled", "Disabled with Apotheosis adventure module enabled");
    add("gem_class_format", "• %s: %s");
    add("gem.tooltip", "• Can be inserted into items with sockets");
    add("gem_bonus.removal", "Destroys gems in the item");
    add("gem_bonus.random", "Outcome unpredictable");
    // weapon info
    add("weapon.poisoned", "Poisoned:");
    // quiver info
    add("quiver.capacity", "• Can hold up to %s arrows");
    add("quiver.contents", "• Contents: %s");
    // items
    add("item.cant_use.info", "You can not use this");
    addGem("citrine", "Citrine");
    addGem("ruby", "Ruby");
    addGem("sapphire", "Sapphire");
    addGem("jade", "Jade");
    addGem("iriscite", "Iriscite");
    addGem("vacucite", "Vacucite");
    add(PSTItems.WISDOM_SCROLL.get(), "Wisdom Scroll");
    add(PSTItems.AMNESIA_SCROLL.get(), "Amnesia Scroll");
    add(PSTItems.COPPER_RING.get(), "Copper Ring");
    add(PSTItems.IRON_RING.get(), "Iron Ring");
    add(PSTItems.GOLDEN_RING.get(), "Golden Ring");
    add(PSTItems.COPPER_NUGGET.get(), "Copper Nugget");
    add(PSTItems.ASSASSIN_NECKLACE.get(), "Assassin's Necklace");
    add(PSTItems.HEALER_NECKLACE.get(), "Healer's Necklace");
    add(PSTItems.TRAVELER_NECKLACE.get(), "Traveler's Necklace");
    add(PSTItems.SIMPLE_NECKLACE.get(), "Simple Necklace");
    add(PSTItems.SCHOLAR_NECKLACE.get(), "Scholar's Necklace");
    add(PSTItems.ARSONIST_NECKLACE.get(), "Arsonist's Necklace");
    add(PSTItems.FISHERMAN_NECKLACE.get(), "Fisherman's Necklace");
    add(PSTItems.ANCIENT_ALLOY_GILDED.get(), "Gilded Ancient Alloy");
    add(PSTItems.ANCIENT_ALLOY_LIGHTWEIGHT.get(), "Lightweight Ancient Alloy");
    add(PSTItems.ANCIENT_ALLOY_CURATIVE.get(), "Curative Ancient Alloy");
    add(PSTItems.ANCIENT_ALLOY_TOXIC.get(), "Toxic Ancient Alloy");
    add(PSTItems.ANCIENT_ALLOY_ENCHANTED.get(), "Enchanted Ancient Alloy");
    add(PSTItems.ANCIENT_ALLOY_SPATIAL.get(), "Spatial Ancient Alloy");
    addTooltip(PSTItems.WISDOM_SCROLL.get(), "Grants one passive skill point");
    addTooltip(PSTItems.AMNESIA_SCROLL.get(), "Resets your passive skill tree");
    addWarning(PSTItems.AMNESIA_SCROLL.get(), "%d%% of your skill points will be lost");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_GILDED.get(),
        "Allows improving loot-related attributes of certain items");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_LIGHTWEIGHT.get(),
        "Allows improving speed attributes of certain items");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_CURATIVE.get(),
        "Allows improving healing attributes of certain items");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_TOXIC.get(), "Allows improving poison attributes of certain items");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_ENCHANTED.get(),
        "Allows improving magic attributes of certain items");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_SPATIAL.get(), "Allows improving socket number of certain items");
    add("ancient_material.tooltip", "Requires certain knowledge to be used");
    // slots
    addCurioSlot("ring", "Ring Slot");
    addCurioSlot("ring", "plural", "Ring Slots");
    addCurioSlot("necklace", "Necklace Slot");
    addCurioSlot("necklace", "plural", "Necklace Slots");
    // attributes
    add(PSTAttributes.REGENERATION.get(), "Life Regeneration");
    add(PSTAttributes.EXP_PER_MINUTE.get(), "Experience Per Minute");
    add(PSTAttributes.POISON_DAMAGE.get(), "Poison Damage");
    add(PSTAttributes.DEXTERITY.get(), "Dexterity");
    addInfo(PSTAttributes.DEXTERITY.get(), "Gain 1% Projectile Damage per Dexterity point");
    add(PSTAttributes.STRENGTH.get(), "Strength");
    addInfo(PSTAttributes.STRENGTH.get(), "Gain 1% Melee Damage per Strength point");
    add(PSTAttributes.INTELLIGENCE.get(), "Intelligence");
    addInfo(PSTAttributes.INTELLIGENCE.get(), "Gain 1% Magic Damage per Intelligence point");
    add(PSTAttributes.VITALITY.get(), "Vitality");
    addInfo(PSTAttributes.VITALITY.get(), "Gain 1% Max Health per Vitality point");
    // effects
    add(PSTEffects.CRIT_DAMAGE_BONUS.get(), "Critical Damage");
    add(PSTEffects.DAMAGE_BONUS.get(), "Damage");
    add(PSTEffects.LIFE_REGENERATION_BONUS.get(), "Life Regeneration");
    // system messages
    add("skilltree.message.reset", "Skill Tree has changed. Your skill points have been restored.");
    add("skilltree.message.reset_command", "Your skill tree has been reset.");
    add("skilltree.message.point_command", "Skill point gained.");
    // screen info
    add("widget.skill_points_left", "Points left: %s");
    add("widget.skill_button.not_learned", "Skill not learned");
    add("widget.buy_skill_button", "Buy Skill Point");
    add("widget.skill_button.multiple_bonuses", "%s and %s");
    add("widget.confirm_button", "Confirm");
    add("widget.cancel_button", "Cancel");
    add("widget.show_stats", "Show Stats");
    add("key.categories.skilltree", "Passive Skill Tree");
    add("key.display_skill_tree", "Open Skill Tree");
    add("skill.limitation", "Limited to: %s");
    // jei info
    add(
        "skilltree.jei.gem_info",
        "Gems can be inserted into items with sockets on a smithing table. Drop from any ore with"
            + " a small chance (requires no silk touch tool).");
    // curios info
    add("curios.identifier.quiver", "Quiver");
    add("curios.modifiers.quiver", "When worn:");
    // tabs
    add("itemGroup.skilltree", "Passive Skill Tree");
    // misc
    add("item.modifiers.both_hands", "When Held:");
    // apotheosis compatibility
    add("text.apotheosis.category.curios:ring.plural", "Rings");
    add("text.apotheosis.category.curios:necklace.plural", "Necklaces");
    add("gem_class.jewelry", "Jewelry");
    // affix names
    add("affix.skilltree:jewelry/dmg_reduction/tempered", "Tempered");
    add("affix.skilltree:jewelry/dmg_reduction/tempered.suffix", "of Hardening");
    add("affix.skilltree:jewelry/attribute/immortal", "Immortal");
    add("affix.skilltree:jewelry/attribute/immortal.suffix", "of Immortality");
    add("affix.skilltree:jewelry/attribute/experienced", "Experienced");
    add("affix.skilltree:jewelry/attribute/experienced.suffix", "of Experience");
    add("affix.skilltree:jewelry/attribute/lucky", "Lucky");
    add("affix.skilltree:jewelry/attribute/lucky.suffix", "of Luck");
    add("affix.skilltree:jewelry/attribute/hasty", "Hasty");
    add("affix.skilltree:jewelry/attribute/hasty.suffix", "of Haste");
    add("affix.skilltree:jewelry/attribute/hidden", "Hidden");
    add("affix.skilltree:jewelry/attribute/hidden.suffix", "of Hiding");
    add("affix.skilltree:jewelry/attribute/healthy", "Healthy");
    add("affix.skilltree:jewelry/attribute/healthy.suffix", "of Health");
  }

  protected void addMixture(String name, MobEffect... effects) {
    name = "Mixture of " + name;
    addMixture(name, "potion", effects);
    addMixture("Splash " + name, "splash_potion", effects);
    addMixture("Lingering " + name, "lingering_potion", effects);
  }

  protected void addGem(String type, String name) {
    super.addGem(type, name, "Crumbled", "Broken", "Low-Quality", "Big", "Rare", "Exceptional");
  }
}
