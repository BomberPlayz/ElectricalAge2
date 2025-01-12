package org.eln2.mc.common.blocks.cell

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.eln2.mc.common.blocks.CellBlockBase
import org.eln2.mc.common.cell.CellRegistry
import org.eln2.mc.extensions.VoxelShapeExtensions.align

class SolarLightCellBlock: CellBlockBase() {
    override fun getCellProvider(): ResourceLocation {
        return CellRegistry.SOLAR_LIGHT_CELL.id
    }

    override fun getCollisionShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        return getFor(pState)
    }

    // TODO: This is deprecated
    override fun getShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        return getFor(pState)
    }

    companion object {
        private val lightPillar = box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0)
        private val lightHead = box(5.0, 8.0, 5.0, 11.0, 11.0, 11.0)

        private val shape = Shapes.joinUnoptimized(lightPillar, lightHead, BooleanOp.OR)

        private val cache = HashMap<BlockState, VoxelShape>()

        fun getFor(state : BlockState) : VoxelShape {
            return cache.computeIfAbsent(state){
                when(val facing = state.getValue(FACING)){
                    Direction.NORTH -> shape
                    Direction.SOUTH -> shape
                    Direction.EAST -> shape.align(Direction.NORTH, Direction.EAST)
                    Direction.WEST -> shape.align(Direction.NORTH, Direction.WEST)

                    else -> error("Unhandled dir: $facing")
                }
            }
        }
    }
}
