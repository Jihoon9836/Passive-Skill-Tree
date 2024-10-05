package daripher.skilltree.data.generation.translation;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.*;
import daripher.skilltree.skill.bonus.player.GainedExperienceBonus;
import daripher.skilltree.skill.bonus.player.LootDuplicationBonus;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class PSTRussianTranslationProvider extends PSTTranslationProvider {
  public PSTRussianTranslationProvider(DataGenerator gen) {
    super(gen, SkillTreeMod.MOD_ID, "ru_ru");
  }

  @Override
  protected void addTranslations() {
    // skill bonuses
    add(PSTSkillBonuses.DAMAGE.get(), "Урон");
    add(PSTSkillBonuses.CRIT_DAMAGE.get(), "Критический урон");
    add(PSTSkillBonuses.CRIT_CHANCE.get(), "Шанс критического удара");
    add(PSTSkillBonuses.BLOCK_BREAK_SPEED.get(), "Скорость добычи блоков");
    add(PSTSkillBonuses.REPAIR_EFFICIENCY.get(), "Ремонтируем%s: %s");
    add(PSTSkillBonuses.REPAIR_EFFICIENCY.get(), "bonus", "Прочности восстановлено");
    add(PSTSkillBonuses.FREE_ENCHANTMENT.get(), "Зачарование: %s");
    add(PSTSkillBonuses.FREE_ENCHANTMENT.get(), "bonus", "Шанс бесплатного зачарование");
    add(PSTSkillBonuses.RECIPE_UNLOCK.get(), "Открывает рецепт: %s");
    add(PSTSkillBonuses.JUMP_HEIGHT.get(), "Высота прыжка");
    add(PSTSkillBonuses.INCOMING_HEALING.get(), "Получаемое лечение");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "Шанс получить %s %s");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "multiplier", "+%s%%");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "guaranteed", "Вы всегда получаете %s %s");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "double", "двойные");
    add(PSTSkillBonuses.LOOT_DUPLICATION.get(), "triple", "тройные");
    add(PSTSkillBonuses.GAINED_EXPERIENCE.get(), "Опыт %s");
    add(PSTSkillBonuses.IGNITE.get(), "player", "Вы загораетесь на %s");
    add(PSTSkillBonuses.IGNITE.get(), "player.chance", "Шанс загореться на %s");
    add(PSTSkillBonuses.IGNITE.get(), "enemy", "Вы поджигаете врагов на %s");
    add(PSTSkillBonuses.IGNITE.get(), "enemy.chance", "Шанс поджечь врагов на %s");
    add(PSTSkillBonuses.ARROW_RETRIEVAL.get(), "Шанс вернуть стрелы");
    add(PSTSkillBonuses.HEALTH_RESERVATION.get(), "Удержание здоровья");
    add(PSTSkillBonuses.ALL_ATTRIBUTES.get(), "Все характеристики");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "player", "Вы получаете %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "player.chance", "Шанс получить %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "enemy", "Вы накладываете %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "enemy.chance", "Шанс наложить %s%s");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "seconds", " на %s секунд");
    add(PSTSkillBonuses.MOB_EFFECT.get(), "minutes", " на %s минут");
    add(PSTSkillBonuses.CANT_USE_ITEM.get(), "Нельзя использовать %s");
    add(PSTSkillBonuses.HEALING.get(), "player", "Вы восстанавливаете %s здоровья");
    add(PSTSkillBonuses.HEALING.get(), "player.chance", "Шанс восстановить %s здоровья");
    add(PSTSkillBonuses.HEALING.get(), "enemy", "Враги восстанавливают %s здоровья");
    add(PSTSkillBonuses.HEALING.get(), "enemy.chance", "Шанс для врагов восстановить %s здоровья");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "player", "Вы получаете %s урона");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "player.chance", "Шанс получить %s урона");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "enemy", "Вы наносите %s урона");
    add(PSTSkillBonuses.INFLICT_DAMAGE.get(), "enemy.chance", "Шанс нанести %s урона");
    add(PSTSkillBonuses.CAN_POISON_ANYONE.get(), "Ваши яды действуют на любых врагов");
    add(PSTSkillBonuses.LETHAL_POISON.get(), "Ваши яды летальны");
    add(PSTSkillBonuses.DAMAGE_TAKEN.get(), "Получаемый %s");
    add(PSTSkillBonuses.DAMAGE_AVOIDANCE.get(), "Шанс избежать %s");
    // item bonuses
    add(PSTItemBonuses.SOCKETS.get(), "+%d Гнезда для Самоцветов");
    add(PSTItemBonuses.DURABILITY.get(), "Прочность");
    add(PSTItemBonuses.QUIVER_CAPACITY.get(), "Вместимость");
    add(PSTItemBonuses.POTION_AMPLIFICATION.get(), "Шанс Усиления");
    add(PSTItemBonuses.POTION_DURATION.get(), "Длительность");
    add(PSTItemBonuses.FOOD_EFFECT.get(), "%s на %s");
    add(PSTItemBonuses.FOOD_SATURATION.get(), "Насыщение");
    add(PSTItemBonuses.FOOD_HEALING.get(), "Восстанавливает %s Здоровья");
    // experience sources
    add(GainedExperienceBonus.ExperienceSource.MOBS.getDescriptionId(), "с Существ");
    add(GainedExperienceBonus.ExperienceSource.ORE.getDescriptionId(), "из Руды");
    add(GainedExperienceBonus.ExperienceSource.FISHING.getDescriptionId(), "за Рыбалку");
    // loot conditions
    add(LootDuplicationBonus.LootType.MOBS.getDescriptionId(), "награды с существ");
    add(LootDuplicationBonus.LootType.FISHING.getDescriptionId(), "награды с рыбалки");
    add(LootDuplicationBonus.LootType.GEMS.getDescriptionId(), "самоцветы из руды");
    // living conditions
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "target.you", "вас");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "target.target", "цели");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "min.1", "%s если на %s есть Эффекты");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "min", "%s если на %s минимум %d Эффектов");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "max", "%s если на %s максимум %d Эффектов");
    add(PSTLivingConditions.EFFECT_AMOUNT.get(), "range", "%s если на %s от %d до %d Эффектов");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "target.you", "если у вас");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "target.target", "если у цели");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "min", "%s %s минимум %s%% здоровья");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "max", "%s %s максимум %s%% здоровья");
    add(PSTLivingConditions.HEALTH_PERCENTAGE.get(), "range", "%s %s от %s%% до %s%% здоровья");
    add(PSTLivingConditions.HAS_ITEM_EQUIPPED.get(), "target.you", "вас");
    add(PSTLivingConditions.HAS_ITEM_EQUIPPED.get(), "target.target", "цели");
    add(PSTLivingConditions.HAS_ITEM_EQUIPPED.get(), "%s если на %s %s");
    add(PSTLivingConditions.HAS_GEMS.get(), "target.you", "если у вас есть");
    add(PSTLivingConditions.HAS_GEMS.get(), "target.target", "если у цели есть");
    add(PSTLivingConditions.HAS_GEMS.get(), "min.1", "%s %s самоцветы в %s");
    add(PSTLivingConditions.HAS_GEMS.get(), "min", "%s %s минимум %d самоцветов в %s");
    add(PSTLivingConditions.HAS_GEMS.get(), "max", "%s %s максимум %d самоцветов в %s");
    add(PSTLivingConditions.HAS_GEMS.get(), "range", "%s %s от %d до %d самоцветов в %s");
    add(PSTLivingConditions.HAS_EFFECT.get(), "target.you", "вас");
    add(PSTLivingConditions.HAS_EFFECT.get(), "target.target", "цель");
    add(PSTLivingConditions.HAS_EFFECT.get(), "%s если на %s действует %s");
    add(PSTLivingConditions.HAS_EFFECT.get(), "amplifier", "%s если на %s действует %s или выше");
    add(PSTLivingConditions.BURNING.get(), "target.you", "вы горите");
    add(PSTLivingConditions.BURNING.get(), "target.target", "цель горит");
    add(PSTLivingConditions.BURNING.get(), "%s если %s");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "target.you", "у вас");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "target.target", "у цели");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "min", "%s если %s минимум %s %s");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "max", "%s если %s максимум %s %s");
    add(PSTLivingConditions.ATTRIBUTE_VALUE.get(), "range", "%s если %s от %s%% до %s %s");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "target.you", "у вас");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "target.target", "у цели");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "min", "%s если %s минимум %s очков голода");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "max", "%s если %s максимум %s очков голода");
    add(PSTLivingConditions.FOOD_LEVEL.get(), "range", "%s если %s от %s%% до %s очков голода");
    add(PSTLivingConditions.FISHING.get(), "target.you", "вы рыбачите");
    add(PSTLivingConditions.FISHING.get(), "target.target", "цель рыбачит");
    add(PSTLivingConditions.FISHING.get(), "%s если %s");
    add(PSTLivingConditions.UNDERWATER.get(), "target.you", "вы");
    add(PSTLivingConditions.UNDERWATER.get(), "target.target", "цель");
    add(PSTLivingConditions.UNDERWATER.get(), "%s если %s под водой");
    add(PSTLivingConditions.DUAL_WIELDING.get(), "target.you", "вы держите");
    add(PSTLivingConditions.DUAL_WIELDING.get(), "target.target", "цель держит");
    add(PSTLivingConditions.DUAL_WIELDING.get(), "%s если %s %s в обеих руках");
    add(PSTLivingConditions.HAS_ITEM_IN_HAND.get(), "target.you", "вас");
    add(PSTLivingConditions.HAS_ITEM_IN_HAND.get(), "target.target", "цели");
    add(PSTLivingConditions.HAS_ITEM_IN_HAND.get(), "%s если у %s в руке %s");
    add(PSTLivingConditions.CROUCHING.get(), "target.you", "вы крадётесь");
    add(PSTLivingConditions.CROUCHING.get(), "target.target", "цель крадется");
    add(PSTLivingConditions.CROUCHING.get(), "%s если %s");
    add(PSTLivingConditions.UNARMED.get(), "target.you", "без оружия");
    add(PSTLivingConditions.UNARMED.get(), "target.target", "если цель безоружна");
    add(PSTLivingConditions.UNARMED.get(), "%s %s");
    // event listeners
    add(PSTEventListeners.ATTACK.get(), "%s при атаке");
    add(PSTEventListeners.ATTACK.get(), "damage", "%s при атаке %s");
    add(PSTEventListeners.BLOCK.get(), "%s при блоке");
    add(PSTEventListeners.BLOCK.get(), "damage", "%s при блоке %s");
    add(PSTEventListeners.EVASION.get(), "%s при уклонении");
    add(PSTEventListeners.ITEM_USED.get(), "%s когда вы используете %s");
    add(PSTEventListeners.DAMAGE_TAKEN.get(), "%s когда вы получаете %s");
    add(PSTEventListeners.ON_KILL.get(), "%s при убийстве");
    add(PSTEventListeners.ON_KILL.get(), "damage", "%s при убийстве %s");
    add(PSTEventListeners.SKILL_LEARNED.get(), "%s при изучении этого умения");
    add(PSTEventListeners.SKILL_REMOVED.get(), "%s когда вы забудете это умение");
    // damage conditions
    add(PSTDamageConditions.PROJECTILE.get(), "Урон снарядами");
    add(PSTDamageConditions.PROJECTILE.get(), "type", "снарядами");
    add(PSTDamageConditions.PROJECTILE.get(), "type.blocked", "снаряда");
    add(PSTDamageConditions.MELEE.get(), "Урон в ближнем бою");
    add(PSTDamageConditions.MELEE.get(), "type", "в ближнем бою");
    add(PSTDamageConditions.MAGIC.get(), "Урон магией");
    add(PSTDamageConditions.MAGIC.get(), "type", "магией");
    add(PSTDamageConditions.MAGIC.get(), "type.blocked", "магии");
    add(PSTDamageConditions.NONE.get(), "Урон");
    add(PSTDamageConditions.FALL.get(), "Урон от падения");
    add(PSTDamageConditions.FALL.get(),"type", "от падения");
    // enchantment conditions
    add(PSTEnchantmentConditions.WEAPON.get(), "Зачарование оружия");
    add(PSTEnchantmentConditions.ARMOR.get(), "Зачарование брони");
    add(PSTEnchantmentConditions.NONE.get(), "Зачарование");
    // item conditions
    add(PSTItemConditions.NONE.get(), "Предмет");
    add(PSTItemConditions.NONE.get(), "where", "Предмете");
    add(PSTItemConditions.NONE.get(), "type", "ый Предмет");
    add(PSTItemConditions.NONE.get(), "plural.type", "ые Предметы");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon", "Оружие");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon.where", "Оружии");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon.type", "ое Оружие");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon.plural.type", "ое Оружие");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "weapon.plural", "Оружие");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "ranged_weapon", "Оружие дальнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "ranged_weapon.where", "Оружии дальнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "ranged_weapon.type", "ое Оружие дальнего боя");
    add(
        PSTItemConditions.EQUIPMENT_TYPE.get(),
        "ranged_weapon.plural.type",
        "ое Оружие дальнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "ranged_weapon.plural", "Оружие дальнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow", "Лук");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow.where", "Луке");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow.type", "ый Лук");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow.plural.type", "ые Луки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "bow.plural", "Луки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow", "Арбалет");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow.where", "Арбалете");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow.type", "ый Арбалет");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow.plural.type", "ые Арбалеты");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "crossbow.plural", "Арбалеты");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "melee_weapon", "Оружие ближнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "melee_weapon.where", "Оружии ближнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "melee_weapon.type", "ое Оружие ближнего боя");
    add(
        PSTItemConditions.EQUIPMENT_TYPE.get(),
        "melee_weapon.plural.type",
        "ое Оружие ближнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "melee_weapon.plural", "Оружие ближнего боя");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword", "Меч");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword.where", "Мече");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword.type", "ый Меч");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword.plural.type", "ые Мечи");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "sword.plural", "Мечи");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident", "Трезубец");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident.where", "Трезубце");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident.type", "ый Трезубец");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident.plural.type", "ые Трезубцы");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "trident.plural", "Трезубцы");
    add(PSTTags.RINGS, "Кольцо");
    add(PSTTags.RINGS, "where", "Кольцах");
    add(PSTTags.RINGS, "type", "ое Кольцо");
    add(PSTTags.RINGS, "plural.type", "ые Кольца");
    add(PSTTags.RINGS, "plural", "Кольца");
    add(PSTTags.NECKLACES, "Ожерелье");
    add(PSTTags.NECKLACES, "type", "ое Ожерелье");
    add(PSTTags.NECKLACES, "plural.type", "ые Ожерелья");
    add(PSTTags.NECKLACES, "plural", "Ожерелья");
    add(PSTTags.QUIVERS, "Колчан");
    add(PSTTags.QUIVERS, "where", "Колчане");
    add(PSTTags.QUIVERS, "type", "ый Колчан");
    add(PSTTags.QUIVERS, "plural.type", "ые Колчаны");
    add(PSTTags.QUIVERS, "plural", "Колчаны");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "armor", "Броня");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "armor.where", "Броне");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "armor.type", "ая Броня");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "armor.plural.type", "ая Броня");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "armor.plural", "Броня");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet", "Шлем");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet.where", "Шлеме");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet.type", "ый Шлем");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet.plural.type", "ые Шлемы");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "helmet.plural", "Шлемы");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate", "Нагрудник");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate.where", "Нагруднике");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate.type", "ый Нагрудник");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate.plural.type", "ые Нагрудники");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "chestplate.plural", "Нагрудники");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "leggings", "Штаны");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "leggings.where", "Штанах");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "leggings.type", "ые Штаны");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "leggings.plural.type", "ые Штаны");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "leggings.plural", "Штаны");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "boots", "Ботинки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "boots.where", "Ботинках");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "boots.type", "ые Ботинки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "boots.plural.type", "ые Ботинки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "boots.plural", "Ботинки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield", "Щит");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield.where", "Щите");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield.type", "ый Щит");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield.plural.type", "ые Щиты");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shield.plural", "Щиты");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "any", "Экипировка");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "any.where", "Экипировке");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "any.type", "ая Экипировка");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "any.plural.type", "ая Экипировка");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "any.plural", "Экипировка");
    add(PSTItemConditions.POTIONS.get(), "any", "Зелье");
    add(PSTItemConditions.POTIONS.get(), "any.type", "ое Зелье");
    add(PSTItemConditions.POTIONS.get(), "any.plural.type", "ые Зелья");
    add(PSTItemConditions.POTIONS.get(), "any.plural", "Зелья");
    add(PSTItemConditions.POTIONS.get(), "beneficial", "Благотворное Зелье");
    add(PSTItemConditions.POTIONS.get(), "beneficial.type", "ое Благотворное Зелье");
    add(PSTItemConditions.POTIONS.get(), "beneficial.plural.type", "ые Благотворные Зелья");
    add(PSTItemConditions.POTIONS.get(), "beneficial.plural", "Благотворные Зелья");
    add(PSTItemConditions.POTIONS.get(), "harmful", "Вредящее Зелье");
    add(PSTItemConditions.POTIONS.get(), "harmful.type", "ое Вредящее Зелье");
    add(PSTItemConditions.POTIONS.get(), "harmful.plural.type", "ые Вредящие Зелья");
    add(PSTItemConditions.POTIONS.get(), "harmful.plural", "Вредящие Зелья");
    add(PSTItemConditions.POTIONS.get(), "neutral", "Нейтральное Зелье");
    add(PSTItemConditions.POTIONS.get(), "neutral.type", "ое Нейтральное Зелье");
    add(PSTItemConditions.POTIONS.get(), "neutral.plural.type", "ые Нейтральные Зелья");
    add(PSTItemConditions.POTIONS.get(), "neutral.plural", "Нейтральные Зелья");
    add(PSTItemConditions.FOOD.get(), "Еда");
    add(PSTItemConditions.FOOD.get(), "type", "ая Еда");
    add(PSTItemConditions.FOOD.get(), "plural.type", "ая Еда");
    add(PSTItemConditions.FOOD.get(), "plural", "Еда");
    add(PSTTags.JEWELRY, "Бижутерия");
    add(PSTTags.JEWELRY, "where", "Бижутерии");
    add(PSTTags.JEWELRY, "type", "ая Бижутерия");
    add(PSTTags.JEWELRY, "plural.type", "ая Бижутерия");
    add(PSTTags.JEWELRY, "plural", "Бижутерия");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool", "Инструмент");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool.where", "Инструменте");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool.type", "ый Инструмент");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool.plural.type", "ые Инструменты");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "tool.plural", "Инструменты");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe", "Топор");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe.where", "Топоре");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe.type", "ый Топор");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe.plural.type", "ые Топоры");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "axe.plural", "Топоры");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe", "Мотыга");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe.where", "Мотыге");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe.type", "ая Мотыга");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe.plural.type", "ые Мотыги");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "hoe.plural", "Мотыги");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe", "Кирка");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe.where", "Кирке");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe.type", "ая Кирка");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe.plural.type", "ые Кирки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "pickaxe.plural", "Кирки");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel", "Лопата");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel.where", "Лопате");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel.type", "ая Лопата");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel.plural.type", "ые Лопаты");
    add(PSTItemConditions.EQUIPMENT_TYPE.get(), "shovel.plural", "Лопаты");
    add(PSTItemConditions.ENCHANTED.get(), "Зачарованн%s");
    // skill multipliers
    add(PSTLivingMultipliers.EFFECT_AMOUNT.get(), "player", "%s за каждый эффект");
    add(PSTLivingMultipliers.EFFECT_AMOUNT.get(), "enemy", "%s за каждый эффект врага");
    add(PSTLivingMultipliers.ATTRIBUTE_VALUE.get(), "player", "%s за каждые %s %s");
    add(PSTLivingMultipliers.ATTRIBUTE_VALUE.get(), "enemy", "%s за каждые %s %s врага");
    add(PSTLivingMultipliers.ENCHANTS_AMOUNT.get(), "player", "%s за каждое зачарование на %s");
    add(
        PSTLivingMultipliers.ENCHANTS_AMOUNT.get(),
        "enemy",
        "%s за каждое зачарование на %s врага");
    add(
        PSTLivingMultipliers.ENCHANTS_LEVELS.get(),
        "player",
        "%s за каждый уровень зачарований на %s");
    add(
        PSTLivingMultipliers.ENCHANTS_LEVELS.get(),
        "enemy",
        "%s за каждый уровень зачарований на %s врага");
    add(PSTLivingMultipliers.GEMS_AMOUNT.get(), "player", "%s за каждый Самоцвет в %s");
    add(PSTLivingMultipliers.GEMS_AMOUNT.get(), "enemy", "%s за каждый Самоцвет в %s врага");
    add(PSTLivingMultipliers.FOOD_LEVEL.get(), "player", "%s за каждую единицу Голода");
    add(PSTLivingMultipliers.FOOD_LEVEL.get(), "enemy", "%s за каждую единицу Голода врага");
    add(PSTLivingMultipliers.DISTANCE_TO_TARGET.get(), "player", "%s за каждый блок до врага");
    add(
        PSTLivingMultipliers.MISSING_HEALTH_POINTS.get(),
        "player",
        "%s за каждые %s недостающего здоровья");
    add(
        PSTLivingMultipliers.MISSING_HEALTH_POINTS.get(),
        "enemy",
        "%s за каждые %s недостающего здоровья врага");
    add(
        PSTLivingMultipliers.MISSING_HEALTH_PERCENTAGE.get(),
        "player",
        "%s за каждые %s недостающего здоровья");
    add(
        PSTLivingMultipliers.MISSING_HEALTH_PERCENTAGE.get(),
        "enemy",
        "%s за каждые %s недостающего здоровья врага");
    // recipes
    add("recipe.skilltree.weapon_poisoning", "Отравление Оружия");
    add(
        "recipe.skilltree.weapon_poisoning.info",
        "(Объедините оружие ближнего боя и вредящее зелье на верстаке, чтобы отравить оружие)");
    add("recipe.skilltree.potion_mixing", "Смешивание Зелий");
    add(
        "recipe.skilltree.potion_mixing.info",
        "(Объедините два разных зелья на верстаке, чтобы создать микстуру)");
    add("upgrade_recipe.chance", "Шанс: %s%%");
    // potions info
    add("potion.superior", "Качественное %s");
    add("item.minecraft.potion.mixture", "Микстура");
    add("item.minecraft.splash_potion.mixture", "Взрывная микстура");
    add("item.minecraft.lingering_potion.mixture", "Туманная микстура");
    addMixture("ныряния", MobEffects.NIGHT_VISION, MobEffects.WATER_BREATHING);
    addMixture("вечной молодости", MobEffects.HEAL, MobEffects.REGENERATION);
    addMixture("болезни", MobEffects.POISON, MobEffects.WEAKNESS);
    addMixture("филина", MobEffects.INVISIBILITY, MobEffects.NIGHT_VISION);
    addMixture("труса", MobEffects.INVISIBILITY, MobEffects.MOVEMENT_SPEED);
    addMixture("драконьей крови", MobEffects.FIRE_RESISTANCE, MobEffects.REGENERATION);
    addMixture("демона", MobEffects.FIRE_RESISTANCE, MobEffects.DAMAGE_BOOST);
    addMixture("убийцы", MobEffects.HARM, MobEffects.POISON);
    addMixture("антигравитации", MobEffects.JUMP, MobEffects.SLOW_FALLING);
    addMixture("старения", MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS);
    addMixture("атлета", MobEffects.JUMP, MobEffects.MOVEMENT_SPEED);
    addMixture("вора", MobEffects.INVISIBILITY, MobEffects.LUCK);
    addMixture("охотника за сокровищами", MobEffects.LUCK, MobEffects.WATER_BREATHING);
    addMixture("рыцаря", MobEffects.REGENERATION, MobEffects.DAMAGE_BOOST);
    addMixture("замедленного времени", MobEffects.SLOW_FALLING, MobEffects.MOVEMENT_SLOWDOWN);
    addMixture("солдата", MobEffects.HEAL, MobEffects.DAMAGE_BOOST);
    addMixture("ниндзя", MobEffects.DAMAGE_BOOST, MobEffects.MOVEMENT_SPEED);
    addMixture("благословения", MobEffects.LUCK, MobEffects.DAMAGE_BOOST);
    addMixture("чумы", MobEffects.POISON, MobEffects.MOVEMENT_SLOWDOWN);
    // gems info
    add("gem.socket", "Пустое гнездо");
    add("gem.additional_socket_1", "• Имеет дополнительное гнездо");
    add("gem.disabled", "Отключено с модулем приключений Apotheosis");
    add("gem_class_format", "• %s: %s");
    add("gem.tooltip", "• Можно вставить в предметы с гнёздами");
    add("gem_bonus.removal", "Уничтожает Самоцветы в предмете");
    add("gem_bonus.random", "Результат непредсказуем");
    // weapon info
    add("weapon.poisoned", "Отравлено:");
    // quiver info
    add("quiver.capacity", "• Вмещает до %s стрел");
    add("quiver.contents", "• Внутри: %s");
    // items
    add("item.cant_use.info", "Вы не можете это использовать");
    addGem("citrine", "цитрин");
    addGem("ruby", "рубин");
    addGem("sapphire", "сапфир");
    addGem("jade", "нефрит");
    addGem("iriscite", "ирисцит");
    addGem("vacucite", "вакуцит");
    add(PSTItems.WISDOM_SCROLL.get(), "Свиток мудрости");
    add(PSTItems.AMNESIA_SCROLL.get(), "Свиток амнезии");
    add(PSTItems.COPPER_RING.get(), "Медное кольцо");
    add(PSTItems.IRON_RING.get(), "Железное кольцо");
    add(PSTItems.GOLDEN_RING.get(), "Золотое кольцо");
    add(PSTItems.COPPER_NUGGET.get(), "Кусочек меди");
    add(PSTItems.ASSASSIN_NECKLACE.get(), "Ожерелье убийцы");
    add(PSTItems.HEALER_NECKLACE.get(), "Ожерелье целителя");
    add(PSTItems.TRAVELER_NECKLACE.get(), "Ожерелье путешественника");
    add(PSTItems.SIMPLE_NECKLACE.get(), "Ожерелье простоты");
    add(PSTItems.SCHOLAR_NECKLACE.get(), "Ожерелье учёного");
    add(PSTItems.ARSONIST_NECKLACE.get(), "Ожерелье поджигателя");
    add(PSTItems.FISHERMAN_NECKLACE.get(), "Ожерелье рыбака");
    add(PSTItems.ANCIENT_ALLOY_GILDED.get(), "Позолоченный древний сплав");
    add(PSTItems.ANCIENT_ALLOY_LIGHTWEIGHT.get(), "Облегченный древний сплав");
    add(PSTItems.ANCIENT_ALLOY_CURATIVE.get(), "Целебный древний сплав");
    add(PSTItems.ANCIENT_ALLOY_TOXIC.get(), "Токсичный древний сплав");
    add(PSTItems.ANCIENT_ALLOY_ENCHANTED.get(), "Зачарованный древний сплав");
    add(PSTItems.ANCIENT_ALLOY_SPATIAL.get(), "Пространственный древний сплав");
    addTooltip(PSTItems.WISDOM_SCROLL.get(), "Дарует одно очко пассивных умений");
    addTooltip(PSTItems.AMNESIA_SCROLL.get(), "Сбрасывает ваше древо пассивных умений");
    addWarning(PSTItems.AMNESIA_SCROLL.get(), "%d%% очков умений будут потеряны");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_GILDED.get(),
        "Позволяет улучшать характеристики связанные с лутом на некоторых предметах");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_LIGHTWEIGHT.get(),
        "Позволяет улучшать характеристики скорости на некоторых предметах");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_CURATIVE.get(),
        "Позволяет улучшать характеристики лечения на некоторых предметах");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_TOXIC.get(),
        "Позволяет улучшать характеристики ядов на некоторых предметах");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_ENCHANTED.get(),
        "Позволяет улучшать магические характеристики на некоторых предметах");
    addTooltip(
        PSTItems.ANCIENT_ALLOY_SPATIAL.get(),
        "Позволяет улучшать количество гнёзд на некоторых предметах");
    add("ancient_material.tooltip", "Требует определенных знаний для использования");
    // slots
    addCurioSlot("ring", "Слот кольца");
    addCurioSlot("ring", "plural", "Слоты колец");
    addCurioSlot("necklace", "Слот ожерелья");
    addCurioSlot("necklace", "plural", "Слоты ожерелий");
    // attributes
    add(PSTAttributes.REGENERATION.get(), "Регенерация здоровья");
    add(PSTAttributes.EXP_PER_MINUTE.get(), "Опыт в минуту");
    add(PSTAttributes.POISON_DAMAGE.get(), "Урон ядом");
    add(PSTAttributes.DEXTERITY.get(), "Ловкость");
    addInfo(PSTAttributes.DEXTERITY.get(), "+1% Урона снарядами за очко ловкости");
    add(PSTAttributes.STRENGTH.get(), "Сила");
    addInfo(PSTAttributes.STRENGTH.get(), "+1% Урона в ближнем бою за очко силы");
    add(PSTAttributes.INTELLIGENCE.get(), "Интеллект");
    addInfo(PSTAttributes.INTELLIGENCE.get(), "+1% Урона магией за очко интеллекта");
    add(PSTAttributes.VITALITY.get(), "Живучесть");
    addInfo(PSTAttributes.VITALITY.get(), "+1% Максимального здоровья за очко живучести");
    // effects
    add(PSTEffects.CRIT_DAMAGE_BONUS.get(), "Критический урон");
    add(PSTEffects.DAMAGE_BONUS.get(), "Урон");
    add(PSTEffects.LIFE_REGENERATION_BONUS.get(), "Регенерация здоровья");
    // system messages
    add(
        "skilltree.message.reset",
        "Древо пассивных умений изменилось. Ваши очки умений были восстановлены.");
    add("skilltree.message.reset_command", "Ваше древо пассивных умений было сброшено.");
    add("skilltree.message.point_command", "Получено очко пассивных умений.");
    // screen info
    add("widget.skill_points_left", "Очков осталось: %s");
    add("widget.skill_button.not_learned", "Умение не изучено");
    add("widget.skill_button.multiple_bonuses", "%s и %s");
    add("widget.buy_skill_button", "Купить очко умений");
    add("widget.confirm_button", "Подтвердить");
    add("widget.cancel_button", "Отмена");
    add("widget.show_stats", "Список бонусов");
    add("key.categories.skilltree", "Древо пассивных умений");
    add("key.display_skill_tree", "Открыть древо пассивных умений");
    add("skill.limitation", "Ограничение: %s");
    // jei info
    add(
        "skilltree.jei.gem_info",
        "Самоцветы можно вставлять в предметы с гнёздами на кузнечном столе. Выпадают из любой руды с небольшим шансом (требуется инструмент без шёлкового касания).");
    // curios info
    add("curios.identifier.quiver", "Колчан");
    add("curios.modifiers.quiver", "Когда надет:");
    // tabs
    add("itemGroup.skilltree", "Passive Skill Tree");
    // misc
    add("item.modifiers.both_hands", "Когда в руке:");
    // apotheosis compatibility
    add("text.apotheosis.category.curios:ring.plural", "Кольца");
    add("text.apotheosis.category.curios:necklace.plural", "Ожерелья");
    add("gem_class.jewelry", "Бижутерия");
    // affix names
    add("affix.skilltree:jewelry/dmg_reduction/tempered", "Закалённое");
    add("affix.skilltree:jewelry/dmg_reduction/tempered.suffix", "Закалки");
    add("affix.skilltree:jewelry/attribute/immortal", "Бессмертное");
    add("affix.skilltree:jewelry/attribute/immortal.suffix", "Бессмертия");
    add("affix.skilltree:jewelry/attribute/experienced", "Опытное");
    add("affix.skilltree:jewelry/attribute/experienced.suffix", "Опыта");
    add("affix.skilltree:jewelry/attribute/lucky", "Удачливое");
    add("affix.skilltree:jewelry/attribute/lucky.suffix", "Удачи");
    add("affix.skilltree:jewelry/attribute/hasty", "Спешащее");
    add("affix.skilltree:jewelry/attribute/hasty.suffix", "Спешки");
    add("affix.skilltree:jewelry/attribute/hidden", "Сокрытое");
    add("affix.skilltree:jewelry/attribute/hidden.suffix", "Скрытности");
    add("affix.skilltree:jewelry/attribute/healthy", "Здоровое");
    add("affix.skilltree:jewelry/attribute/healthy.suffix", "Здоровья");
  }

  protected void addMixture(String name, MobEffect... effects) {
    addMixture("Микстура " + name, "potion", effects);
    addMixture("Взрывная микстура" + name, "splash_potion", effects);
    addMixture("Туманная микстура " + name, "lingering_potion", effects);
  }

  protected void addGem(String type, String name) {
    super.addGem(
        type,
        name,
        "Раскрошенный",
        "Сломанный",
        "Некачественный",
        "Большой",
        "Редкий",
        "Исключительный");
  }
}
