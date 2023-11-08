package daripher.skilltree;

import daripher.skilltree.config.Config;
import daripher.skilltree.init.PSTAttributes;
import daripher.skilltree.init.PSTCreativeTabs;
import daripher.skilltree.init.PSTEffects;
import daripher.skilltree.init.PSTItems;
import daripher.skilltree.init.PSTRecipeSerializers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SkillTreeMod.MOD_ID)
public class SkillTreeMod {
	public static final String MOD_ID = "skilltree";
	public static final Logger logger = LogManager.getLogger(MOD_ID);

	public SkillTreeMod() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		PSTItems.REGISTRY.register(bus);
		PSTAttributes.REGISTRY.register(bus);
		PSTRecipeSerializers.REGISTRY.register(bus);
		PSTEffects.REGISTRY.register(bus);
		PSTCreativeTabs.REGISTRY.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}
}
