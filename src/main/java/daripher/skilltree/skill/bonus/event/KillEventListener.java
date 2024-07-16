package daripher.skilltree.skill.bonus.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import daripher.skilltree.client.widget.editor.SkillTreeEditor;
import daripher.skilltree.data.serializers.SerializationHelper;
import daripher.skilltree.init.PSTEventListeners;
import daripher.skilltree.network.NetworkHelper;
import daripher.skilltree.skill.bonus.EventListenerBonus;
import daripher.skilltree.skill.bonus.SkillBonus;
import daripher.skilltree.skill.bonus.condition.damage.DamageCondition;
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
import net.minecraft.world.entity.player.Player;

public class KillEventListener implements SkillEventListener {
  private LivingCondition playerCondition = NoneLivingCondition.INSTANCE;
  private LivingCondition enemyCondition = NoneLivingCondition.INSTANCE;
  private DamageCondition damageCondition = NoneDamageCondition.INSTANCE;
  private LivingMultiplier playerMultiplier = NoneLivingMultiplier.INSTANCE;
  private LivingMultiplier enemyMultiplier = NoneLivingMultiplier.INSTANCE;

  public void onEvent(
      @Nonnull Player player,
      @Nonnull LivingEntity enemy,
      @Nonnull DamageSource damage,
      @Nonnull EventListenerBonus<?> skill) {
    if (!playerCondition.met(player)) return;
    if (!enemyCondition.met(enemy)) return;
    if (!damageCondition.met(damage)) return;
    skill
        .multiply(playerMultiplier.getValue(player) * enemyMultiplier.getValue(enemy))
        .applyEffect(player);
  }

  @Override
  public MutableComponent getTooltip(Component bonusTooltip) {
    MutableComponent eventTooltip;
    if (damageCondition != NoneDamageCondition.INSTANCE) {
      Component damageTooltip = damageCondition.getTooltip();
      eventTooltip =
          Component.translatable(getDescriptionId() + ".damage", bonusTooltip, damageTooltip);
    } else {
      eventTooltip = Component.translatable(getDescriptionId(), bonusTooltip);
    }
    eventTooltip = playerCondition.getTooltip(eventTooltip, "you");
    eventTooltip = enemyCondition.getTooltip(eventTooltip, "target");
    eventTooltip = playerMultiplier.getTooltip(eventTooltip, SkillBonus.Target.PLAYER);
    eventTooltip = enemyMultiplier.getTooltip(eventTooltip, SkillBonus.Target.ENEMY);
    return eventTooltip;
  }

  @Override
  public SkillBonus.Target getTarget() {
    return SkillBonus.Target.PLAYER;
  }

  @Override
  public SkillEventListener.Serializer getSerializer() {
    return PSTEventListeners.ON_KILL.get();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    KillEventListener listener = (KillEventListener) o;
    return Objects.equals(playerCondition, listener.playerCondition)
        && Objects.equals(enemyCondition, listener.enemyCondition)
        && Objects.equals(damageCondition, listener.damageCondition)
        && Objects.equals(playerMultiplier, listener.playerMultiplier)
        && Objects.equals(enemyMultiplier, listener.enemyMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        playerCondition, enemyCondition, damageCondition, playerMultiplier, enemyMultiplier);
  }

