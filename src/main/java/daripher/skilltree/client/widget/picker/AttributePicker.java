package daripher.skilltree.client.widget.picker;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import daripher.skilltree.client.widget.DropDownList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;

public class AttributePicker extends DropDownList<Attribute> {
  private static final Set<Attribute> EDITABLE_ATTRIBUTES = new HashSet<>();

  public AttributePicker(int x, int y, int width, int height, int maxDisplayed, Attribute value) {
    super(x, y, width, height, maxDisplayed, EDITABLE_ATTRIBUTES, value);
    setToNameFunc(AttributePicker::getAttributeName);
  }

  private static MutableComponent getAttributeName(Attribute attribute) {
    ResourceLocation id = ForgeRegistries.ATTRIBUTES.getKey(attribute);
    if (attribute instanceof SlotAttribute slotAttribute) {
      id = new ResourceLocation("curios", slotAttribute.getIdentifier());
    }
    Objects.requireNonNull(id);
    return Component.literal(id.toString());
  }

  static {
    //noinspection deprecation
    ForgeRegistries.ATTRIBUTES.getValues().stream()
        .filter(ForgeHooks.getAttributesView().get(EntityType.PLAYER)::hasAttribute)
        .forEach(EDITABLE_ATTRIBUTES::add);
    CuriosApi.getSlots().keySet().stream()
        .map(SlotAttribute::getOrCreate)
        .forEach(EDITABLE_ATTRIBUTES::add);
  }
}
