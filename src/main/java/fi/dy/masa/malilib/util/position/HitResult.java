package fi.dy.masa.malilib.util.position;

import javax.annotation.Nullable;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Post-ReWrite code
 */
@ApiStatus.Experimental
public class HitResult
{
    public final Type type;
    @Nullable public final BlockPos blockPos;
    @Nullable public final Direction side;
    @Nullable public final Vec3d pos;
    @Nullable public final Entity entity;

    public HitResult(Type type, @Nullable BlockPos blockPos, @Nullable Direction side, @Nullable Vec3d pos, @Nullable Entity entity)
    {
        this.type = type;
        this.blockPos = blockPos;
        this.side = side;
        this.pos = pos;
        this.entity = entity;
    }

    @Nullable
    public BlockPos getBlockPos()
    {
        return this.blockPos;
    }

    public net.minecraft.util.hit.HitResult toVanilla()
    {
        switch (this.type)
        {
            case BLOCK:     return new BlockHitResult(this.pos.toVanilla(), this.side.getVanillaDirection(), this.blockPos.toVanillaPos(), false);
            case ENTITY:    return new EntityHitResult(this.entity, this.pos.toVanilla());
            default:        return BlockHitResult.createMissed(net.minecraft.util.math.Vec3d.ZERO, Direction.DOWN.getVanillaDirection(), net.minecraft.util.math.BlockPos.ORIGIN);
        }
    }

    @Override
    public String toString()
    {
        return "HitResult{type=" + this.type + ", blockPos=" + this.blockPos + ", side=" + this.side +
               ", pos=" + this.pos + ", entity=" + this.entity + "}";
    }

    public enum Type
    {
        MISS,
        BLOCK,
        ENTITY;
    }

    public static HitResult miss()
    {
        return new HitResult(Type.MISS, null, null, null, null);
    }

    public static HitResult block(BlockPos pos, Direction side, Vec3d exactPos)
    {
        return new HitResult(Type.BLOCK, pos, side, exactPos, null);
    }

    public static HitResult entity(Entity entity, Vec3d exactPos)
    {
        return new HitResult(Type.ENTITY, null, null, exactPos, entity);
    }

    public static HitResult of(@Nullable net.minecraft.util.hit.HitResult trace)
    {
        if (trace == null)
        {
            return miss();
        }

        switch (trace.getType())
        {
            case BLOCK:     return block(BlockPos.of(((BlockHitResult) trace).getBlockPos()), Direction.of(((BlockHitResult) trace).getSide()), Vec3d.of(trace.getPos()));
            case ENTITY:    return entity(((EntityHitResult) trace).getEntity(), Vec3d.of(trace.getPos()));
            case MISS:
            default:
                return miss();
        }
    }
}
