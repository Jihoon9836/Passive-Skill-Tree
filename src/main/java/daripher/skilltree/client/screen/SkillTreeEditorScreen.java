package daripher.skilltree.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.client.data.SkillTexturesData;
import daripher.skilltree.client.data.SkillTreeClientData;
import daripher.skilltree.client.tooltip.TooltipHelper;
import daripher.skilltree.client.widget.*;
import daripher.skilltree.client.widget.Button;
import daripher.skilltree.init.PSTSkillBonuses;
import daripher.skilltree.skill.PassiveSkill;
import daripher.skilltree.skill.PassiveSkillTree;
import daripher.skilltree.skill.bonus.SkillBonus;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;

public class SkillTreeEditorScreen extends Screen {
  private final Map<ResourceLocation, SkillButton> skillButtons = new HashMap<>();
  private final List<SkillConnection> skillConnections = new ArrayList<>();
  private final Set<ResourceLocation> selectedSkills = new LinkedHashSet<>();
  private final PassiveSkillTree skillTree;
  private Tools selectedTools = Tools.MAIN;
  protected float scrollSpeedX;
  protected float scrollSpeedY;
  protected float scrollX;
  protected float scrollY;
  protected int maxScrollX;
  protected int maxScrollY;
  protected int toolsY;
  protected int toolsX;
  private boolean closeOnEsc = true;
  private int prevMouseX;
  private int prevMouseY;
  private float zoom = 1F;
  private int selectedSubMenu = -1;
  private int dragX;
  private int dragY;
  private boolean selectingArea;
  private boolean mirroring;
  private float mirrorAngle;
  private int mirrorSides = 2;
  private float mirrorCenterX;
  private float mirrorCenterY;

  public SkillTreeEditorScreen(ResourceLocation skillTreeId) {
    super(Component.empty());
    this.minecraft = Minecraft.getInstance();
    this.skillTree = SkillTreeClientData.getOrCreateEditorTree(skillTreeId);
  }

  @Override
  public void init() {
    if (skillTree == null) {
      getMinecraft().setScreen(null);
      return;
    }
    clearWidgets();
    addSkillButtons();
    maxScrollX -= width / 2 - 350;
    maxScrollY -= height / 2 - 350;
    if (maxScrollX < 0) maxScrollX = 0;
    if (maxScrollY < 0) maxScrollY = 0;
    addSkillConnections();
    addToolButtons();
  }

