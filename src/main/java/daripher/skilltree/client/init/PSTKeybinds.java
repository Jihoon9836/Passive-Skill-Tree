package daripher.skilltree.client.init;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.client.data.SkillTreeClientData;
import daripher.skilltree.client.screen.SkillTreeScreen;
import daripher.skilltree.data.reloader.SkillTreesReloader;
import daripher.skilltree.data.reloader.SkillsReloader;
import daripher.skilltree.skill.PassiveSkillTree;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = SkillTreeMod.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class PSTKeybinds {
  private static final KeyMapping SKILL_TREE_KEY = new KeyMapping("key.display_skill_tree", GLFW.GLFW_KEY_O, "key.categories." + SkillTreeMod.MOD_ID);

  @SubscribeEvent
  public static void registerKeybinds(RegisterKeyMappingsEvent event) {
    event.register(SKILL_TREE_KEY);
  }

  @EventBusSubscriber(modid = SkillTreeMod.MOD_ID, value = Dist.CLIENT)
  private static class KeyEvents {
    @SubscribeEvent
    public static void keyPressed(InputEvent.Key event) {
      Minecraft minecraft = Minecraft.getInstance();
      if (event.getAction() != GLFW.GLFW_PRESS) return;
      if (minecraft.screen != null) return;
      if (minecraft.player == null) return;
      if (event.getKey() == SKILL_TREE_KEY.getKey().getValue()) {
        ResourceLocation treeId = new ResourceLocation(SkillTreeMod.MOD_ID, "main_tree");
        PassiveSkillTree skillTree = SkillTreesReloader.getSkillTreeById(treeId);
        for (ResourceLocation skillId : skillTree.getSkillIds()) {
          if (SkillsReloader.getSkillById(skillId) == null) {
            SkillTreeClientData.printMessage("This skill tree is broken.", ChatFormatting.DARK_RED);
            SkillTreeClientData.printMessage("Open it in the editor to resolve issues.", ChatFormatting.RED);
            return;
          }
        }
        SkillTreeScreen screen = new SkillTreeScreen(treeId);
        minecraft.setScreen(screen);
      }
    }
  }
}
