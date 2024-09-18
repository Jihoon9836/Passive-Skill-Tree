package daripher.skilltree.mixin.minecraft;

import daripher.skilltree.init.PSTAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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
    if (((Object) this) == MobEffects.POISON) {
      // poison lethality check
//      if (!(livingEntity.getHealth() > 1f)) return;
      LivingEntity attacker = livingEntity.getKillCredit();
      float damage = 1f;
      if (attacker != null
          && attacker.getAttributes().hasAttribute(PSTAttributes.POISON_DAMAGE.get())) {
        damage = (float) attacker.getAttributeValue(PSTAttributes.POISON_DAMAGE.get());
      }
      livingEntity.hurt(livingEntity.damageSources().magic(), damage);
    }
  }
}
