package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@Deprecated //TODO: Remove later
public class TerrainHelper {
    public static boolean isWater(IWorld world, BlockPos... positions) {
        for (BlockPos pos : positions) {
            if (world.getBlockState(pos).getMaterial() != Material.WATER) return false;
        }

        return true;
    }

    public static boolean isWaterSource(IWorld world, BlockPos... positions) {
        for (BlockPos pos : positions) {
            if (world.getFluidState(pos).getType() != Fluids.WATER) return false;
        }

        return true;
    }
}
