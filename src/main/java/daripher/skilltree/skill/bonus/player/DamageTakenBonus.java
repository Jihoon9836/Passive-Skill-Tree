package daripher.skilltree.skill.bonus.player;

import com.google.gson.*;
import daripher.skilltree.client.tooltip.TooltipHelper;
import daripher.skilltree.client.widget.editor.SkillTreeEditor;
import daripher.skilltree.data.serializers.SerializationHelper;
import daripher.skilltree.init.PSTSkillBonuses;
import daripher.skilltree.network.NetworkHelper;
import daripher.skilltree.skill.bonus.SkillBonus;
import daripher.skilltree.skill.bonus.condition.damage.DamageCondition;
import daripher.skilltree.skill.bonus.condition.damage.MeleeDamageCondition;
import daripher.skilltree.skill.bonus.condition.damage.NoneDamageCondition;
import daripher.skilltree.skill.bonus.condition.living.LivingCondition;
import daripher.skilltree.skill.bonus.condition.living.NoneLivingCondition;
import daripher.skilltree.skill.bonus.multiplier.LivingMultiplier;
import daripher.skilltree.skill.bonus.multiplier.NoneLivingMultiplier;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public final class DamageTakenBonus implements SkillBonus<DamageTakenBonus> {
  private float amount;
  private AttributeModifier.Operation operation;
  private @Nonnull LivingMultiplier playerMultiplier = NoneLivingMultiplier.INSTANCE;
  private @Nonnull LivingMultiplier attackerMultiplier = NoneLivingMultiplier.INSTANCE;
  private @Nonnull LivingCondition playerCondition = NoneLivingCondition.INSTANCE;
  private @Nonnull LivingCondition attackerCondition = NoneLivingCondition.INSTANCE;
  private @Nonnull DamageCondition damageCondition = NoneDamageCondition.INSTANCE;

  public DamageTakenBonus(float amount, AttributeModifier.Operation operation) {
    this.amount = amount;
    this.operation = operation;
  }

  public float getDamageBonus(
      AttributeModifier.Operation operation,
      DamageSource source,
      Player player,
      LivingEntity attacker) {
    if (this.operation != operation) return 0f;
    if (!damageCondition.met(source)) return 0f;
    if (!playerCondition.met(player)) return 0f;
    if (!attackerCondition.met(attacker)) return 0f;
    return amount * playerMultiplier.getValue(player) * attackerMultiplier.getValue(attacker);
  }

  @Override
  public SkillBonus.Serializer getSerializer() {
    return PSTSkillBonuses.DAMAGE_TAKEN.get();
  }

  @Override
  public DamageTakenBonus copy() {
    DamageTakenBonus bonus = new DamageTakenBonus(amount, operation);
    bonus.playerMultiplier = this.playerMultiplier;
    bonus.attackerMultiplier = this.attackerMultiplier;
    bonus.playerCondition = this.playerCondition;
    bonus.damageCondition = this.damageCondition;
    bonus.attackerCondition = this.attackerCondition;
    return bonus;
  }

  @Override
  public DamageTakenBonus multiply(double multiplier) {
    amount *= (float) multiplier;
    return this;
  }

  @Override
  public boolean canMerge(SkillBonus<?> other) {
    if (!(other instanceof DamageTakenBonus otherBonus)) return false;
    if (!Objects.equals(otherBonus.playerMultiplier, this.playerMultiplier)) return false;
    if (!Objects.equals(otherBonus.attackerMultiplier, this.attackerMultiplier)) return false;
    if (!Objects.equals(otherBonus.playerCondition, this.playerCondition)) return false;
    if (!Objects.equals(otherBonus.damageCondition, this.damageCondition)) return false;
    if (!Objects.equals(otherBonus.attackerCondition, this.attackerCondition)) return false;
    return otherBonus.operation == this.operation;
  }

  @Override
  public SkillBonus<DamageTakenBonus> merge(SkillBonus<?> other) {
    if (!(other instanceof DamageTakenBonus otherBonus)) {
      throw new IllegalArgumentException();
    }
    float mergedAmount = otherBonus.amount + this.amount;
    DamageTakenBonus mergedBonus = new DamageTakenBonus(mergedAmount, this.operation);
    mergedBonus.playerMultiplier = this.playerMultiplier;
    mergedBonus.attackerMultiplier = this.attackerMultiplier;
    mergedBonus.playerCondition = this.playerCondition;
    mergedBonus.damageCondition = this.damageCondition;
    mergedBonus.attackerCondition = this.attackerCondition;
    return mergedBonus;
  }

  @Override
  public MutableComponent getTooltip() {
    MutableComponent tooltip =
        Component.translatable(getDescriptionId(), damageCondition.getTooltip());
    tooltip = TooltipHelper.getSkillBonusTooltip(tooltip, amount, operation);
    tooltip = playerMultiplier.getTooltip(tooltip, Target.PLAYER);
    tooltip = attackerMultiplier.getTooltip(tooltip, Target.ENEMY);
    tooltip = playerCondition.getTooltip(tooltip, "you");
    tooltip = attackerCondition.getTooltip(tooltip, "target");
    return tooltip.withStyle(TooltipHelper.getSkillBonusStyle(isPositive()));
  }

  @Override
  public boolean isPositive() {
    return amount < 0;
  }

  @Override
  public void addEditorWidgets(
      SkillTreeEditor editor, int row, Consumer<DamageTakenBonus> consumer) {
    editor.addLabel(0, 0, "Amount", ChatFormatting.GOLD);
    editor.addLabel(55, 0, "Operation", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor
        .addNumericTextField(0, 0, 50, 14, amount)
        .setNumericResponder(value -> selectAmount(consumer, value));
    editor
        .addOperationSelection(55, 0, 145, operation)
        .setResponder(operation -> selectOperation(consumer, operation));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Damage Condition", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, damageCondition)
        .setResponder(condition -> selectDamageCondition(consumer, condition));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Player Condition", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, playerCondition)
        .setResponder(condition -> selectPlayerCondition(editor, consumer, condition))
        .setMenuInitFunc(() -> addPlayerConditionWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Attacker Condition", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, attackerCondition)
        .setResponder(condition -> selectTargetCondition(editor, consumer, condition))
        .setMenuInitFunc(() -> addTargetConditionWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Player Multiplier", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, playerMultiplier)
        .setResponder(multiplier -> selectPlayerMultiplier(editor, consumer, multiplier))
        .setMenuInitFunc(() -> addPlayerMultiplierWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Attacker Multiplier", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, attackerMultiplier)
        .setResponder(multiplier -> selectTargetMultiplier(editor, consumer, multiplier))
        .setMenuInitFunc(() -> addTargetMultiplierWidgets(editor, consumer));
    editor.increaseHeight(19);
  }

  private void selectOperation(
      Consumer<DamageTakenBonus> consumer, AttributeModifier.Operation operation) {
    setOperation(operation);
    consumer.accept(this.copy());
  }

  private void selectAmount(Consumer<DamageTakenBonus> consumer, Double value) {
    setAmount(value.floatValue());
    consumer.accept(this.copy());
  }

  private void addTargetMultiplierWidgets(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer) {
    attackerMultiplier.addEditorWidgets(
        editor,
        multiplier -> {
          setEnemyMultiplier(multiplier);
          consumer.accept(this.copy());
        });
  }

  private void selectTargetMultiplier(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer, LivingMultiplier multiplier) {
    setEnemyMultiplier(multiplier);
    consumer.accept(this.copy());
    editor.rebuildWidgets();
  }

  private void addPlayerMultiplierWidgets(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer) {
    playerMultiplier.addEditorWidgets(
        editor,
        multiplier -> {
          setPlayerMultiplier(multiplier);
          consumer.accept(this.copy());
        });
  }

  private void selectPlayerMultiplier(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer, LivingMultiplier multiplier) {
    setPlayerMultiplier(multiplier);
    consumer.accept(this.copy());
    editor.rebuildWidgets();
  }

  private void addTargetConditionWidgets(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer) {
    attackerCondition.addEditorWidgets(
        editor,
        c -> {
          setTargetCondition(c);
          consumer.accept(this.copy());
        });
  }

  private void selectTargetCondition(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer, LivingCondition condition) {
    setTargetCondition(condition);
    consumer.accept(this.copy());
    editor.rebuildWidgets();
  }

  private void addPlayerConditionWidgets(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer) {
    playerCondition.addEditorWidgets(
        editor,
        c -> {
          setPlayerCondition(c);
          consumer.accept(this.copy());
        });
  }

  private void selectPlayerCondition(
      SkillTreeEditor editor, Consumer<DamageTakenBonus> consumer, LivingCondition condition) {
    setPlayerCondition(condition);
    consumer.accept(this.copy());
    editor.rebuildWidgets();
  }

  private void selectDamageCondition(
      Consumer<DamageTakenBonus> consumer, DamageCondition condition) {
    setDamageCondition(condition);
    consumer.accept(this.copy());
  }

  public SkillBonus<?> setPlayerCondition(LivingCondition condition) {
    this.playerCondition = condition;
    return this;
  }

  public SkillBonus<?> setDamageCondition(DamageCondition condition) {
    this.damageCondition = condition;
    return this;
  }

  public SkillBonus<?> setTargetCondition(LivingCondition condition) {
    this.attackerCondition = condition;
    return this;
  }

  public SkillBonus<?> setPlayerMultiplier(LivingMultiplier multiplier) {
    this.playerMultiplier = multiplier;
    return this;
  }

  public SkillBonus<?> setEnemyMultiplier(LivingMultiplier multiplier) {
    this.attackerMultiplier = multiplier;
    return this;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public void setOperation(AttributeModifier.Operation operation) {
    this.operation = operation;
  }

  public static class Serializer implements SkillBonus.Serializer {
    @Override
    public DamageTakenBonus deserialize(JsonObject json) throws JsonParseException {
      float amount = SerializationHelper.getElement(json, "amount").getAsFloat();
      AttributeModifier.Operation operation = SerializationHelper.deserializeOperation(json);
      DamageTakenBonus bonus = new DamageTakenBonus(amount, operation);
      bonus.playerMultiplier =
          SerializationHelper.deserializeLivingMultiplier(json, "player_multiplier");
      bonus.attackerMultiplier =
          SerializationHelper.deserializeLivingMultiplier(json, "attacker_multiplier");
      bonus.playerCondition =
          SerializationHelper.deserializeLivingCondition(json, "player_condition");
      bonus.damageCondition = SerializationHelper.deserializeDamageCondition(json);
      bonus.attackerCondition =
          SerializationHelper.deserializeLivingCondition(json, "attacker_condition");
      return bonus;
    }

    @Override
    public void serialize(JsonObject json, SkillBonus<?> bonus) {
      if (!(bonus instanceof DamageTakenBonus aBonus)) {
        throw new IllegalArgumentException();
      }
      json.addProperty("amount", aBonus.amount);
      SerializationHelper.serializeOperation(json, aBonus.operation);
      SerializationHelper.serializeLivingMultiplier(
          json, aBonus.playerMultiplier, "player_multiplier");
      SerializationHelper.serializeLivingMultiplier(
          json, aBonus.attackerMultiplier, "attacker_multiplier");
      SerializationHelper.serializeLivingCondition(
          json, aBonus.playerCondition, "player_condition");
      SerializationHelper.serializeDamageCondition(json, aBonus.damageCondition);
      SerializationHelper.serializeLivingCondition(
          json, aBonus.attackerCondition, "attacker_condition");
    }

    @Override
    public DamageTakenBonus deserialize(CompoundTag tag) {
      float amount = tag.getFloat("amount");
      AttributeModifier.Operation operation = SerializationHelper.deserializeOperation(tag);
      DamageTakenBonus bonus = new DamageTakenBonus(amount, operation);
      bonus.playerMultiplier =
          SerializationHelper.deserializeLivingMultiplier(tag, "player_multiplier");
      bonus.attackerMultiplier =
          SerializationHelper.deserializeLivingMultiplier(tag, "attacker_multiplier");
      bonus.playerCondition =
          SerializationHelper.deserializeLivingCondition(tag, "player_condition");
      bonus.damageCondition = SerializationHelper.deserializeDamageCondition(tag);
      bonus.attackerCondition =
          SerializationHelper.deserializeLivingCondition(tag, "attacker_condition");
      return bonus;
    }

    @Override
    public CompoundTag serialize(SkillBonus<?> bonus) {
      if (!(bonus instanceof DamageTakenBonus aBonus)) {
        throw new IllegalArgumentException();
      }
      CompoundTag tag = new CompoundTag();
      tag.putFloat("amount", aBonus.amount);
      SerializationHelper.serializeOperation(tag, aBonus.operation);
      SerializationHelper.serializeLivingMultiplier(
          tag, aBonus.playerMultiplier, "player_multiplier");
      SerializationHelper.serializeLivingMultiplier(
          tag, aBonus.attackerMultiplier, "attacker_multiplier");
      SerializationHelper.serializeLivingCondition(tag, aBonus.playerCondition, "player_condition");
      SerializationHelper.serializeDamageCondition(tag, aBonus.damageCondition);
      SerializationHelper.serializeLivingCondition(
          tag, aBonus.attackerCondition, "attacker_condition");
      return tag;
    }

    @Override
    public DamageTakenBonus deserialize(FriendlyByteBuf buf) {
      float amount = buf.readFloat();
      AttributeModifier.Operation operation = AttributeModifier.Operation.fromValue(buf.readInt());
      DamageTakenBonus bonus = new DamageTakenBonus(amount, operation);
      bonus.playerMultiplier = NetworkHelper.readLivingMultiplier(buf);
      bonus.attackerMultiplier = NetworkHelper.readLivingMultiplier(buf);
      bonus.playerCondition = NetworkHelper.readLivingCondition(buf);
      bonus.damageCondition = NetworkHelper.readDamageCondition(buf);
      bonus.attackerCondition = NetworkHelper.readLivingCondition(buf);
      return bonus;
    }

    @Override
    public void serialize(FriendlyByteBuf buf, SkillBonus<?> bonus) {
      if (!(bonus instanceof DamageTakenBonus aBonus)) {
        throw new IllegalArgumentException();
      }
      buf.writeFloat(aBonus.amount);
      buf.writeInt(aBonus.operation.toValue());
      NetworkHelper.writeLivingMultiplier(buf, aBonus.playerMultiplier);
      NetworkHelper.writeLivingMultiplier(buf, aBonus.attackerMultiplier);
      NetworkHelper.writeLivingCondition(buf, aBonus.playerCondition);
      NetworkHelper.writeDamageCondition(buf, aBonus.damageCondition);
      NetworkHelper.writeLivingCondition(buf, aBonus.attackerCondition);
    }

    @Override
    public SkillBonus<?> createDefaultInstance() {
      return new DamageTakenBonus(0.1f, AttributeModifier.Operation.MULTIPLY_BASE)
          .setDamageCondition(new MeleeDamageCondition());
    }
  }
}
