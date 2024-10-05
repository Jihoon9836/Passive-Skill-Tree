package daripher.skilltree.client.tooltip;

import daripher.skilltree.effect.SkillBonusEffect;
import daripher.skilltree.skill.bonus.SkillBonus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TooltipHelper {
  private static final Style SKILL_BONUS_STYLE = Style.EMPTY.withColor(0x7B7BE5);
  private static final Style SKILL_BONUS_STYLE_NEGATIVE = Style.EMPTY.withColor(0xE25A5A);
  private static final Style ITEM_BONUS_STYLE = Style.EMPTY.withColor(0x7AB3E2);
  private static final Style ITEM_BONUS_STYLE_NEGATIVE = Style.EMPTY.withColor(0xDB9792);

  public static Component getEffectTooltip(MobEffectInstance effect) {
    Component effectDescription;
    if (effect.getEffect() instanceof SkillBonusEffect skillEffect) {
      effectDescription =
          skillEffect
              .getBonus()
              .copy()
              .multiply(effect.getAmplifier() + 1)
              .getTooltip()
              .setStyle(Style.EMPTY);
    } else {
      effectDescription = effect.getEffect().getDisplayName();
      if (effect.getAmplifier() == 0) return effectDescription;
      Component amplifier = Component.translatable("potion.potency." + effect.getAmplifier());
      effectDescription =
          Component.translatable("potion.withAmplifier", effectDescription, amplifier);
    }
    return effectDescription;
  }

  public static Component getEffectTooltipWithTime(MobEffectInstance effect) {
    Component effectDescription = getEffectTooltip(effect);
    Component durationDescription = MobEffectUtil.formatDuration(effect, 1f);
    ChatFormatting style = effect.getEffect().getCategory().getTooltipFormatting();
    return Component.translatable("potion.withDuration", effectDescription, durationDescription)
        .withStyle(style);
  }

  public static Component getOperationName(AttributeModifier.Operation operation) {
    return Component.literal(
        switch (operation) {
          case ADDITION -> "Addition";
          case MULTIPLY_BASE -> "Multiply Base";
          case MULTIPLY_TOTAL -> "Multiply Total";
        });
  }

  public static Component getOptionalTooltip(String descriptionId, String subtype) {
    String key = "%s.%s".formatted(descriptionId, subtype);
    Component tooltip = Component.translatable(key);
    if (!tooltip.getString().equals(key)) {
      return tooltip;
    }
    return Component.translatable(descriptionId);
  }

  public static void consumeTranslated(String descriptionId, Consumer<MutableComponent> consumer) {
    MutableComponent tooltip = Component.translatable(descriptionId);
    if (!tooltip.getString().equals(descriptionId)) {
      consumer.accept(tooltip);
    }
  }

  public static MutableComponent getSkillBonusTooltip(
      Component bonusDescription, double amount, AttributeModifier.Operation operation) {
    float multiplier = 1;
    if (operation != AttributeModifier.Operation.ADDITION) {
      multiplier = 100;
    }
    double visibleAmount = amount * multiplier;
    if (amount < 0) visibleAmount *= -1;
    String operationDescription = amount > 0 ? "plus" : "take";
    operationDescription = "attribute.modifier." + operationDescription + "." + operation.ordinal();
    String multiplierDescription = formatNumber(visibleAmount);
    return Component.translatable(operationDescription, multiplierDescription, bonusDescription);
  }

  public static String formatNumber(double number) {
    return ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(number);
  }

  public static MutableComponent getSkillBonusTooltip(
      String bonus, double amount, AttributeModifier.Operation operation) {
    return getSkillBonusTooltip(Component.translatable(bonus), amount, operation);
  }

  public static Style getSkillBonusStyle(boolean positive) {
    return positive ? SKILL_BONUS_STYLE : SKILL_BONUS_STYLE_NEGATIVE;
  }

  public static Style getItemBonusStyle(boolean positive) {
    return positive ? ITEM_BONUS_STYLE : ITEM_BONUS_STYLE_NEGATIVE;
  }

  public static MutableComponent getTextureName(ResourceLocation location) {
    String texture = location.getPath();
    texture = texture.substring(texture.lastIndexOf("/") + 1);
    texture = texture.replace(".png", "");
    texture = TooltipHelper.idToName(texture);
    return Component.literal(texture);
  }

  public static MutableComponent getTargetName(SkillBonus.Target target) {
    return Component.literal(TooltipHelper.idToName(target.name().toLowerCase()));
  }

  public static String getRecipeDescriptionId(ResourceLocation recipeId) {
    return "recipe.%s.%s".formatted(recipeId.getNamespace(), recipeId.getPath());
  }

  @NotNull
  public static String idToName(String path) {
    String[] words = path.split("_");
    StringBuilder name = new StringBuilder();
    Arrays.stream(words)
        .map(w -> w.substring(0, 1).toUpperCase() + w.substring(1))
        .forEach(
            w -> {
              name.append(" ");
              name.append(w);
            });
    return name.substring(1);
  }

  public static List<MutableComponent> split(MutableComponent component, Font font, int maxWidth) {
    String[] split = component.getString().split(" ");
    if (split.length < 2) {
      return List.of(component);
    }
    String line = split[0];
    List<MutableComponent> components = new ArrayList<>();
    for (int i = 1; i < split.length; i++) {
      String next = line + " " + split[i];
      if (font.width(next) > maxWidth) {
        components.add(Component.translatable(line).withStyle(component.getStyle()));
        line = "  " + split[i];
        continue;
      }
      line = next;
    }
    components.add(Component.translatable(line).withStyle(component.getStyle()));
    return components;
  }

  @NotNull
  public static String getTrimmedString(Font font, String message, int maxWidth) {
    if (font.width(message) > maxWidth) {
      while (font.width(message + "...") > maxWidth) {
        message = message.substring(0, message.length() - 1);
      }
      message += "...";
    }
    return message;
  }

  @NotNull
  public static String getTrimmedString(String message, int maxWidth) {
    return getTrimmedString(Minecraft.getInstance().font, message, maxWidth);
  }

  public static Component getSlotTooltip(String slotName, String type) {
    return Component.translatable("curio.slot.%s.%s".formatted(slotName, type));
  }

  public static Component getSlotTooltip(String slotName) {
    return Component.translatable("curio.slot.%s".formatted(slotName));
  }
}
