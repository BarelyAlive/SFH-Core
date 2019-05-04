package mod.sfhcore.items.model_bucket;

import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.BakedItemModel.BakedGuiItemModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedItemModel implements IBakedModel {
    protected final ImmutableList<BakedQuad> quads;
    protected final TextureAtlasSprite particle;
    protected final ImmutableMap<TransformType, TRSRTransformation> transforms;
    protected final ItemOverrideList overrides;
    protected final IBakedModel guiModel;

    public BakedItemModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<TransformType, TRSRTransformation> transforms, ItemOverrideList overrides)
    {
        this.quads = quads;
        this.particle = particle;
        this.transforms = transforms;
        this.overrides = overrides;
        this.guiModel = hasGuiIdentity(transforms) ? new BakedGuiItemModel<>(this) : null;
    }

    private static boolean hasGuiIdentity(ImmutableMap<TransformType, TRSRTransformation> transforms)
    {
        TRSRTransformation guiTransform = transforms.get(TransformType.GUI);
        return guiTransform == null || guiTransform.isIdentity();
    }

    @Override public boolean isAmbientOcclusion() { return true; }
    @Override public boolean isGui3d() { return false; }
    @Override public boolean isBuiltInRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleTexture() { return particle; }
    @Override public ItemOverrideList getOverrides() { return overrides; }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if (side == null)
        {
            return quads;
        }
        return ImmutableList.of();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
    {
        if (type == TransformType.GUI && this.guiModel != null)
        {
            return this.guiModel.handlePerspective(type);
        }
        return PerspectiveMapWrapper.handlePerspective(this, transforms, type);
    }

    public static class BakedGuiItemModel<T extends BakedItemModel> extends BakedModelWrapper<T>
    {
        private final ImmutableList<BakedQuad> quads;

        public BakedGuiItemModel(T originalModel)
        {
            super(originalModel);
            ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
            for (BakedQuad quad : originalModel.quads)
            {
                if (quad.getFace() == EnumFacing.SOUTH)
                {
                    builder.add(quad);
                }
            }
            this.quads = builder.build();
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
        {
            if(side == null)
            {
                return quads;
            }
            return ImmutableList.of();
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
        {
            if (type == TransformType.GUI)
            {
                return PerspectiveMapWrapper.handlePerspective(this, originalModel.transforms, type);
            }
            return this.originalModel.handlePerspective(type);
        }
    }
}
