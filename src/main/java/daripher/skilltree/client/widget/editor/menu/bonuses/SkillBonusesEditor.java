package daripher.skilltree.client.widget.editor.menu.bonuses;

import daripher.skilltree.client.tooltip.TooltipHelper;
import daripher.skilltree.client.widget.editor.SkillTreeEditor;
import daripher.skilltree.client.widget.editor.menu.EditorMenu;
import daripher.skilltree.client.widget.picker.SkillBonusPicker;
import daripher.skilltree.skill.PassiveSkill;
import daripher.skilltree.skill.bonus.SkillBonus;
import java.util.List;
import net.minecraft.ChatFormatting;

public class SkillBonusesEditor extends EditorMenu {
  public SkillBonusesEditor(SkillTreeEditor editor, EditorMenu previousMenu) {
    super(editor, previousMenu);
  }

  @Override
  public void init() {
    editor.addButton(0, 0, 90, 14, "Back").setPressFunc(b -> editor.selectMenu(previousMenu));
    editor.increaseHeight(29);
    if (!canEditBonuses(editor)) return;
    PassiveSkill selectedSkill = editor.getFirstSelectedSkill();
    if (selectedSkill == null) return;
    List<SkillBonus<?>> bonuses = selectedSkill.getBonuses();
    for (int i = 0; i < bonuses.size(); i++) {
      final int bonusIndex = i;
      SkillBonus<?> bonus = bonuses.get(i);
      String message = bonus.getTooltip().getString();
      message = TooltipHelper.getTrimmedString(message, 190);
      editor
          .addButton(0, 0, 200, 14, message)
          .setPressFunc(b -> editor.selectMenu(new SkillBonusEditor(editor, this, bonusIndex)));
      editor.increaseHeight(19);
    }
    editor.increaseHeight(10);
    editor.addLabel(0, 0, "Add Bonus", ChatFormatting.GOLD);
    editor.increaseHeight(19);
    SkillBonusPicker bonusPicker = editor.addSkillBonusPicker(0, 0, 200, 14, 10);
    editor.increaseHeight(19);
    editor.addButton(0, 0, 90, 14, "Add").setPressFunc(b -> addSkillBonuses(editor, bonusPicker));
    editor.increaseHeight(19);
  }

  private void addSkillBonuses(SkillTreeEditor editor, SkillBonusPicker bonusPicker) {
    editor.getSelectedSkills().forEach(s -> s.getBonuses().add(bonusPicker.getValue().copy()));
    editor.rebuildWidgets();
    editor.saveSelectedSkills();
  }

  private boolean canEditBonuses(SkillTreeEditor editor) {
    PassiveSkill selectedSkill = editor.getFirstSelectedSkill();
    if (selectedSkill == null) return false;
    for (PassiveSkill otherSkill : editor.getSelectedSkills()) {
      if (otherSkill == selectedSkill) continue;
      List<SkillBonus<?>> bonuses = otherSkill.getBonuses();
      List<SkillBonus<?>> otherBonuses = selectedSkill.getBonuses();
      if (bonuses.size() != otherBonuses.size()) return false;
      for (int i = 0; i < bonuses.size(); i++) {
        if (!bonuses.get(i).sameBonus(otherBonuses.get(i))) return false;
      }
    }
    return true;
  }
}
