package uk.joshiejack.penguinlib.data.custom;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import uk.joshiejack.penguinlib.data.adapters.CustomObjectAdapter;
import uk.joshiejack.penguinlib.data.adapters.ResourceLocationAdapter;

public class PenguinGson {
    private static Gson gson; //Temporary

    public static Gson get() {
        //Create the gson if it's null
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().setExclusionStrategies(new SuperClassExclusionStrategy());
//            builder.registerTypeAdapter(Placeable.class, new PlaceableAdapter());
//            builder.registerTypeAdapter(BlockState.class, new StateAdapter());
//            builder.registerTypeAdapter(ItemStack.class, new StackAdapter());
            builder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdapter());
//            builder.registerTypeAdapter(ITextComponent.class, new TextComponentAdapter());
//            builder.registerTypeAdapter(BlockPos.class, new BlockPosAdapter());
//            builder.registerTypeAdapter(EnumFacing.class, new FacingAdapter());
//            builder.registerTypeAdapter(UseAction.class, new ActionAdapter());
//            builder.registerTypeAdapter(NBTTagCompound.class, new NBTAdapter());
//            builder.registerTypeAdapter(Material.class, new MaterialAdapter());
//            builder.registerTypeAdapter(SoundType.class, new SoundTypeAdapter());
//            builder.registerTypeAdapter(AxisAlignedBB.class, new AABBAdapter());
//            builder.registerTypeAdapter(BlockRenderLayer.class, new BlockRenderLayerAdapter());
//            builder.registerTypeAdapter(EnumCreatureType.class, new CreatureTypeAdapter());
            builder.registerTypeAdapter(CustomObject.class, new CustomObjectAdapter());
//            builder.registerTypeAdapter(Block.EnumOffsetType.class, new OffsetAdapter()); //TODO
            gson = builder.create();
        }

        return gson;
    }

    private static class SuperClassExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getDeclaringClass().equals(ForgeRegistryEntry.class);
        }
    }
}
