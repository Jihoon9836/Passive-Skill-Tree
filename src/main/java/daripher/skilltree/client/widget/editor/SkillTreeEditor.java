package daripher.skilltree.client.widget.editor;

import daripher.skilltree.client.data.SkillTreeClientData;
import daripher.skilltree.client.widget.*;
import daripher.skilltree.client.widget.editor.menu.EditorMenu;
import daripher.skilltree.client.widget.editor.menu.MainEditorMenu;
import daripher.skilltree.client.widget.group.WidgetGroup;
import daripher.skilltree.client.widget.picker.AttributePicker;
import daripher.skilltree.client.widget.picker.SkillBonusPicker;
import daripher.skilltree.client.widget.skill.SkillButton;
import daripher.skilltree.client.widget.skill.SkillTreeButtons;
import daripher.skilltree.skill.PassiveSkill;
import daripher.skilltree.skill.PassiveSkillTree;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class SkillTreeEditor extends WidgetGroup<AbstractWidget> {
  private final SkillTreeButtons skillButtons;
  private final SkillSelector skillSelector;
  private final SkillMirrorer skillMirrorer;
  private final SkillDragger skillDragger;
  private @NotNull EditorMenu selectedMenu = new MainEditorMenu(this);

  public SkillTreeEditor(SkillTreeButtons skillButtons) {
    super(0, 0, 0, 0);
    this.skillButtons = skillButtons;
    this.skillSelector = new SkillSelector(this, skillButtons);
    this.skillMirrorer = new SkillMirrorer(this);
    this.skillDragger = new SkillDragger(this);
  }

  public void init() {
    clearWidgets();
    addWidget(selectedMenu);
    addWidget(skillSelector);
    addWidget(skillDragger);
    addWidget(skillMirrorer);
    selectedMenu.init();
  }

  @Override
  public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    skillMirrorer.render(graphics, mouseX, mouseY, partialTick);
    if (!skillSelector.getSelectedSkills().isEmpty()) {
      graphics.fill(getX(), getY(), getX() + width, getY() + height, 0xDD000000);
    }
    super.render(graphics, mouseX, mouseY, partialTick);
  }

