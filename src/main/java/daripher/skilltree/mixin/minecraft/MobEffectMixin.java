package daripher.skilltree.mixin.minecraft;

import daripher.skilltree.init.PSTAttributes;
import daripher.skilltree.skill.bonus.SkillBonusHandler;
import daripher.skilltree.skill.bonus.player.LethalPoisonBonus;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin implements IForgeMobEffect {
  @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
  public void inflictPoisonDamage(
      LivingEntity livingEntity, int amplifier, CallbackInfo callbackInfo) {
    //noinspection ConstantValue
    if (((Object) this) != MobEffects.POISON) return;
    handlePoisonDamage(livingEntity);
  }

  private static void handlePoisonDamage(LivingEntity livingEntity) {
    LivingEntity attacker = livingEntity.getKillCredit();
    float damage = getPoisonDamage(attacker);
    boolean isLowHealth = livingEntity.getHealth() <= damage;
    boolean isPoisonLethal = isPoisonLethal(attacker);
    if (isLowHealth && !isPoisonLethal) return;
    DamageSources damageSources = livingEntity.damageSources();
    DamageSource damageSource = damageSources.magic();
    if (attacker instanceof Player player) {
      damageSource = damageSources.indirectMagic(player, null);
      // resets hurt timer
      livingEntity.setLastHurtByPlayer(player);
    }
    SkillBonusHandler.forcefullyInflictDamage(damageSource, damage, livingEntity);
  }

  private static boolean isPoisonLethal(LivingEntity attacker) {
    return attacker instanceof Player player
        && !SkillBonusHandler.getSkillBonuses(player, LethalPoisonBonus.class).isEmpty();
  }

  private static float getPoisonDamage(LivingEntity attacker) {
    float damage = 1f;
    if (attacker != null) {
      Attribute poisonDamage = PSTAttributes.POISON_DAMAGE.get();
      if (attacker.getAttributes().hasAttribute(poisonDamage)) {
        damage = (float) attacker.getAttributeValue(poisonDamage);
      }
    }
    return damage;
  }
}