  @Override
  public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    updateScroll(partialTick);
    renderBackground(graphics);
    renderConnections(graphics, mouseX, mouseY);
    renderSkills(graphics, mouseX, mouseY, partialTick);
    renderMirrorMode(graphics);
    renderOverlay(graphics);
    renderWidgets(graphics, mouseX, mouseY, partialTick);
    renderSkillTooltip(graphics, mouseX, mouseY, partialTick);
    renderSkillSelection(graphics, mouseX, mouseY);
    prevMouseX = mouseX;
    prevMouseY = mouseY;
  }

  private void renderMirrorMode(@NotNull GuiGraphics graphics) {
    if (!mirroring) return;
    graphics.pose().pushPose();
    graphics
        .pose()
        .translate(width / 2f + mirrorCenterX + scrollX, height / 2f + mirrorCenterY + scrollY, 0);
    graphics.pose().mulPose(Axis.ZP.rotationDegrees(mirrorAngle));
    for (int i = 0; i < mirrorSides; i++) {
      graphics.pose().mulPose(Axis.ZP.rotationDegrees(360f / mirrorSides));
      graphics.fill(-1, -1, 1, width * 2, 0x55CFCFCF);
    }
    ScreenHelper.drawRectangle(graphics, -4, -4, 8, 8, 0x55CFCFCF);
    graphics.pose().popPose();
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
  }

  private void renderWidgets(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    if (!selectedSkills.isEmpty()) {
      graphics.fill(toolsX - 10, 0, width, toolsY, 0xDD000000);
    }
    for (Renderable widget : renderables) {
      if (widget instanceof SkillButton) continue;
      widget.render(graphics, mouseX, mouseY, partialTick);
    }
    graphics.pose().pushPose();
    graphics.pose().translate(0, 0, 1f);
    widgets()
        .filter(DropDownList.class::isInstance)
        .map(DropDownList.class::cast)
        .forEach(w -> w.renderList(graphics));
    graphics.pose().popPose();
  }

  private void renderSkillTooltip(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    if (getWidgetAt(mouseX, mouseY).isPresent()) return;
    SkillButton skillAtMouse = getSkillAt(mouseX, mouseY);
    float tooltipX = mouseX + (prevMouseX - mouseX) * partialTick;
    float tooltipY = mouseY + (prevMouseY - mouseY) * partialTick;
    if (skillAtMouse == null) return;
    ScreenHelper.renderSkillTooltip(
        skillTree, skillAtMouse, graphics, tooltipX, tooltipY, width, height);
  }

  private void renderSkills(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    graphics.pose().pushPose();
    graphics.pose().translate(scrollX, scrollY, 0);
    for (SkillButton widget : skillButtons.values()) {
      graphics.pose().pushPose();
      double widgetCenterX = widget.x + widget.getWidth() / 2f;
      double widgetCenterY = widget.y + widget.getHeight() / 2f;
      graphics.pose().translate(widgetCenterX, widgetCenterY, 0F);
      graphics.pose().scale(zoom, zoom, 1F);
      graphics.pose().translate(-widgetCenterX, -widgetCenterY, 0F);
      widget.render(graphics, mouseX, mouseY, partialTick);
      if (selectedSkills.contains(widget.skill.getId())) {
        graphics.pose().pushPose();
        graphics.pose().translate(widget.x, widget.y, 0);
        renderSkillSelection(graphics, widget);
        graphics.pose().popPose();
      }
      graphics.pose().popPose();
    }
    graphics.pose().popPose();
  }

  private void renderSkillSelection(GuiGraphics graphics, int mouseX, int mouseY) {
    if (!selectingArea) return;
    ScreenHelper.drawRectangle(graphics, dragX, dragY, mouseX - dragX, mouseY - dragY, 0xEE95EB34);
  }

  private void renderSkillSelection(GuiGraphics graphics, SkillButton widget) {
    ScreenHelper.drawRectangle(
        graphics, -1, -1, widget.getWidth() + 2, widget.getHeight() + 2, 0xAA32FF00);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (clickedWidget(mouseX, mouseY, button)) {
      return true;
    }
    if (hasShiftDown() && button == 0) {
      selectingArea = true;
      dragX = (int) mouseX;
      dragY = (int) mouseY;
    }
    return clickedSkill(mouseX, mouseY);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (selectingArea) {
      addSelectedSkillsToSelection(mouseX, mouseY);
      selectingArea = false;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  private void addSelectedSkillsToSelection(double mouseX, double mouseY) {
    double sx1 = dragX - scrollX;
    double sx2 = mouseX - scrollX;
    double sy1 = dragY - scrollY;
    double sy2 = mouseY - scrollY;
    if (sx1 > sx2) {
      double temp = sx1;
      sx1 = sx2;
      sx2 = temp;
    }
    if (sy1 < sy2) {
      double temp = sy1;
      sy1 = sy2;
      sy2 = temp;
    }
    for (SkillButton skill : skillButtons.values()) {
      double skillSize = skill.skill.getButtonSize() * zoom;
      double bx1 = skill.x + skill.getWidth() / 2d - skillSize / 2;
      double bx2 = bx1 + skillSize;
      double by2 = skill.y + skill.getHeight() / 2d - skillSize / 2;
      double by1 = by2 + skillSize;
      if (overlap(sx1, sx2, sy1, sy2, bx1, bx2, by1, by2)) {
        selectedSkills.add(skill.skill.getId());
      }
    }
    rebuildWidgets();
  }

  private boolean overlap(
      double x1, double x2, double y1, double y2, double x3, double x4, double y3, double y4) {
    return x1 < x4 && x2 > x3 && y1 > y4 && y2 < y3;
  }

  private boolean clickedWidget(double mouseX, double mouseY, int button) {
    boolean clicked = false;
    for (GuiEventListener child : widgets().toList()) {
      if (child.mouseClicked(mouseX, mouseY, button)) {
        setFocused(child);
        clicked = true;
      }
    }
    return clicked;
  }

  private boolean clickedSkill(double mouseX, double mouseY) {
    SkillButton skill = getSkillAt(mouseX, mouseY);
    if (skill == null) return false;
    playButtonSound();
    skillButtonPressed(skill);
    return true;
  }

  private void playButtonSound() {
    getMinecraft()
        .getSoundManager()
        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
  }

  private List<AbstractWidget> textFields() {
    List<AbstractWidget> list = new ArrayList<>();
    for (GuiEventListener widget : children()) {
      if (isTextField(widget)) {
        list.add((AbstractWidget) widget);
      }
    }
    return list;
  }

  @SuppressWarnings("rawtypes")
  private List<DropDownList> dropDownLists() {
    List<DropDownList> list = new ArrayList<>();
    for (GuiEventListener widget : children()) {
      if (widget instanceof DropDownList) {
        list.add((DropDownList) widget);
      }
    }
    return list;
  }

  private boolean isTextField(GuiEventListener widget) {
    return widget instanceof EditBox || widget instanceof MultiLineEditBox;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
      if (selectedSubMenu != -1) {
        selectSubMenu(-1);
        closeOnEsc = false;
        return true;
      }
      if (selectedTools != Tools.MAIN) {
        selectTools(Tools.MAIN);
        closeOnEsc = false;
        return true;
      }
      if (!selectedSkills.isEmpty()) {
        selectedSkills.clear();
        rebuildWidgets();
        closeOnEsc = false;
        return true;
      }
      if (shouldCloseOnEsc()) {
        onClose();
        return true;
      }
    }
    if (keyCode == GLFW.GLFW_KEY_N && Screen.hasControlDown()) {
      createNewSkill(0, 0, null);
      rebuildWidgets();
      return true;
    }
    return keyPressedOnTextField(keyCode, scanCode, modifiers);
  }

  private boolean keyPressedOnTextField(int keyCode, int scanCode, int modifiers) {
    return textFields().stream().anyMatch(b -> b.keyPressed(keyCode, scanCode, modifiers));
  }

  @Override
  public boolean shouldCloseOnEsc() {
    if (!closeOnEsc) {
      closeOnEsc = true;
      return false;
    }
    return super.shouldCloseOnEsc();
  }

  private void removeSelectedSkills() {
    selectedSkills()
        .forEach(
            skill -> {
              skillTree.getSkillIds().remove(skill.getId());
              SkillTreeClientData.deleteEditorSkill(skill);
              SkillTreeClientData.saveEditorSkillTree(skillTree);
            });
    selectedSkills.clear();
    rebuildWidgets();
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    textFields().forEach(b -> b.keyReleased(keyCode, scanCode, modifiers));
    return super.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char character, int keyCode) {
    for (AbstractWidget textField : textFields()) {
      if (textField.charTyped(character, keyCode)) {
        return true;
      }
    }
    for (DropDownList<?> dropDownList : dropDownLists()) {
      if (dropDownList.charTyped(character, keyCode)) {
        return true;
      }
    }
    return false;
  }

  private Optional<? extends GuiEventListener> getWidgetAt(double mouseX, double mouseY) {
    Optional<? extends GuiEventListener> openedList =
        widgets()
            .filter(DropDownList.class::isInstance)
            .map(DropDownList.class::cast)
            .filter(w -> w.isOpened() && w.isMouseOver(mouseX, mouseY))
            .findFirst();
    if (openedList.isPresent()) return openedList;
    return widgets().filter(w -> w.isMouseOver(mouseX, mouseY)).findFirst();
  }

  private Stream<? extends GuiEventListener> widgets() {
    return children().stream().filter(Predicate.not(SkillButton.class::isInstance));
  }

  private @Nullable SkillButton getSkillAt(double mouseX, double mouseY) {
    if (mouseX > toolsX && mouseY < toolsY) return null;
    mouseX -= scrollX;
    mouseY -= scrollY;
    for (SkillButton button : skillButtons.values()) {
      double skillSize = button.skill.getButtonSize() * zoom;
      double skillX = button.x + button.getWidth() / 2d - skillSize / 2;
      double skillY = button.y + button.getHeight() / 2d - skillSize / 2;
      if (mouseX >= skillX
          && mouseY >= skillY
          && mouseX < skillX + skillSize
          && mouseY < skillY + skillSize) {
        return button;
      }
    }
    return null;
  }

  private void addSkillButtons() {
    skillButtons.clear();
    getTreeSkills().forEach(this::addSkillButton);
  }

  private void addToolButtons() {
    if (selectedSkills.isEmpty()) return;
    toolsX = width - 210;
    toolsY = 10;
    switch (selectedTools) {
      case MAIN -> addMainTools();
      case TEXTURES -> addTexturesTools();
      case BUTTON -> addButtonTools();
      case BONUSES -> addBonusesTools();
      case NODE -> addNodeToolsButtons();
      case TAGS -> addTagsToolsButtons();
      case DESCRIPTION -> addDescriptionsToolsButtons();
      case CONNECTIONS -> addConnectionToolsButton();
    }
    toolsY += 5;
  }

  private void addBonusesTools() {
    Button backButton = addButton(0, 0, 90, 14, "Back");
    if (selectedSubMenu == -1) {
      backButton.setPressFunc(b -> selectTools(Tools.MAIN));
    } else {
      backButton.setPressFunc(b -> selectSubMenu(-1));
      addConfirmationButton(110, 0, 90, 14, "Remove", "Confirm")
          .setPressFunc(
              b -> {
                selectedSkills().forEach(s -> removeSkillBonus(s, selectedSubMenu));
                selectSubMenu(-1);
                saveSelectedSkills();
                rebuildWidgets();
              });
    }
    shiftWidgets(0, 29);
    PassiveSkill skill = getFirstSelectedSkill();
    if (selectedSkills().anyMatch(otherSkill -> !sameBonuses(skill, otherSkill))) return;
    List<SkillBonus<?>> bonuses = skill.getBonuses();
    if (selectedSubMenu >= bonuses.size()) {
      selectedSubMenu = -1;
    }
    if (selectedSubMenu == -1) {
      for (int i = 0; i < bonuses.size(); i++) {
        final int index = i;
        SkillBonus<?> bonus = bonuses.get(i);
        String message = bonus.getTooltip().getString();
        message = TooltipHelper.getTrimmedMessage(font, message, 190);
        addButton(0, 0, 200, 14, message).setPressFunc(b -> selectSubMenu(index));
        shiftWidgets(0, 19);
      }
    } else {
      skill
          .getBonuses()
          .get(selectedSubMenu)
          .addEditorWidgets(
              this,
              selectedSubMenu,
              b -> {
                selectedSkills().forEach(s -> s.getBonuses().set(selectedSubMenu, b.copy()));
                saveSelectedSkills();
              });
    }
    if (selectedSubMenu == -1) {
      shiftWidgets(0, 10);
      addLabel(0, 0, "Add Bonus", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      SkillBonus<?> defaultBonusType = PSTSkillBonuses.ATTRIBUTE.get().createDefaultInstance();
      @SuppressWarnings("rawtypes")
      DropDownList<SkillBonus> bonusTypeSelection =
          addDropDownList(0, 0, 200, 14, 10, defaultBonusType, PSTSkillBonuses.bonusList())
              .setToNameFunc(b -> Component.literal(PSTSkillBonuses.getName(b)));
      shiftWidgets(0, 19);
      addButton(0, 0, 90, 14, "Add")
          .setPressFunc(
              b -> {
                selectedSkills()
                    .forEach(s -> s.getBonuses().add(bonusTypeSelection.getValue().copy()));
                rebuildWidgets();
                saveSelectedSkills();
              });
      shiftWidgets(0, 19);
    }
  }

  private void selectSubMenu(int index) {
    selectedSubMenu = index;
    rebuildWidgets();
  }

  private void addMainTools() {
    addButton(0, 0, 200, 14, "Bonuses").setPressFunc(b -> selectTools(Tools.BONUSES));
    shiftWidgets(0, 19);
    addButton(0, 0, 200, 14, "Textures").setPressFunc(b -> selectTools(Tools.TEXTURES));
    shiftWidgets(0, 19);
    addButton(0, 0, 200, 14, "Button").setPressFunc(b -> selectTools(Tools.BUTTON));
    shiftWidgets(0, 19);
    addButton(0, 0, 200, 14, "New Skill").setPressFunc(b -> selectTools(Tools.NODE));
    shiftWidgets(0, 19);
    addButton(0, 0, 200, 14, "Tags").setPressFunc(b -> selectTools(Tools.TAGS));
    shiftWidgets(0, 19);
    addButton(0, 0, 200, 14, "Description").setPressFunc(b -> selectTools(Tools.DESCRIPTION));
    shiftWidgets(0, 19);
    if (selectedSkills.size() >= 2) {
      addButton(0, 0, 200, 14, "Connections").setPressFunc(b -> selectTools(Tools.CONNECTIONS));
      shiftWidgets(0, 19);
    }
    addConfirmationButton(0, 0, 200, 14, "Remove", "Confirm")
        .setPressFunc(b -> removeSelectedSkills());
    shiftWidgets(0, 19);
  }

  private void selectTools(Tools tools) {
    selectedTools = tools;
    rebuildWidgets();
  }

  private double lastUsedDistance = 10;
  private double lastUsedAngle = 0;

  private void addNodeToolsButtons() {
    addButton(0, 0, 90, 14, "Back").setPressFunc(b -> selectTools(Tools.MAIN));
    shiftWidgets(0, 29);
    if (selectedSkills.isEmpty()) return;
    addLabel(0, 0, "Distance", ChatFormatting.GOLD);
    addLabel(65, 0, "Angle", ChatFormatting.GOLD);
    shiftWidgets(0, 19);
    NumericTextField distanceEditor = addNumericTextField(0, 0, 60, 14, lastUsedDistance);
    distanceEditor.setNumericResponder(v -> lastUsedDistance = v);
    NumericTextField angleEditor = addNumericTextField(65, 0, 60, 14, lastUsedAngle);
    angleEditor.setNumericResponder(v -> lastUsedAngle = v);
    shiftWidgets(0, 19);
    addButton(0, 0, 60, 14, "Add")
        .setPressFunc(b -> createSkills(angleEditor, distanceEditor, this::createNewSkill));
    addButton(65, 0, 60, 14, "Copy")
        .setPressFunc(b -> createSkills(angleEditor, distanceEditor, this::createCopiedSkill));
    shiftWidgets(0, 19);
    addLabel(0, 0, "Mirror", ChatFormatting.GOLD);
    addCheckBox(186, 0, mirroring)
        .setResponder(
            v -> {
              mirroring = v;
              rebuildWidgets();
            });
    shiftWidgets(0, 19);
    if (mirroring) {
      addLabel(0, 0, "Sides", ChatFormatting.GOLD);
      addNumericTextField(160, 0, 40, 14, mirrorSides)
          .setNumericFilter(v -> v > 1)
          .setNumericResponder(v -> mirrorSides = v.intValue());
      shiftWidgets(0, 19);
      addLabel(0, 0, "Rotation", ChatFormatting.GOLD);
      addNumericTextField(160, 0, 40, 14, mirrorAngle)
          .setNumericResponder(v -> mirrorAngle = v.floatValue());
      shiftWidgets(0, 19);
      addLabel(0, 0, "Center", ChatFormatting.GOLD);
      addNumericTextField(160, 0, 40, 14, mirrorCenterX)
          .setNumericResponder(v -> mirrorCenterX = v.floatValue());
      addNumericTextField(115, 0, 40, 14, mirrorCenterY)
          .setNumericResponder(v -> mirrorCenterY = v.floatValue());
      if (selectedSkills.size() == 1) {
        addButton(70, 0, 40, 14, "Set")
            .setPressFunc(
                b -> {
                  PassiveSkill skill = getFirstSelectedSkill();
                  mirrorCenterX = skill.getPositionX();
                  mirrorCenterY = skill.getPositionY();
                  rebuildWidgets();
                });
        shiftWidgets(0, 19);
      }
    }
  }

  private void addTagsToolsButtons() {
    Button backButton = addButton(0, 0, 90, 14, "Back");
    shiftWidgets(0, 29);
    if (selectedSubMenu == -1) {
      backButton.setPressFunc(b -> selectTools(Tools.MAIN));
      if (selectedSkills.isEmpty()) return;
      PassiveSkill skill = getFirstSelectedSkill();
      if (selectedSkills().anyMatch(otherSkill -> !sameTags(skill, otherSkill))) return;
      addLabel(0, 0, "Tag List", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      List<String> tags = skill.getTags();
      for (int i = 0; i < tags.size(); i++) {
        int index = i;
        addTextField(0, 0, 200, 14, tags.get(i))
            .setResponder(
                v -> {
                  selectedSkills().forEach(s -> s.getTags().set(index, v));
                  saveSelectedSkills();
                });
        shiftWidgets(0, 19);
      }
      toolsY += 10;
      addButton(0, 0, 90, 14, "Add")
          .setPressFunc(
              b -> {
                String name = "New Tag";
                while (skill.getTags().contains(name)) {
                  name += "1";
                }
                String finalName = name;
                selectedSkills().forEach(s -> s.getTags().add(finalName));
                saveSelectedSkills();
                rebuildWidgets();
              });
      if (!tags.isEmpty()) {
        addButton(110, 0, 90, 14, "Remove")
            .setPressFunc(
                b -> {
                  selectedSkills().forEach(s -> s.getTags().remove(tags.size() - 1));
                  saveSelectedSkills();
                  rebuildWidgets();
                });
      }
      shiftWidgets(0, 19);
      addButton(0, 0, 200, 14, "Tree Limitations").setPressFunc(b -> selectSubMenu(0));
      shiftWidgets(0, 19);
    } else {
      backButton.setPressFunc(b -> selectSubMenu(-1));
      Map<String, Integer> limitations = skillTree.getSkillLimitations();
      List<String> tags = limitations.keySet().stream().toList();
      List<Pair<TextField, NumericTextField>> editors = new ArrayList<>();
      Runnable saveFunc =
          () -> {
            limitations.clear();
            for (Pair<TextField, NumericTextField> pair : editors) {
              int limit = (int) pair.getValue().getNumericValue();
              if (limit == 0) continue;
              String tag = pair.getKey().getValue();
              limitations.put(tag, limit);
            }
            SkillTreeClientData.saveEditorSkillTree(skillTree);
          };
      for (int i = 0; i < limitations.size(); i++) {
        TextField tagEditor = addTextField(0, 0, 155, 14, tags.get(i));
        NumericTextField limitEditor =
            addNumericTextField(160, 0, 40, 14, limitations.get(tags.get(i)));
        tagEditor.setResponder(v -> saveFunc.run());
        editors.add(Pair.of(tagEditor, limitEditor));
        limitEditor
            .setNumericFilter(d -> d >= 0)
            .setNumericResponder(
                v -> {
                  saveFunc.run();
                  if (v == 0) rebuildWidgets();
                });
        shiftWidgets(0, 19);
      }
      shiftWidgets(0, 10);
      addButton(0, 0, 90, 14, "Add")
          .setPressFunc(
              b -> {
                String name = "New Tag";
                while (limitations.containsKey(name)) {
                  name += "1";
                }
                limitations.put(name, 1);
                rebuildWidgets();
                SkillTreeClientData.saveEditorSkillTree(skillTree);
              });
      shiftWidgets(0, 19);
    }
  }

  private void addDescriptionsToolsButtons() {
    Button backButton = addButton(0, 0, 90, 14, "Back");
    if (selectedSkills.isEmpty()) return;
    PassiveSkill skill = getFirstSelectedSkill();
    if (selectedSkills().anyMatch(otherSkill -> !sameDescription(skill, otherSkill))) return;
    List<MutableComponent> description = skill.getDescription();
    if (selectedSubMenu == -1) {
      backButton.setPressFunc(b -> selectTools(Tools.MAIN));
      addConfirmationButton(110, 0, 90, 14, "Regenerate", "Confirm")
          .setPressFunc(b -> regenerateSelectedSkillsDescription());
      shiftWidgets(0, 29);
      if (description != null) {
        for (int i = 0; i < description.size(); i++) {
          int index = i;
          String message = description.get(i).getString();
          message = TooltipHelper.getTrimmedMessage(font, message, 190);
          addButton(0, 0, 200, 14, message).setPressFunc(b -> selectSubMenu(index));
          shiftWidgets(0, 19);
        }
      }
      shiftWidgets(0, 10);
      addButton(0, 0, 90, 14, "Add").setPressFunc(b -> addSelectedSkillsDescriptionLine());
      addConfirmationButton(110, 0, 90, 14, "Clear", "Confirm")
          .setPressFunc(b -> removeSelectedSkillsDescription());
      shiftWidgets(0, 19);
    } else {
      backButton.setPressFunc(b -> selectSubMenu(-1));
      addConfirmationButton(110, 0, 90, 14, "Remove", "Confirm")
          .setPressFunc(b -> removeSelectedSkillsDescriptionLine());
      shiftWidgets(0, 29);
      if (description == null || selectedSubMenu > description.size()) {
        selectSubMenu(-1);
        return;
      }
      MutableComponent component = description.get(selectedSubMenu);
      addTextArea(0, 0, 200, 70, component.getString())
          .setResponder(this::setSelectedSkillsDescription);
      shiftWidgets(0, 75);
      addLabel(0, 0, "Color", ChatFormatting.GOLD);
      Style style = component.getStyle();
      TextColor textColor = style.getColor();
      if (textColor == null) {
        textColor = TextColor.fromRgb(0xffffff);
      }
      String color = Integer.toHexString(textColor.getValue());
      addTextField(120, 0, 80, 14, color)
          .setSoftFilter(v -> v.matches("^#?[a-fA-F0-9]{6}"))
          .setResponder(
              v -> {
                int rgb = Integer.parseInt(formatColor(v), 16);
                setSelectedSkillsDescriptionStyle(s -> s.withColor(rgb));
              });
      shiftWidgets(0, 19);
      addLabel(0, 0, "Bold", ChatFormatting.GOLD);
      addCheckBox(186, 0, style.isBold())
          .setResponder(
              v -> {
                setSelectedSkillsDescriptionStyle(s -> s.withBold(v));
                rebuildWidgets();
              });
      shiftWidgets(0, 19);
      addLabel(0, 0, "Italic", ChatFormatting.GOLD);
      addCheckBox(186, 0, style.isItalic())
          .setResponder(
              v -> {
                setSelectedSkillsDescriptionStyle(s -> s.withItalic(v));
                rebuildWidgets();
              });
      shiftWidgets(0, 19);
      addLabel(0, 0, "Underline", ChatFormatting.GOLD);
      addCheckBox(186, 0, style.isUnderlined())
          .setResponder(
              v -> {
                setSelectedSkillsDescriptionStyle(s -> s.withUnderlined(v));
                rebuildWidgets();
              });
      shiftWidgets(0, 19);
      addLabel(0, 0, "Strikethrough", ChatFormatting.GOLD);
      addCheckBox(186, 0, style.isStrikethrough())
          .setResponder(
              v -> {
                setSelectedSkillsDescriptionStyle(s -> s.withStrikethrough(v));
                rebuildWidgets();
              });
      shiftWidgets(0, 19);
      addLabel(0, 0, "Obfuscated", ChatFormatting.GOLD);
      addCheckBox(186, 0, style.isObfuscated())
          .setResponder(
              v -> {
                setSelectedSkillsDescriptionStyle(s -> s.withObfuscated(v));
                rebuildWidgets();
              });
      shiftWidgets(0, 19);
    }
  }

  private void createSkills(
      NumericTextField angleEditor,
      NumericTextField distanceEditor,
      TriConsumer<Float, Float, PassiveSkill> skillFactory) {
    float angle = (float) angleEditor.getNumericValue();
    float angleRadians = (float) Math.toRadians(angle);
    selectedSkills.forEach(
        skillId -> {
          PassiveSkill skill = SkillTreeClientData.getEditorSkill(skillId);
          float distance = (float) distanceEditor.getNumericValue();
          distance += skill.getButtonSize() / 2f + 8;
          float skillX = skill.getPositionX() + Mth.sin(angleRadians) * distance;
          float skillY = skill.getPositionY() + Mth.cos(angleRadians) * distance;
          skillFactory.accept(skillX, skillY, skill);
        });
    if (mirroring) {
      float sectorSize = 360f / mirrorSides;
      for (int i = 1; i < mirrorSides; i++) {
        angle = mirrorSides == 2 ? -angle - mirrorAngle * 2 : angle - sectorSize;
        float finalAngle = (float) Math.toRadians(angle);
        int sector = i;
        selectedSkills.forEach(
            skillId -> {
              PassiveSkill skill = SkillTreeClientData.getEditorSkill(skillId);
              skill = getMirroredSkill(skill, sector);
              if (skill == null) return;
              float distance = (float) distanceEditor.getNumericValue();
              distance += skill.getButtonSize() / 2f + 8;
              float skillX = skill.getPositionX() + Mth.sin(finalAngle) * distance;
              float skillY = skill.getPositionY() + Mth.cos(finalAngle) * distance;
              skillFactory.accept(skillX, skillY, skill);
            });
      }
    }
    rebuildWidgets();
  }

  private @Nullable PassiveSkill getMirroredSkill(PassiveSkill skill, int sector) {
    float skillX = skill.getPositionX();
    float skillY = skill.getPositionY();
    if (mirrorCenterX == skillX && mirrorCenterY == skillY) {
      return skill;
    }
    float originalAngle =
        (float) Math.toDegrees(Math.atan2(skillY - mirrorCenterY, skillX - mirrorCenterX)) + 90;
    float sectorSize = 360f / mirrorSides;
    float angle =
        (float)
            Math.toRadians(
                mirrorSides == 2
                    ? -originalAngle + mirrorAngle * 2
                    : originalAngle + sectorSize * sector);
    float distance = (float) Math.hypot(skillX - mirrorCenterX, skillY - mirrorCenterY);
    float mirroredSkillX = mirrorCenterX + Mth.sin(angle) * distance;
    float mirroredSkillY = mirrorCenterY + Mth.cos((float) (angle + Math.PI)) * distance;
    return getSkillAtPosition(mirroredSkillX, mirroredSkillY);
  }

  @Nullable
  private PassiveSkill getSkillAtPosition(float x, float y) {
    for (PassiveSkill skill : getTreeSkills().toList()) {
      double distance = Math.hypot(x - skill.getPositionX(), y - skill.getPositionY());
      if (distance < skill.getButtonSize()) return skill;
    }
    return null;
  }

  private void createCopiedSkill(float x, float y, PassiveSkill original) {
    PassiveSkill skill =
        new PassiveSkill(
            createNewSkillId(),
            original.getButtonSize(),
            original.getBackgroundTexture(),
            original.getIconTexture(),
            original.getBorderTexture(),
            original.isStartingPoint());
    skill.setPosition(x, y);
    skill.setConnectedTree(original.getConnectedTreeId());
    skill.setStartingPoint(original.isStartingPoint());
    original.getBonuses().stream().map(SkillBonus::copy).forEach(skill::addSkillBonus);
    original.getTags().forEach(skill.getTags()::add);
    skill.setTitle(original.getTitle());
    skill.setTitleColor(original.getTitleColor());
    skill.setDescription(original.getDescription());
    skill.connect(original);
    SkillTreeClientData.saveEditorSkill(skill);
    SkillTreeClientData.loadEditorSkill(skill.getId());
    skillTree.getSkillIds().add(skill.getId());
    SkillTreeClientData.saveEditorSkillTree(skillTree);
  }

  private void createNewSkill(float x, float y, @Nullable PassiveSkill other) {
    ResourceLocation background =
        new ResourceLocation(SkillTreeMod.MOD_ID, "textures/icons/background/lesser.png");
    ResourceLocation icon = new ResourceLocation(SkillTreeMod.MOD_ID, "textures/icons/void.png");
    ResourceLocation border =
        new ResourceLocation(SkillTreeMod.MOD_ID, "textures/tooltip/lesser.png");
    PassiveSkill skill = new PassiveSkill(createNewSkillId(), 16, background, icon, border, false);
    skill.setPosition(x, y);
    if (other != null) skill.connect(other);
    SkillTreeClientData.saveEditorSkill(skill);
    SkillTreeClientData.loadEditorSkill(skill.getId());
    skillTree.getSkillIds().add(skill.getId());
    SkillTreeClientData.saveEditorSkillTree(skillTree);
  }

  private ResourceLocation createNewSkillId() {
    ResourceLocation id;
    int counter = 1;
    do {
      id = new ResourceLocation("skilltree", "new_skill_" + counter++);
    } while (SkillTreeClientData.getEditorSkill(id) != null);
    return id;
  }

  private void addButtonTools() {
    addButton(0, 0, 90, 14, "Back").setPressFunc(b -> selectTools(Tools.MAIN));
    shiftWidgets(0, 29);
    PassiveSkill firstSelectedSkill = getFirstSelectedSkill();
    if (canEdit(PassiveSkill::getButtonSize)) {
      addLabel(0, 0, "Size", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addNumericTextField(0, 0, 40, 14, firstSelectedSkill.getButtonSize())
          .setNumericFilter(d -> d >= 2)
          .setNumericResponder(this::setSelectedSkillsSize);
      shiftWidgets(0, 19);
    }
    if (selectedSkills.size() == 1) {
      toolsY -= 38;
      addLabel(65, 0, "Position", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addNumericTextField(65, 0, 60, 14, firstSelectedSkill.getPositionX())
          .setNumericResponder(
              v ->
                  setSkillPosition(
                      getFirstSelectedSkill(), v.floatValue(), firstSelectedSkill.getPositionY()));
      addNumericTextField(130, 0, 60, 14, firstSelectedSkill.getPositionY())
          .setNumericResponder(
              v ->
                  setSkillPosition(
                      getFirstSelectedSkill(), firstSelectedSkill.getPositionX(), v.floatValue()));
      shiftWidgets(0, 19);
    }
    if (canEdit(PassiveSkill::getTitle)) {
      addLabel(0, 0, "Title", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addTextField(0, 0, 200, 14, firstSelectedSkill.getTitle())
          .setResponder(this::setSelectedSkillsTitle);
      shiftWidgets(0, 19);
    }
    boolean canEditTitleColor = canEdit(PassiveSkill::getTitleColor);
    if (canEditTitleColor) {
      addLabel(0, 0, "Title Color", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addTextField(0, 0, 80, 14, firstSelectedSkill.getTitleColor())
          .setSoftFilter(v -> v.matches("^#?[a-fA-F0-9]{6}") || v.isEmpty())
          .setResponder(this::setSelectedSkillsTitleColor);
      shiftWidgets(0, 19);
    }
    if (canEdit(PassiveSkill::isStartingPoint)) {
      if (canEditTitleColor) {
        shiftWidgets(100, -38);
      }
      addLabel(0, 0, "Starting Point", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addCheckBox(0, 0, firstSelectedSkill.isStartingPoint())
          .setResponder(
              v -> {
                selectedSkills().forEach(s -> s.setStartingPoint(v));
                saveSelectedSkills();
              });
      if (canEditTitleColor) {
        shiftWidgets(-100, 0);
      }
      shiftWidgets(0, 19);
    }
  }

  private void setSelectedSkillsSize(double size) {
    selectedSkills()
        .forEach(
            skill -> {
              skill.setButtonSize((int) size);
              reAddSkillButton(skill);
            });
    addSkillConnections();
    saveSelectedSkills();
  }

  private void setSkillPosition(PassiveSkill skill, float x, float y) {
    skill.setPosition(x, y);
    reAddSkillButton(skill);
    addSkillConnections();
    saveSelectedSkills();
  }

  private void moveSelectedSkills(float x, float y) {
    selectedSkills.forEach(
        skillId -> {
          PassiveSkill skill = SkillTreeClientData.getEditorSkill(skillId);
          skill.setPosition(skill.getPositionX() + x, skill.getPositionY() + y);
          reAddSkillButton(skill);
        });
    addSkillConnections();
    saveSelectedSkills();
  }

  private PassiveSkill getFirstSelectedSkill() {
    ResourceLocation skillId = (ResourceLocation) selectedSkills.toArray()[0];
    return SkillTreeClientData.getEditorSkill(skillId);
  }

  private void setSelectedSkillsTitle(String title) {
    selectedSkills().forEach(skill -> skill.setTitle(title));
    saveSelectedSkills();
  }

  private void setSelectedSkillsTitleColor(String color) {
    String finalColor = formatColor(color);
    selectedSkills().forEach(skill -> skill.setTitleColor(finalColor));
    saveSelectedSkills();
  }

  @NotNull
  private static String formatColor(String color) {
    if (color.startsWith("#")) color = color.substring(1);
    return color;
  }

  private void setSelectedSkillsDescription(String line) {
    selectedSkills()
        .forEach(
            skill -> {
              List<MutableComponent> description = skill.getDescription();
              Objects.requireNonNull(description);
              MutableComponent component = description.get(selectedSubMenu);
              Style style = component.getStyle();
              description.set(selectedSubMenu, Component.literal(line).withStyle(style));
            });
    saveSelectedSkills();
  }

  private void setSelectedSkillsDescriptionStyle(Function<Style, Style> styleFunc) {
    selectedSkills()
        .forEach(
            skill -> {
              List<MutableComponent> description = skill.getDescription();
              Objects.requireNonNull(description);
              MutableComponent component = description.get(selectedSubMenu);
              Style style = styleFunc.apply(component.getStyle());
              description.set(selectedSubMenu, component.withStyle(style));
              SkillTreeClientData.saveEditorSkill(skill);
              SkillTreeClientData.loadEditorSkill(skill.getId());
            });
  }

  private void regenerateSelectedSkillsDescription() {
    selectedSkills().forEach(skill -> skill.setDescription(null));
    saveSelectedSkills();
    selectedSkills()
        .forEach(
            skill -> {
              List<MutableComponent> description = new ArrayList<>();
              skillButtons.get(skill.getId()).addSkillBonusTooltip(description);
              skill.setDescription(description);
            });
    rebuildWidgets();
  }

  private void removeSelectedSkillsDescriptionLine() {
    selectedSkills()
        .forEach(
            skill -> {
              List<MutableComponent> description = skill.getDescription();
              Objects.requireNonNull(description);
              description.remove(selectedSubMenu);
            });
    saveSelectedSkills();
    selectSubMenu(-1);
    rebuildWidgets();
  }

  private void removeSelectedSkillsDescription() {
    selectedSkills().forEach(skill -> skill.setDescription(null));
    saveSelectedSkills();
    rebuildWidgets();
  }

  private void addSelectedSkillsDescriptionLine() {
    selectedSkills()
        .forEach(
            skill -> {
              List<MutableComponent> description = skill.getDescription();
              if (description == null) {
                description = new ArrayList<>();
                skill.setDescription(description);
              }
              description.add(Component.empty().withStyle(TooltipHelper.getSkillBonusStyle(true)));
            });
    saveSelectedSkills();
    rebuildWidgets();
  }

  private void addTexturesTools() {
    addButton(0, 0, 90, 14, "Back").setPressFunc(b -> selectTools(Tools.MAIN));
    shiftWidgets(0, 29);
    PassiveSkill skill = getFirstSelectedSkill();
    if (canEdit(PassiveSkill::getBackgroundTexture)) {
      addLabel(0, 0, "Border", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addDropDownList(0, 0, 200, 14, 10, skill.getBackgroundTexture(), SkillTexturesData.BORDERS)
          .setToNameFunc(TooltipHelper::getTextureName)
          .setResponder(
              value -> {
                selectedSkills().forEach(s -> s.setBackgroundTexture(value));
                saveSelectedSkills();
              });
      shiftWidgets(0, 19);
    }
    if (canEdit(PassiveSkill::getBorderTexture)) {
      addLabel(0, 0, "Tooltip", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addDropDownList(
              0, 0, 200, 14, 10, skill.getBorderTexture(), SkillTexturesData.TOOLTIP_BACKGROUNDS)
          .setToNameFunc(TooltipHelper::getTextureName)
          .setResponder(
              value -> {
                selectedSkills().forEach(s -> s.setBorderTexture(value));
                saveSelectedSkills();
              });
      shiftWidgets(0, 19);
    }
    if (canEdit(PassiveSkill::getIconTexture)) {
      addLabel(0, 0, "Icon", ChatFormatting.GOLD);
      shiftWidgets(0, 19);
      addDropDownList(0, 0, 200, 14, 10, skill.getIconTexture(), SkillTexturesData.ICONS)
          .setToNameFunc(TooltipHelper::getTextureName)
          .setResponder(
              value -> {
                selectedSkills().forEach(s -> s.setIconTexture(value));
                saveSelectedSkills();
              });
      shiftWidgets(0, 19);
    }
  }

  private void addConnectionToolsButton() {
    addButton(0, 0, 90, 14, "Back").setPressFunc(b -> selectTools(Tools.MAIN));
    shiftWidgets(0, 29);
    if (selectedSkills.size() < 2) return;
    if (selectedSkillsConnected()) {
      Button disconnectButton =
          new Button(toolsX, toolsY, 100, 14, Component.literal("Disconnect"));
      addRenderableWidget(disconnectButton);
      disconnectButton.setPressFunc(b -> disconnectSelectedSkills());
    } else {
      addLabel(0, 0, "Connect");
      shiftWidgets(0, 19);
      addButton(0, 0, 100, 14, "Direct")
          .setPressFunc(b -> connectSelectedSkills(SkillConnection.Type.DIRECT));
      shiftWidgets(0, 19);
      addButton(0, 0, 100, 14, "Long")
          .setPressFunc(b -> connectSelectedSkills(SkillConnection.Type.LONG));
      shiftWidgets(0, 19);
      addButton(0, 0, 100, 14, "One Way")
          .setPressFunc(b -> connectSelectedSkills(SkillConnection.Type.ONE_WAY));
    }
    shiftWidgets(0, 19);
  }

  private void connectSelectedSkills(SkillConnection.Type connectionType) {
    ResourceLocation[] selectedSkillsArray = selectedSkills.toArray(new ResourceLocation[0]);
    for (int i = 0; i < selectedSkills.size() - 1; i++) {
      PassiveSkill skill = SkillTreeClientData.getEditorSkill(selectedSkillsArray[i]);
      List<ResourceLocation> connections =
          switch (connectionType) {
            case DIRECT -> skill.getDirectConnections();
            case LONG -> skill.getLongConnections();
            case ONE_WAY -> skill.getOneWayConnections();
          };
      connections.add(selectedSkillsArray[i + 1]);
    }
    saveSelectedSkills();
    rebuildWidgets();
  }

  private void disconnectSelectedSkills() {
    ResourceLocation[] selectedSkillsArray = selectedSkills.toArray(new ResourceLocation[0]);
    for (int i = 0; i < selectedSkills.size() - 1; i++) {
      PassiveSkill skill1 = SkillTreeClientData.getEditorSkill(selectedSkillsArray[i]);
      PassiveSkill skill2 = SkillTreeClientData.getEditorSkill(selectedSkillsArray[i + 1]);
      skill1.getDirectConnections().remove(skill2.getId());
      skill2.getDirectConnections().remove(skill1.getId());
      skill1.getLongConnections().remove(skill2.getId());
      skill2.getLongConnections().remove(skill1.getId());
      skill1.getOneWayConnections().remove(skill2.getId());
      skill2.getOneWayConnections().remove(skill1.getId());
    }
    saveSelectedSkills();
    rebuildWidgets();
  }

  private boolean selectedSkillsConnected() {
    ResourceLocation[] selectedSkillsArray = selectedSkills.toArray(new ResourceLocation[0]);
    for (int i = 0; i < selectedSkills.size() - 1; i++) {
      PassiveSkill skill1 = SkillTreeClientData.getEditorSkill(selectedSkillsArray[i]);
      PassiveSkill skill2 = SkillTreeClientData.getEditorSkill(selectedSkillsArray[i + 1]);
      if (!skillsConnected(skill1, skill2)) return false;
    }
    return true;
  }

  private boolean skillsConnected(PassiveSkill first, PassiveSkill second) {
    return first.getDirectConnections().contains(second.getId())
        || second.getDirectConnections().contains(first.getId())
        || first.getLongConnections().contains(second.getId())
        || second.getLongConnections().contains(first.getId())
        || first.getOneWayConnections().contains(second.getId())
        || second.getOneWayConnections().contains(first.getId());
  }

  private void saveSelectedSkills() {
    selectedSkills.stream()
        .map(skillButtons::get)
        .map(button -> button.skill)
        .forEach(SkillTreeClientData::saveEditorSkill);
  }

  protected void addSkillButton(PassiveSkill skill) {
    SkillButton button =
        new SkillButton(() -> 0f, getSkillButtonX(skill), getSkillButtonY(skill), skill);
    addRenderableWidget(button);
    button.skillLearned = true;
    skillButtons.put(skill.getId(), button);
    float skillX = skill.getPositionX();
    float skillY = skill.getPositionY();
    if (maxScrollX < Mth.abs(skillX)) maxScrollX = (int) Mth.abs(skillX);
    if (maxScrollY < Mth.abs(skillY)) maxScrollY = (int) Mth.abs(skillY);
  }

  protected void reAddSkillButton(PassiveSkill skill) {
    children().removeIf(w -> w instanceof SkillButton b && b.skill == skill);
    skillButtons.remove(skill.getId());
    addSkillButton(skill);
  }

  private float getSkillButtonX(PassiveSkill skill) {
    float skillX = skill.getPositionX();
    return skillX - skill.getButtonSize() / 2F + width / 2F + skillX * (zoom - 1);
  }

  private float getSkillButtonY(PassiveSkill skill) {
    float skillY = skill.getPositionY();
    return skillY - skill.getButtonSize() / 2F + height / 2F + skillY * (zoom - 1);
  }

  public void addSkillConnections() {
    skillConnections.clear();
    getTreeSkills().forEach(this::addSkillConnections);
  }

  private Stream<PassiveSkill> getTreeSkills() {
    return skillTree.getSkillIds().stream().map(SkillTreeClientData::getEditorSkill);
  }

  private void addSkillConnections(PassiveSkill skill) {
    readSkillConnections(skill, SkillConnection.Type.DIRECT, skill.getDirectConnections());
    readSkillConnections(skill, SkillConnection.Type.LONG, skill.getLongConnections());
    readSkillConnections(skill, SkillConnection.Type.ONE_WAY, skill.getOneWayConnections());
  }

  private void readSkillConnections(
      PassiveSkill skill, SkillConnection.Type type, List<ResourceLocation> connections) {
    for (ResourceLocation connectedSkillId : new ArrayList<>(connections)) {
      if (SkillTreeClientData.getEditorSkill(connectedSkillId) == null) {
        connections.remove(connectedSkillId);
        SkillTreeClientData.saveEditorSkill(skill);
        continue;
      }
      connectSkills(type, skill.getId(), connectedSkillId);
    }
  }

  protected void connectSkills(
      SkillConnection.Type type, ResourceLocation skillId1, ResourceLocation skillId2) {
    SkillButton button1 = skillButtons.get(skillId1);
    SkillButton button2 = skillButtons.get(skillId2);
    skillConnections.add(new SkillConnection(type, button1, button2));
  }

  protected void skillButtonPressed(SkillButton button) {
    if (hasControlDown()) return;
    if (!hasShiftDown() && !selectedSkills.isEmpty()) {
      selectedSkills.clear();
    }
    ResourceLocation skillId = button.skill.getId();
    if (selectedSkills.contains(skillId)) selectedSkills.remove(skillId);
    else selectedSkills.add(skillId);
    rebuildWidgets();
  }

  @Override
  public void tick() {
    textFields()
        .forEach(
            widget -> {
              if (widget instanceof EditBox editBox) {
                editBox.tick();
              }
              if (widget instanceof MultiLineEditBox multiLineEditBox) {
                multiLineEditBox.tick();
              }
            });
    dropDownLists().forEach(DropDownList::tick);
  }

  private void updateScroll(float partialTick) {
    scrollX += scrollSpeedX * partialTick;
    scrollX = Math.max(-maxScrollX * zoom, Math.min(maxScrollX * zoom, scrollX));
    scrollSpeedX *= 0.8f;
    scrollY += scrollSpeedY * partialTick;
    scrollY = Math.max(-maxScrollY * zoom, Math.min(maxScrollY * zoom, scrollY));
    scrollSpeedY *= 0.8f;
  }

  private void renderOverlay(GuiGraphics graphics) {
    ResourceLocation texture =
        new ResourceLocation("skilltree:textures/screen/skill_tree_overlay.png");
    RenderSystem.enableBlend();
    graphics.blit(texture, 0, 0, 0, 0F, 0F, width, height, width, height);
    RenderSystem.disableBlend();
  }

  @Override
  public void renderBackground(GuiGraphics graphics) {
    ResourceLocation texture =
        new ResourceLocation("skilltree:textures/screen/skill_tree_background.png");
    graphics.pose().pushPose();
    graphics.pose().translate(scrollX / 3F, scrollY / 3F, 0);
    int size = SkillTreeScreen.BACKGROUND_SIZE;
    graphics.blit(
        texture, (width - size) / 2, (height - size) / 2, 0, 0F, 0F, size, size, size, size);
    graphics.pose().popPose();
  }

  @Override
  public boolean mouseDragged(
      double mouseX, double mouseY, int mouseButton, double dragAmountX, double dragAmountY) {
    if (mouseButton != 0 && mouseButton != 2) return false;
    if (mouseButton == 0 && hasShiftDown()) {
      selectingArea = true;
      return true;
    }
    if (mouseButton == 0 && hasControlDown() && !selectedSkills.isEmpty()) {
      moveSelectedSkills((float) dragAmountX / zoom, (float) dragAmountY / zoom);
      return true;
    }
    if (maxScrollX > 0) scrollSpeedX += (float) (dragAmountX * 0.25f);
    if (maxScrollY > 0) scrollSpeedY += (float) (dragAmountY * 0.25f);
    return true;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
    if (widgets().anyMatch(w -> w.mouseScrolled(mouseX, mouseY, amount))) return true;
    if (amount > 0 && zoom < 2F) zoom += 0.05f;
    if (amount < 0 && zoom > 0.25F) zoom -= 0.05f;
    rebuildWidgets();
    return super.mouseScrolled(mouseX, mouseY, amount);
  }

  public Stream<PassiveSkill> selectedSkills() {
    return selectedSkills.stream().map(SkillTreeClientData::getEditorSkill);
  }

  public void shiftWidgets(int x, int y) {
    toolsX += x;
    toolsY += y;
  }

  private static void removeSkillBonus(PassiveSkill skill, int index) {
    skill.getBonuses().remove(index);
    SkillTreeClientData.saveEditorSkill(skill);
  }

  public TextField addTextField(int x, int y, int width, int height, String defaultValue) {
    Objects.requireNonNull(minecraft);
    return addRenderableWidget(
        new TextField(minecraft.font, toolsX + x, toolsY + y, width, height, defaultValue));
  }

  public NumericTextField addNumericTextField(
      int x, int y, int width, int height, double defaultValue) {
    Objects.requireNonNull(minecraft);
    return addRenderableWidget(
        new NumericTextField(minecraft.font, toolsX + x, toolsY + y, width, height, defaultValue));
  }

  public TextArea addTextArea(int x, int y, int width, int height, String defaultValue) {
    Objects.requireNonNull(minecraft);
    return addRenderableWidget(
        new TextArea(minecraft.font, toolsX + x, toolsY + y, width, height, defaultValue));
  }

  public void rebuildWidgets() {
    super.rebuildWidgets();
  }

  public Label addLabel(int x, int y, String text, ChatFormatting... styles) {
    MutableComponent message = Component.literal(text);
    for (ChatFormatting style : styles) {
      message.withStyle(style);
    }
    return addRenderableOnly(new Label(toolsX + x, toolsY + y, message));
  }

  public CheckBox addCheckBox(int x, int y, boolean value) {
    return addRenderableWidget(new CheckBox(toolsX + x, toolsY + y, value));
  }

  public <T> DropDownList<T> addDropDownList(
      int x,
      int y,
      int width,
      int height,
      int maxDisplayed,
      T defaultValue,
      Collection<T> possibleValues) {
    return addRenderableWidget(
        new DropDownList<>(
            toolsX + x, toolsY + y, width, height, maxDisplayed, possibleValues, defaultValue));
  }

  public <T extends Enum<T>> DropDownList<T> addDropDownList(
      int x, int y, int width, int height, int maxDisplayed, T defaultValue) {
    Class<T> enumClass = (Class<T>) defaultValue.getClass();
    List<T> enums = Stream.of(enumClass.getEnumConstants()).map(enumClass::cast).toList();
    return addRenderableWidget(
        new DropDownList<>(
            toolsX + x, toolsY + y, width, height, maxDisplayed, enums, defaultValue));
  }

  public DropDownList<Attribute> addAttributePicker(
      int x, int y, int width, int height, int maxDisplayed, Attribute defaultValue) {
    return addDropDownList(x, y, width, height, maxDisplayed, defaultValue, getEditableAttributes())
        .setToNameFunc(
            attribute -> {
              ResourceLocation id = ForgeRegistries.ATTRIBUTES.getKey(attribute);
              if (attribute instanceof SlotAttribute slotAttribute) {
                id = new ResourceLocation("curios", slotAttribute.getIdentifier());
              }
              Objects.requireNonNull(id);
              return Component.literal(id.toString());
            });
  }

  public Button addButton(int x, int y, int width, int height, String message) {
    return addButton(x, y, width, height, Component.literal(message));
  }

  public Button addButton(int x, int y, int width, int height, Component message) {
    return addRenderableWidget(new Button(toolsX + x, toolsY + y, width, height, message));
  }

  public ConfirmationButton addConfirmationButton(
      int x, int y, int width, int height, String message, String confirmationMessage) {
    ConfirmationButton button =
        new ConfirmationButton(toolsX + x, toolsY + y, width, height, Component.literal(message));
    button.setConfirmationMessage(Component.literal(confirmationMessage));
    return addRenderableWidget(button);
  }

  private static final Set<Attribute> EDITABLE_ATTRIBUTES = new HashSet<>();

  @SuppressWarnings("deprecation")
  @NotNull
  private static Collection<Attribute> getEditableAttributes() {
    if (EDITABLE_ATTRIBUTES.isEmpty()) {
      ForgeRegistries.ATTRIBUTES.getValues().stream()
          .filter(ForgeHooks.getAttributesView().get(EntityType.PLAYER)::hasAttribute)
          .forEach(EDITABLE_ATTRIBUTES::add);
      CuriosApi.getSlots().keySet().stream()
          .map(SlotAttribute::getOrCreate)
          .forEach(EDITABLE_ATTRIBUTES::add);
    }
    return EDITABLE_ATTRIBUTES;
  }

  protected void renderConnections(GuiGraphics graphics, int mouseX, int mouseY) {
    skillConnections.stream()
        .filter(c -> c.getType() == SkillConnection.Type.DIRECT)
        .forEach(c -> renderDirectConnection(graphics, c));
    skillConnections.stream()
        .filter(c -> c.getType() == SkillConnection.Type.LONG)
        .forEach(c -> renderLongConnection(graphics, c, mouseX, mouseY));
    skillConnections.stream()
        .filter(c -> c.getType() == SkillConnection.Type.ONE_WAY)
        .forEach(c -> renderOneWayConnection(graphics, c));
  }

  private void renderDirectConnection(GuiGraphics graphics, SkillConnection c) {
    ScreenHelper.renderConnection(graphics, scrollX, scrollY, c, zoom, 0);
  }

  private void renderLongConnection(
      GuiGraphics graphics, SkillConnection connection, int mouseX, int mouseY) {
    SkillButton hoveredSkill = getSkillAt(mouseX, mouseY);
    if (hoveredSkill != connection.getFirstButton()
        && hoveredSkill != connection.getSecondButton()) {
      return;
    }
    ScreenHelper.renderGatewayConnection(graphics, scrollX, scrollY, connection, true, zoom, 0);
  }

  private void renderOneWayConnection(GuiGraphics graphics, SkillConnection connection) {
    ScreenHelper.renderOneWayConnection(graphics, scrollX, scrollY, connection, true, zoom, 0);
  }

  protected boolean sameBonuses(PassiveSkill skill, PassiveSkill otherSkill) {
    if (skill == otherSkill) return true;
    List<SkillBonus<?>> modifiers = skill.getBonuses();
    List<SkillBonus<?>> otherModifiers = otherSkill.getBonuses();
    if (modifiers.size() != otherModifiers.size()) return false;
    for (int i = 0; i < modifiers.size(); i++) {
      if (!modifiers.get(i).sameBonus(otherModifiers.get(i))) return false;
    }
    return true;
  }

  protected boolean sameTags(PassiveSkill skill, PassiveSkill otherSkill) {
    if (skill == otherSkill) return true;
    List<String> tags = skill.getTags();
    List<String> otherTags = otherSkill.getTags();
    if (tags.size() != otherTags.size()) return false;
    for (int i = 0; i < tags.size(); i++) {
      if (!tags.get(i).equals(otherTags.get(i))) return false;
    }
    return true;
  }

  protected boolean sameDescription(PassiveSkill skill, PassiveSkill otherSkill) {
    if (skill == otherSkill) return true;
    List<MutableComponent> description = skill.getDescription();
    List<MutableComponent> otherDescription = otherSkill.getDescription();
    if (description == null && otherDescription == null) return true;
    if (description == null || otherDescription == null) return false;
    if (description.size() != otherDescription.size()) return false;
    for (int i = 0; i < description.size(); i++) {
      if (!description.get(i).equals(otherDescription.get(i))) return false;
    }
    return true;
  }

  protected final boolean canEdit(Function<PassiveSkill, ?> function) {
    return selectedSkills().map(function).distinct().count() <= 1;
  }

  private enum Tools {
    MAIN,
    BONUSES,
    TEXTURES,
    BUTTON,
    CONNECTIONS,
    TAGS,
    DESCRIPTION,
    NODE
  }
}
