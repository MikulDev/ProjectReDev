package net.roadkill.redev.common.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.roadkill.redev.core.init.BlockEntityInit;

public class ModSignBlockEntity extends SignBlockEntity
{
    public ModSignBlockEntity(BlockPos pos, BlockState state)
    {   super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {   return BlockEntityInit.SIGN_BLOCK_ENTITY_TYPE.get();
    }
}
