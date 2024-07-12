package daripher.skilltree.client.widget.picker;

import daripher.skilltree.client.widget.DropDownList;
import daripher.skilltree.init.PSTSkillBonuses;
import daripher.skilltree.skill.bonus.SkillBonus;
import net.minecraft.network.chat.Component;

@SuppressWarnings("rawtypes")
public class SkillBonusPicker extends DropDownList<SkillBonus> {
  public SkillBonusPicker(int x, int y, int width, int height, int maxDisplayed) {
    super(
        x,
        y,
        width,
        height,
        maxDisplayed,
        PSTSkillBonuses.bonusList(),
        PSTSkillBonuses.ATTRIBUTE.get().createDefaultInstance());
    setToNameFunc(b -> Component.literal(PSTSkillBonuses.getName(b)));
  }
}
