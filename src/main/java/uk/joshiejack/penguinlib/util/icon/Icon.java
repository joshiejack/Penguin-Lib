package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;

import java.util.ArrayList;
import java.util.List;

public abstract class Icon {
    public static final ResourceLocation DEFAULT_LOCATION = new ResourceLocation(PenguinLib.MODID, "textures/gui/icons.png");
    protected boolean shadowed;

    public enum Type {
        ITEM, TEXTURE, ENTITY, TAG, LIST
    }

    public Icon shadowed() {
        this.shadowed = true;
        return this;
    }

    public static Icon fromJson(JsonObject json) {
        //List
        if (json.has("list")) {
            JsonArray array = json.getAsJsonArray("list");
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(array.get(i).getAsString()));
                if (item != null)
                    items.add(new ItemStack(item));
            }

            return new ItemListIcon(items);
        }

        //Rest
        return json.has("item") ? new ItemIcon(new ItemStack(JSONUtils.getAsItem(json, "item")))
                : json.has("tag") ? new TagIcon(ItemTags.createOptional(new ResourceLocation(JSONUtils.getAsString(json, "tag"))))
                : json.has("entity") ? new EntityIcon(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(JSONUtils.getAsString(json, "entity"))), JSONUtils.getAsInt(json, "scale"))
                : new TextureIcon(json.has("texture") ? new ResourceLocation(JSONUtils.getAsString(json, "texture")) : DEFAULT_LOCATION,
                json.has("x") ? JSONUtils.getAsInt(json, "x") : 0,
                json.has("y") ? JSONUtils.getAsInt(json, "y") : 0);
    }

    public abstract JsonElement toJson(JsonObject json);

    public static Icon fromNetwork(PacketBuffer pb) {
        Type type = Type.values()[pb.readByte()];
        switch (type) {
            case ITEM:
                return new ItemIcon(pb.readItem());
            case TEXTURE:
                return new TextureIcon(pb.readBoolean() ? pb.readResourceLocation() : DEFAULT_LOCATION, pb.readInt(), pb.readInt());
            case ENTITY:
                return new EntityIcon(pb.readRegistryIdSafe(EntityType.class), pb.readByte());
            case TAG:
                return new TagIcon(ItemTags.createOptional(pb.readResourceLocation()));
            case LIST:
            {
                List<ItemStack> items = new ArrayList<>();
                int size = pb.readInt();
                for (int i = 0; i < size; i++) {
                    items.add(pb.readItem());
                }

                return new ItemListIcon(items);
            }
        }

        //Unreachable
        return null;
    }

    public abstract void toNetwork(PacketBuffer pb);

    @OnlyIn(Dist.CLIENT)
    public abstract void render(Minecraft mc, MatrixStack matrix, int x, int y);
}