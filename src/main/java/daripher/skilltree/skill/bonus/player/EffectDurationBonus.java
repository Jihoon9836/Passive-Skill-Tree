package daripher.skilltree.skill.bonus.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import daripher.skilltree.client.tooltip.TooltipHelper;
import daripher.skilltree.client.widget.editor.SkillTreeEditor;
import daripher.skilltree.data.serializers.SerializationHelper;
import daripher.skilltree.init.PSTSkillBonuses;
import daripher.skilltree.network.NetworkHelper;
import daripher.skilltree.skill.bonus.SkillBonus;
import daripher.skilltree.skill.bonus.condition.effect.EffectType;
import daripher.skilltree.skill.bonus.condition.living.LivingCondition;
import daripher.skilltree.skill.bonus.condition.living.NoneLivingCondition;
import daripher.skilltree.skill.bonus.multiplier.LivingMultiplier;
import daripher.skilltree.skill.bonus.multiplier.NoneLivingMultiplier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public final class EffectDurationBonus implements SkillBonus<EffectDurationBonus> {
  private EffectType effectType;
  private float duration;
  private @Nonnull LivingMultiplier playerMultiplier = NoneLivingMultiplier.INSTANCE;
  private @Nonnull LivingCondition playerCondition = NoneLivingCondition.INSTANCE;

  public EffectDurationBonus(EffectType effectType, float duration) {
    this.effectType = effectType;
    this.duration = duration;
  }

  public float getDuration(Player player) {
    if (!playerCondition.met(player)) return 0f;
    return duration * playerMultiplier.getValue(player);
  }

  @Override
  public SkillBonus.Serializer getSerializer() {
    return PSTSkillBonuses.EFFECT_DURATION.get();
  }

  @Override
  public EffectDurationBonus copy() {
    EffectDurationBonus bonus = new EffectDurationBonus(effectType, duration);
    bonus.playerMultiplier = this.playerMultiplier;
    bonus.playerCondition = this.playerCondition;
    return bonus;
  }

  @Override
  public EffectDurationBonus multiply(double multiplier) {
    duration = (float) (duration * multiplier);
    return this;
  }

  @Override
  public boolean canMerge(SkillBonus<?> other) {
    if (!(other instanceof EffectDurationBonus otherBonus)) return false;
    return otherBonus.effectType == this.effectType;
  }

  @Override
  public SkillBonus<EffectDurationBonus> merge(SkillBonus<?> other) {
    if (!(other instanceof EffectDurationBonus otherBonus)) {
      throw new IllegalArgumentException();
    }
    EffectDurationBonus mergedBonus = new EffectDurationBonus(effectType, duration + otherBonus.duration);
    mergedBonus.playerCondition = this.playerCondition;
    mergedBonus.playerMultiplier = this.playerMultiplier;
    return mergedBonus;
  }

  @Override
  public MutableComponent getTooltip() {
    Component effectTypeDescription = Component.translatable(effectType.getDescriptionId() + ".plural");
    MutableComponent tooltip = Component.translatable(getDescriptionId(), effectTypeDescription);
    tooltip = TooltipHelper.getSkillBonusTooltip(tooltip, duration, AttributeModifier.Operation.MULTIPLY_BASE);
    tooltip = playerMultiplier.getTooltip(tooltip, Target.PLAYER);
    tooltip = playerCondition.getTooltip(tooltip, Target.PLAYER);
    return tooltip.withStyle(TooltipHelper.getSkillBonusStyle(isPositive()));
  }

  @Override
  public boolean isPositive() {
    return effectType == EffectType.HARMFUL ? duration < 0 : duration > 0;
  }

  @Override
  public void addEditorWidgets(SkillTreeEditor editor, int index, Consumer<EffectDurationBonus> consumer) {
    editor.addLabel(0, 0, "Effect Type", ChatFormatting.GREEN);
    editor.increaseHeight(19);
    editor.addSelectionMenu(0, 0, 200, effectType)
        .setElementNameGetter(effectType -> Component.literal(effectType.name()))
        .setResponder(effectType -> selectEffectType(consumer, effectType));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Duration", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor.addNumericTextField(0, 0, 50, 14, duration)
        .setNumericResponder(value -> selectDuration(consumer, value));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Player Condition", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor.addSelectionMenu(0, 0, 200, playerCondition)
        .setResponder(condition -> selectPlayerCondition(editor, consumer, condition))
        .setMenuInitFunc(() -> addPlayerConditionWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Player Multiplier", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor.addSelectionMenu(0, 0, 200, playerMultiplier)
        .setResponder(multiplier -> selectPlayerMultiplier(editor, consumer, multiplier))
        .setMenuInitFunc(() -> addPlayerMultiplierWidgets(editor, consumer));
    editor.increaseHeight(19);
  }

  private void selectPlayerMultiplier(SkillTreeEditor editor, Consumer<EffectDurationBonus> consumer, LivingMultiplier multiplier) {
    setMultiplier(multiplier);
    consumer.accept(this.copy());
    editor.rebuildWidgets();
  }

  private void selectPlayerCondition(SkillTreeEditor editor, Consumer<EffectDurationBonus> consumer, LivingCondition condition) {
    setCondition(condition);
    consumer.accept(this.copy());
    editor.rebuildWidgets();
  }

  private void selectEffectType(Consumer<EffectDurationBonus> consumer, EffectType effectType) {
    setEffectType(effectType);
    consumer.accept(this.copy());
  }

  private void selectDuration(Consumer<EffectDurationBonus> consumer, Double duration) {
    setDuration(duration.floatValue());
    consumer.accept(this.copy());
  }

  private void addPlayerConditionWidgets(SkillTreeEditor editor, Consumer<EffectDurationBonus> consumer) {
    playerCondition.addEditorWidgets(editor, c -> {
      setCondition(c);
      consumer.accept(this.copy());
    });
  }

  private void addPlayerMultiplierWidgets(SkillTreeEditor editor, Consumer<EffectDurationBonus> consumer) {
    playerMultiplier.addEditorWidgets(editor, m -> {
      setMultiplier(m);
      consumer.accept(this.copy());
    });
  }

  public void setDuration(float duration) {
    this.duration = duration;
  }

  public void setEffectType(EffectType effectType) {
    this.effectType = effectType;
  }

  public SkillBonus<?> setCondition(LivingCondition condition) {
    this.playerCondition = condition;
    return this;
  }

  public SkillBonus<?> setMultiplier(LivingMultiplier multiplier) {
    this.playerMultiplier = multiplier;
    return this;
  }

  public static class Serializer implements SkillBonus.Serializer {
    @Override
    public EffectDurationBonus deserialize(JsonObject json) throws JsonParseException {
      EffectType effectType = EffectType.fromName(json.get("effect_type")
          .getAsString());
      float duration = json.get("duration")
          .getAsFloat();
      EffectDurationBonus bonus = new EffectDurationBonus(effectType, duration);
      bonus.playerMultiplier = SerializationHelper.deserializeLivingMultiplier(json, "player_multiplier");
      bonus.playerCondition = SerializationHelper.deserializeLivingCondition(json, "player_condition");
      return bonus;
    }

    @Override
    public void serialize(JsonObject json, SkillBonus<?> bonus) {
      if (!(bonus instanceof EffectDurationBonus aBonus)) {
        throw new IllegalArgumentException();
      }
      json.addProperty("effect_type", aBonus.effectType.getName());
      json.addProperty("duration", aBonus.duration);
      SerializationHelper.serializeLivingMultiplier(json, aBonus.playerMultiplier, "player_multiplier");
      SerializationHelper.serializeLivingCondition(json, aBonus.playerCondition, "player_condition");
    }

    @Override
    public EffectDurationBonus deserialize(CompoundTag tag) {
      EffectType effectType = EffectType.fromName(tag.getString("effect_type"));
      float duration = tag.getFloat("duration");
      EffectDurationBonus bonus = new EffectDurationBonus(effectType, duration);
      bonus.playerMultiplier = SerializationHelper.deserializeLivingMultiplier(tag, "player_multiplier");
      bonus.playerCondition = SerializationHelper.deserializeLivingCondition(tag, "player_condition");
      return bonus;
    }

    @Override
    public CompoundTag serialize(SkillBonus<?> bonus) {
      if (!(bonus instanceof EffectDurationBonus aBonus)) {
        throw new IllegalArgumentException();
      }
      CompoundTag tag = new CompoundTag();
      tag.putString("effect_type", aBonus.effectType.getName());
      tag.putFloat("duration", aBonus.duration);
      SerializationHelper.serializeLivingMultiplier(tag, aBonus.playerMultiplier, "player_multiplier");
      SerializationHelper.serializeLivingCondition(tag, aBonus.playerCondition, "player_condition");
      return tag;
    }

    @Override
    public EffectDurationBonus deserialize(FriendlyByteBuf buf) {
      EffectType effectType = EffectType.values()[buf.readInt()];
      float duration = buf.readFloat();
      EffectDurationBonus bonus = new EffectDurationBonus(effectType, duration);
      bonus.playerMultiplier = NetworkHelper.readLivingMultiplier(buf);
      bonus.playerCondition = NetworkHelper.readLivingCondition(buf);
      return bonus;
    }

    @Override
    public void serialize(FriendlyByteBuf buf, SkillBonus<?> bonus) {
      if (!(bonus instanceof EffectDurationBonus aBonus)) {
        throw new IllegalArgumentException();
      }
      buf.writeInt(aBonus.effectType.ordinal());
      buf.writeFloat(aBonus.duration);
      NetworkHelper.writeLivingMultiplier(buf, aBonus.playerMultiplier);
      NetworkHelper.writeLivingCondition(buf, aBonus.playerCondition);
    }

    @Override
    public SkillBonus<?> createDefaultInstance() {
      return new EffectDurationBonus(EffectType.BENEFICIAL, 0.1f);
    }
  }
}
