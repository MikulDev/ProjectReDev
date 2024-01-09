package net.roadkill.redev.common.world.tree;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.roadkill.redev.ReDev;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ShadeTree extends AbstractTreeGrower
{
    private final Color color;

    public ShadeTree(Color color)
    {   super();
        this.color = color;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean hasFlowers)
    {   return null;
    }

    @Override
    public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState state, RandomSource rand)
    {   return place(level, pos, this.color);
    }

    public static boolean place(WorldGenLevel level, BlockPos origin, Color variant)
    {
        BlockPos.MutableBlockPos pos = origin.mutable();
        StructureTemplateManager structuretemplatemanager = level.getServer().getStructureManager();
        int treeVariant = level.getRandom().nextIntBetweenInclusive(0, 4);

        String color = switch (variant)
        {   case NORMAL -> "";
            case TEAL -> "teal_";
            case RED -> "red_";
            case PURPLE -> "purple_";
        };
        Rotation rotation = Rotation.getRandom(level.getRandom());
        Optional<StructureTemplate> treeOpt = structuretemplatemanager.get(new ResourceLocation(ReDev.MOD_ID, "shade_tree/" + color + "shade_tree_" + treeVariant));

        treeOpt.ifPresent(tree ->
        {   Pair<Integer, Integer> offset = getRotatedOffset(treeVariant, rotation);
            tree.placeInWorld(level, pos.offset(offset.getFirst(), 0, offset.getSecond()), pos, new StructurePlaceSettings().setRotation(rotation), level.getRandom(), 2);
        });
        return true;
    }

    public static Pair<Integer, Integer> getRotatedOffset(int variant, Rotation rotation)
    {
        Pair<Integer, Integer> offset = switch (variant)
        {   default -> Pair.of(-5, -6);
            case 1 -> Pair.of(-6, -7);
            case 2 -> Pair.of(-8, -5);
            case 3 -> Pair.of(-8, -6);
            case 4 -> Pair.of(-7, -6);
        };
        return switch (rotation)
        {
            case CLOCKWISE_90 -> Pair.of(-offset.getSecond(), offset.getFirst());
            case CLOCKWISE_180 -> Pair.of(-offset.getFirst(), -offset.getSecond());
            case COUNTERCLOCKWISE_90 -> Pair.of(offset.getSecond(), -offset.getFirst());
            default -> offset;
        };
    }

    public enum Color
    {
        NORMAL,
        TEAL,
        RED,
        PURPLE
    }
}
