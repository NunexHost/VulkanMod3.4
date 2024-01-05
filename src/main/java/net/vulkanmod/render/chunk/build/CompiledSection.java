package net.vulkanmod.render.chunk.build;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.renderer.chunk.VisibilitySet;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import net.vulkanmod.render.vertex.TerrainRenderType;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompiledSection {

    public static final CompiledSection UNCOMPILED = new CompiledSection() {
        public boolean canSeeThrough(Direction dir1, Direction dir2) {
            return false;
        }
    };

    public final Set<TerrainRenderType> renderTypes = new BitSet();

    boolean isCompletelyEmpty = true;

    final List<BlockEntity> renderableBlockEntities = Lists.newArrayList();

    VisibilitySet visibilitySet = new VisibilitySet();

    @Nullable
    TerrainBufferBuilder.SortState transparencyState;

    public boolean hasNoRenderableLayers() {
        return this.renderTypes.isEmpty();
    }

    public boolean isEmpty(TerrainRenderType p_112759_) {
        return !this.renderTypes.get(p_112759_.ordinal());
    }

    public List<BlockEntity> getRenderableBlockEntities() {
        return this.renderableBlockEntities;
    }

    public boolean canSeeThrough(Direction dir1, Direction dir2) {
        if (this.visibilityCache.containsKey(dir1, dir2)) {
            return this.visibilityCache.get(dir1, dir2);
        }

        boolean visibility = this.visibilitySet.visibilityBetween(dir1, dir2);
        this.visibilityCache.put(dir1, dir2, visibility);
        return visibility;
    }

    private final Map<Direction, Map<Direction, Boolean>> visibilityCache = new HashMap<>();

    public void addRenderableLayer(TerrainRenderType renderType) {
        this.renderTypes.set(renderType.ordinal());
        this.isCompletelyEmpty = false;
    }

    public void addRenderableBlockEntity(BlockEntity blockEntity) {
        this.renderableBlockEntities.add(blockEntity);
    }

    public void updateVisibility(int chunkX, int chunkY, int chunkZ) {
        this.visibilitySet.update(chunkX, chunkY, chunkZ);
    }

}