  @Override
  public void addEditorWidgets(SkillTreeEditor editor, Consumer<SkillEventListener> consumer) {
    editor.addLabel(0, 0, "Player Condition", ChatFormatting.GREEN);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, playerCondition)
        .setResponder(condition -> selectPlayerCondition(editor, consumer, condition))
        .setOnMenuInit(() -> addPlayerConditionWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Enemy Condition", ChatFormatting.GREEN);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, enemyCondition)
        .setResponder(condition -> selectTargetCondition(editor, consumer, condition))
        .setOnMenuInit(() -> addTargetConditionWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Player Multiplier", ChatFormatting.GREEN);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, playerMultiplier)
        .setResponder(multiplier -> selectPlayerMultiplier(editor, consumer, multiplier))
        .setOnMenuInit(() -> addPlayerMultiplierWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Enemy Multiplier", ChatFormatting.GREEN);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 200, enemyMultiplier)
        .setResponder(multiplier -> selectTargetMultiplier(editor, consumer, multiplier))
        .setOnMenuInit(() -> addTargetMultiplierWidgets(editor, consumer));
    editor.increaseHeight(19);
    editor.addLabel(0, 0, "Damage", ChatFormatting.GREEN);
    editor.increaseHeight(19);
    editor
        .addSelectionMenu(0, 0, 95, damageCondition)
        .setResponder(condition -> selectDamageCondition(consumer, condition));
    editor.increaseHeight(19);
  }

  private void selectDamageCondition(
      Consumer<SkillEventListener> consumer, DamageCondition condition) {
    setDamageCondition(condition);
    consumer.accept(this);
  }

  private void addTargetMultiplierWidgets(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer) {
    enemyMultiplier.addEditorWidgets(
        editor,
        multiplier -> {
          setPlayerMultiplier(multiplier);
          consumer.accept(this);
        });
  }

  private void selectTargetMultiplier(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer, LivingMultiplier multiplier) {
    setEnemyMultiplier(multiplier);
    consumer.accept(this);
    editor.rebuildWidgets();
  }

  private void addPlayerMultiplierWidgets(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer) {
    playerMultiplier.addEditorWidgets(
        editor,
        multiplier -> {
          setPlayerMultiplier(multiplier);
          consumer.accept(this);
        });
  }

  private void selectPlayerMultiplier(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer, LivingMultiplier multiplier) {
    setPlayerMultiplier(multiplier);
    consumer.accept(this);
    editor.rebuildWidgets();
  }

  private void addTargetConditionWidgets(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer) {
    enemyCondition.addEditorWidgets(
        editor,
        condition -> {
          setEnemyCondition(condition);
          consumer.accept(this);
        });
  }

  private void selectTargetCondition(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer, LivingCondition condition) {
    setEnemyCondition(condition);
    consumer.accept(this);
    editor.rebuildWidgets();
  }

  private void addPlayerConditionWidgets(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer) {
    playerCondition.addEditorWidgets(
        editor,
        condition -> {
          setPlayerCondition(condition);
          consumer.accept(this);
        });
  }

  private void selectPlayerCondition(
      SkillTreeEditor editor, Consumer<SkillEventListener> consumer, LivingCondition condition) {
    setPlayerCondition(condition);
    consumer.accept(this);
    editor.rebuildWidgets();
  }

  public KillEventListener setDamageCondition(DamageCondition damageCondition) {
    this.damageCondition = damageCondition;
    return this;
  }

  public KillEventListener setEnemyCondition(LivingCondition enemyCondition) {
    this.enemyCondition = enemyCondition;
    return this;
  }

  public KillEventListener setPlayerCondition(LivingCondition playerCondition) {
    this.playerCondition = playerCondition;
    return this;
  }

  public KillEventListener setEnemyMultiplier(LivingMultiplier enemyMultiplier) {
    this.enemyMultiplier = enemyMultiplier;
    return this;
  }

  public KillEventListener setPlayerMultiplier(LivingMultiplier playerMultiplier) {
    this.playerMultiplier = playerMultiplier;
    return this;
  }

  public static class Serializer implements SkillEventListener.Serializer {
    @Override
    public SkillEventListener deserialize(JsonObject json) throws JsonParseException {
      KillEventListener listener = new KillEventListener();
      listener.setDamageCondition(SerializationHelper.deserializeDamageCondition(json));
      listener.setEnemyCondition(
          SerializationHelper.deserializeLivingCondition(json, "enemy_condition"));
      listener.setPlayerCondition(
          SerializationHelper.deserializeLivingCondition(json, "player_condition"));
      listener.setEnemyMultiplier(
          SerializationHelper.deserializeLivingMultiplier(json, "enemy_multiplier"));
      listener.setPlayerMultiplier(
          SerializationHelper.deserializeLivingMultiplier(json, "player_multiplier"));
      return listener;
    }

    @Override
    public void serialize(JsonObject json, SkillEventListener listener) {
      if (!(listener instanceof KillEventListener aListener)) {
        throw new IllegalArgumentException();
      }
      SerializationHelper.serializeDamageCondition(json, aListener.damageCondition);
      SerializationHelper.serializeLivingCondition(
          json, aListener.enemyCondition, "enemy_condition");
      SerializationHelper.serializeLivingCondition(
          json, aListener.playerCondition, "player_condition");
      SerializationHelper.serializeLivingMultiplier(
          json, aListener.enemyMultiplier, "enemy_multiplier");
      SerializationHelper.serializeLivingMultiplier(
          json, aListener.playerMultiplier, "player_multiplier");
    }

    @Override
    public SkillEventListener deserialize(CompoundTag tag) {
      KillEventListener listener = new KillEventListener();
      listener.setDamageCondition(SerializationHelper.deserializeDamageCondition(tag));
      listener.setEnemyCondition(
          SerializationHelper.deserializeLivingCondition(tag, "enemy_condition"));
      listener.setPlayerCondition(
          SerializationHelper.deserializeLivingCondition(tag, "player_condition"));
      listener.setEnemyMultiplier(
          SerializationHelper.deserializeLivingMultiplier(tag, "enemy_multiplier"));
      listener.setPlayerMultiplier(
          SerializationHelper.deserializeLivingMultiplier(tag, "player_multiplier"));
      return listener;
    }

    @Override
    public CompoundTag serialize(SkillEventListener listener) {
      if (!(listener instanceof KillEventListener aListener)) {
        throw new IllegalArgumentException();
      }
      CompoundTag tag = new CompoundTag();
      SerializationHelper.serializeDamageCondition(tag, aListener.damageCondition);
      SerializationHelper.serializeLivingCondition(
          tag, aListener.enemyCondition, "enemy_condition");
      SerializationHelper.serializeLivingCondition(
          tag, aListener.playerCondition, "player_condition");
      SerializationHelper.serializeLivingMultiplier(
          tag, aListener.enemyMultiplier, "enemy_multiplier");
      SerializationHelper.serializeLivingMultiplier(
          tag, aListener.playerMultiplier, "player_multiplier");
      return tag;
    }

    @Override
    public SkillEventListener deserialize(FriendlyByteBuf buf) {
      KillEventListener listener = new KillEventListener();
      listener.setDamageCondition(NetworkHelper.readDamageCondition(buf));
      listener.setEnemyCondition(NetworkHelper.readLivingCondition(buf));
      listener.setPlayerCondition(NetworkHelper.readLivingCondition(buf));
      listener.setEnemyMultiplier(NetworkHelper.readLivingMultiplier(buf));
      listener.setPlayerMultiplier(NetworkHelper.readLivingMultiplier(buf));
      return listener;
    }

    @Override
    public void serialize(FriendlyByteBuf buf, SkillEventListener listener) {
      if (!(listener instanceof KillEventListener aListener)) {
        throw new IllegalArgumentException();
      }
      NetworkHelper.writeDamageCondition(buf, aListener.damageCondition);
      NetworkHelper.writeLivingCondition(buf, aListener.enemyCondition);
      NetworkHelper.writeLivingCondition(buf, aListener.playerCondition);
      NetworkHelper.writeLivingMultiplier(buf, aListener.enemyMultiplier);
      NetworkHelper.writeLivingMultiplier(buf, aListener.playerMultiplier);
    }

    @Override
    public SkillEventListener createDefaultInstance() {
      return new KillEventListener();
    }
  }
}
