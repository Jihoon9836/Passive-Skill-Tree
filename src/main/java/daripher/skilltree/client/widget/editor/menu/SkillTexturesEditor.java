package daripher.skilltree.client.widget.editor.menu;

import daripher.skilltree.client.data.SkillTexturesData;
import daripher.skilltree.client.tooltip.TooltipHelper;
import daripher.skilltree.client.widget.editor.SkillTreeEditor;
import daripher.skilltree.skill.PassiveSkill;
import net.minecraft.ChatFormatting;

public class SkillTexturesEditor extends EditorMenu {
  public SkillTexturesEditor(SkillTreeEditor editor, EditorMenu previousMenu) {
    super(editor, previousMenu);
  }

  @Override
  public void init() {
    editor.addButton(0, 0, 90, 14, "Back").setPressFunc(b -> editor.selectMenu(previousMenu));
    editor.increaseHeight(29);
    PassiveSkill selectedSkill = editor.getFirstSelectedSkill();
    if (selectedSkill == null) return;
    if (editor.canEdit(PassiveSkill::getBackgroundTexture)) {
      editor.addLabel(0, 0, "Border", ChatFormatting.GOLD);
      editor.increaseHeight(19);
      editor
          .addDropDownList(
              0, 0, 200, 14, 10, selectedSkill.getBackgroundTexture(), SkillTexturesData.BORDERS)
          .setToNameFunc(TooltipHelper::getTextureName)
          .setResponder(
              value -> {
                editor.getSelectedSkills().forEach(s -> s.setBackgroundTexture(value));
                editor.saveSelectedSkills();
              });
      editor.increaseHeight(19);
    }
    if (editor.canEdit(PassiveSkill::getBorderTexture)) {
      editor.addLabel(0, 0, "Tooltip", ChatFormatting.GOLD);
      editor.increaseHeight(19);
      editor
          .addDropDownList(
              0,
              0,
              200,
              14,
              10,
              selectedSkill.getBorderTexture(),
              SkillTexturesData.TOOLTIP_BACKGROUNDS)
          .setToNameFunc(TooltipHelper::getTextureName)
          .setResponder(
              value -> {
                editor.getSelectedSkills().forEach(s -> s.setBorderTexture(value));
                editor.saveSelectedSkills();
              });
      editor.increaseHeight(19);
    }
    if (editor.canEdit(PassiveSkill::getIconTexture)) {
      editor.addLabel(0, 0, "Icon", ChatFormatting.GOLD);
      editor.increaseHeight(19);
      editor
          .addDropDownList(
              0, 0, 200, 14, 10, selectedSkill.getIconTexture(), SkillTexturesData.ICONS)
          .setToNameFunc(TooltipHelper::getTextureName)
          .setResponder(
              value -> {
                editor.getSelectedSkills().forEach(s -> s.setIconTexture(value));
                editor.saveSelectedSkills();
              });
      editor.increaseHeight(19);
    }
  }
}