//  @Override
//  public boolean mouseClicked(double mouseX, double mouseY, int button) {
//    return selectedMenu.mouseClicked(mouseX, mouseY, button)
//        || skillSelector.mouseClicked(mouseX, mouseY, button)
//            || skillDragger.mouseClicked(mouseX, mouseY, button)
//            || skillMirrorer.mouseClicked(mouseX, mouseY, button);
//  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
      return super.keyPressed(keyCode, scanCode, modifiers);
    }
    if (selectedMenu.previousMenu != null) {
      selectMenu(selectedMenu.previousMenu);
      return true;
    }
    if (!skillSelector.getSelectedSkills().isEmpty()) {
      skillSelector.clearSelection();
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  public void selectMenu(EditorMenu menu) {
    if (menu != null) {
      selectedMenu = menu;
      rebuildWidgets();
    }
  }

  public Button addButton(int x, int y, int width, int height, String message) {
    return addButton(x, y, width, height, Component.literal(message));
  }

  public Button addButton(int x, int y, int width, int height, Component message) {
    return addWidget(new Button(getWidgetsX(x), getWidgetsY(y), width, height, message));
  }

  public ConfirmationButton addConfirmationButton(
      int x, int y, int width, int height, String message, String confirmationMessage) {
    ConfirmationButton button =
        new ConfirmationButton(
            getWidgetsX(x), getWidgetsY(y), width, height, Component.literal(message));
    button.setConfirmationMessage(Component.literal(confirmationMessage));
    return addWidget(button);
  }

  public TextField addTextField(int x, int y, int width, int height, String defaultValue) {
    return addWidget(new TextField(getWidgetsX(x), getWidgetsY(y), width, height, defaultValue));
  }

  public NumericTextField addNumericTextField(
      int x, int y, int width, int height, double defaultValue) {
    return addWidget(
        new NumericTextField(getWidgetsX(x), getWidgetsY(y), width, height, defaultValue));
  }

  public TextArea addTextArea(int x, int y, int width, int height, String defaultValue) {
    return addWidget(new TextArea(getWidgetsX(x), getWidgetsY(y), width, height, defaultValue));
  }

  public Label addLabel(int x, int y, String text, ChatFormatting... styles) {
    MutableComponent message = Component.literal(text);
    for (ChatFormatting style : styles) {
      message.withStyle(style);
    }
    return addWidget(new Label(getWidgetsX(x), getWidgetsY(y), message));
  }

  public CheckBox addCheckBox(int x, int y, boolean value) {
    return addWidget(new CheckBox(getWidgetsX(x), getWidgetsY(y), value));
  }

  public <T> DropDownList<T> addDropDownList(
      int x, int y, int width, int height, int maxDisplayed, T defaultValue, Collection<T> values) {
    return addWidget(
        new DropDownList<>(
            getWidgetsX(x), getWidgetsY(y), width, height, maxDisplayed, values, defaultValue));
  }

  public <T extends Enum<T>> DropDownList<T> addDropDownList(
      int x, int y, int width, int height, int maxDisplayed, T defaultValue) {
    Class<T> enumClass = (Class<T>) defaultValue.getClass();
    List<T> enums = Stream.of(enumClass.getEnumConstants()).map(enumClass::cast).toList();
    return addDropDownList(x, y, width, height, maxDisplayed, defaultValue, enums);
  }

  public AttributePicker addAttributePicker(
      int x, int y, int width, int height, int maxDisplayed, Attribute defaultValue) {
    return addWidget(
        new AttributePicker(
            getWidgetsX(x), getWidgetsY(y), width, height, maxDisplayed, defaultValue));
  }

  public SkillBonusPicker addSkillBonusPicker(
      int x, int y, int width, int height, int maxDisplayed) {
    return addWidget(
        new SkillBonusPicker(getWidgetsX(x), getWidgetsY(y), width, height, maxDisplayed));
  }

  public void addMirrorerWidgets() {
    skillMirrorer.init();
  }

  public Set<PassiveSkill> getSelectedSkills() {
    return skillSelector.getSelectedSkills();
  }

  @Nullable
  public PassiveSkill getFirstSelectedSkill() {
    return skillSelector.getFirstSelectedSkill();
  }

  public SkillMirrorer getSkillMirrorer() {
    return skillMirrorer;
  }

  public void saveSelectedSkills() {
    skillSelector.getSelectedSkills().forEach(SkillTreeClientData::saveEditorSkill);
  }

  private int getWidgetsY(int y) {
    return getHeight() + y;
  }

  private int getWidgetsX(int x) {
    return getX() + 5 + x;
  }

  public float getScrollX() {
    return skillButtons.getScrollX();
  }

  public float getScrollY() {
    return skillButtons.getScrollY();
  }

  public float getZoom() {
    return skillButtons.getZoom();
  }

  public void increaseHeight(int delta) {
    setHeight(getHeight() + delta);
  }

  public PassiveSkillTree getSkillTree() {
    return skillButtons.getSkillTree();
  }

  public List<PassiveSkill> getSkills() {
    return getSkillTree().getSkillIds().stream().map(SkillTreeClientData::getEditorSkill).toList();
  }

  public Collection<SkillButton> getSkillButtons() {
    return skillButtons.getWidgets();
  }

  public void addSkillButton(PassiveSkill skill) {
    SkillButton button = skillButtons.addSkillButton(skill, () -> 0f);
    button.skillLearned = true;
  }

  public void updateSkillConnections() {
    skillButtons.updateSkillConnections();
  }

  @Override
  public void rebuildWidgets() {
    super.rebuildWidgets();
    updateSkillConnections();
  }

  public boolean canEdit(Function<PassiveSkill, ?> function) {
    return getSelectedSkills().stream().map(function).distinct().count() <= 1;
  }

  public void removeSkillButton(PassiveSkill skill) {
    skillButtons.getWidgets().removeIf(button -> button.skill == skill);
  }

  public SkillButton getSkillButton(ResourceLocation skillId) {
    return skillButtons.getWidgetById(skillId);
  }
}
