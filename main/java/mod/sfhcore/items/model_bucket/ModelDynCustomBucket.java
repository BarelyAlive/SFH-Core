package mod.sfhcore.items.model_bucket;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Quat4f;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import mod.sfhcore.items.CustomBucket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.FMLLog;

public class ModelDynCustomBucket implements IModel {

    // minimal Z offset to prevent depth-fighting
    private static final float NORTH_Z_COVER = 7.496f / 16f;
    private static final float SOUTH_Z_COVER = 8.504f / 16f;
    private static final float NORTH_Z_FLUID = 7.498f / 16f;
    private static final float SOUTH_Z_FLUID = 8.502f / 16f;

    public static final IModel MODEL = new ModelDynCustomBucket();

    @Nullable
    public ModelResourceLocation LOCATION = new ModelResourceLocation(new ResourceLocation("sfhcore", "bucket_empty"), "inventory");
    @Nullable
    private final ResourceLocation baseLocation;
    @Nullable
    private final ResourceLocation liquidLocation;
    @Nullable
    private final ResourceLocation coverLocation;
    @Nullable
    private final Fluid fluid;

    private final boolean flipGas;
    private final boolean tint;

    public ModelDynCustomBucket()
    {
        this(null, false, true);
    }

    /** @deprecated use {@link #ModelDynBucket(ResourceLocation, ResourceLocation, ResourceLocation, Fluid, boolean, boolean)} */
    @Deprecated // TODO: remove
    public ModelDynCustomBucket(@Nullable Fluid fluid, boolean flipGas)
    {
        this(fluid, flipGas, true);
    }

    public ModelDynCustomBucket(@Nullable Fluid fluid, boolean flipGas, boolean tint)
    {
        this.baseLocation = new ResourceLocation("sfhcore", "items/bucket_empty"); //baseLocation;
        if (fluid == null)
        {
            this.liquidLocation = null;    //liquidLocation;
            this.coverLocation = null;   //coverLocation;
        }
        else
        {
            this.liquidLocation = fluid.getStill();    //liquidLocation;
            this.coverLocation = new ResourceLocation("sfhcore", "items/vanilla_bucket_cover_mask");   //coverLocation;
        }
        this.fluid = fluid;
        this.flipGas = flipGas;
        this.tint = tint;
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

        if (baseLocation != null)
            builder.add(baseLocation);
        if (liquidLocation != null)
            builder.add(liquidLocation);
        if (coverLocation != null)
            builder.add(coverLocation);
        if (fluid != null)
            builder.add(fluid.getStill());

        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format,
                                    Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {

        ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

        // if the fluid is lighter than air, will manipulate the initial state to be rotated 180? to turn it upside down
        if (flipGas && fluid != null && fluid.isLighterThanAir())
        {
            state = new ModelStateComposition(state, TRSRTransformation.blockCenterToCorner(new TRSRTransformation(null, new Quat4f(0, 0, 1, 0), null, null)));
        }

        TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());
        TextureAtlasSprite fluidSprite = null;
        TextureAtlasSprite particleSprite = null;
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        if(fluid != null) {
            fluidSprite = bakedTextureGetter.apply(fluid.getStill());
        }
        
        System.out.println(fluidSprite);
        System.out.println(baseLocation);
        System.out.println(liquidLocation);
        System.out.println(coverLocation);
        
        if (baseLocation != null)
        {
            // build base (insidest)
            IBakedModel model = (new ItemLayerModel(ImmutableList.of(baseLocation))).bake(state, format, bakedTextureGetter);
            builder.addAll(model.getQuads(null, null, 0));
            particleSprite = model.getParticleTexture();
        }
        if (liquidLocation != null)// && fluidSprite != null)
        {
            TextureAtlasSprite liquid = bakedTextureGetter.apply(liquidLocation);
            fluidSprite = liquid;
            // build liquid layer (inside)
            builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, NORTH_Z_FLUID, EnumFacing.NORTH, 0, 1));
            builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, SOUTH_Z_FLUID, EnumFacing.SOUTH, 0, 1));
            particleSprite = fluidSprite;
        }
        if (coverLocation != null)
        {
            // cover (the actual item around the other two)
            TextureAtlasSprite cover = bakedTextureGetter.apply(coverLocation);
            builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, NORTH_Z_COVER, cover, EnumFacing.NORTH, 0xFFFFFFFF, 2));
            builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, SOUTH_Z_COVER, cover, EnumFacing.SOUTH, 0xFFFFFFFF, 2));
            if (particleSprite == null)
            {
                particleSprite = cover;
            }
        }

        return new BakedDynCustomBucket(this, builder.build(), particleSprite, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap());
    }

    /**
     * Sets the fluid in the model.
     * "fluid" - Name of the fluid in the fluid registry.
     * "flipGas" - If "true" the model will be flipped upside down if the fluid is lighter than air. If "false" it won't.
     * "applyTint" - If "true" the model will tint the fluid quads according to the fluid's base color.
     * <p/>
     * If the fluid can't be found, water is used.
     */
    @Override
    public ModelDynCustomBucket process(ImmutableMap<String, String> customData)
    {
        String fluidName = customData.get("fluid");
        Fluid fluid = FluidRegistry.getFluid(fluidName);

        if (fluid == null) fluid = this.fluid;

        boolean flip = flipGas;
        if (customData.containsKey("flipGas"))
        {
            String flipStr = customData.get("flipGas");
            if (flipStr.equals("true")) flip = true;
            else if (flipStr.equals("false")) flip = false;
            else
                throw new IllegalArgumentException(String.format("DynBucket custom data \"flipGas\" must have value \'true\' or \'false\' (was \'%s\')", flipStr));
        }

        boolean tint = this.tint;
        if (customData.containsKey("applyTint"))
        {
            String string = customData.get("applyTint");
            switch (string)
            {
                case "true":  tint = true;  break;
                case "false": tint = false; break;
                default: throw new IllegalArgumentException(String.format("DynBucket custom data \"applyTint\" must have value \'true\' or \'false\' (was \'%s\')", string));
            }
        }
        
        
        /**
         * DEBUG Aufruf
         */

        // create new model with correct liquid
        return new ModelDynCustomBucket(fluid, flip, tint);
    }

    /**
     * Allows to use different textures for the model.
     * There are 3 layers:
     * base - The empty bucket/container
     * fluid - A texture representing the liquid portion. Non-transparent = liquid
     * cover - An overlay that's put over the liquid (optional)
     * <p/>
     * If no liquid is given a hardcoded variant for the bucket is used.
     */
    @Override
    public ModelDynBucket retexture(ImmutableMap<String, String> textures)
    {

        ResourceLocation base = baseLocation;
        ResourceLocation liquid = liquidLocation;
        ResourceLocation cover = coverLocation;

        if (textures.containsKey("base"))
            base = new ResourceLocation(textures.get("base"));
        if (textures.containsKey("fluid"))
            liquid = new ResourceLocation(textures.get("fluid"));
        if (textures.containsKey("cover"))
            cover = new ResourceLocation(textures.get("cover"));

        return new ModelDynBucket(base, liquid, cover, fluid, flipGas, tint);
    }

    public enum LoaderDynCustomBucket implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public boolean accepts(ResourceLocation modelLocation)
        {
            //return modelLocation.getResourceDomain().equals(ForgeVersion.MOD_ID) && modelLocation.getResourcePath().contains("forgebucket");
            return modelLocation.getResourceDomain().equals("sfhcore") && modelLocation.getResourcePath().contains("bucket");
        	//return true;
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation)
        {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager)
        {
            // no need to clear cache since we create a new model instance
        }

        public void register(TextureMap map)
        {
            // only create these textures if they are not added by a resource pack

            if (getResource(new ResourceLocation(ForgeVersion.MOD_ID, "textures/items/bucket_cover.png")) == null)
            {
                ResourceLocation bucketCover = new ResourceLocation(ForgeVersion.MOD_ID, "items/bucket_cover");
                CustomBucketCoverSprite bucketCoverSprite = new CustomBucketCoverSprite(bucketCover);
                map.setTextureEntry(bucketCoverSprite);
            }

            if (getResource(new ResourceLocation(ForgeVersion.MOD_ID, "textures/items/bucket_base.png")) == null)
            {
                ResourceLocation bucketBase = new ResourceLocation(ForgeVersion.MOD_ID, "items/bucket_base");
                CustomBucketBaseSprite bucketBaseSprite = new CustomBucketBaseSprite(bucketBase);
                map.setTextureEntry(bucketBaseSprite);
            }
        }

        @Nullable
        protected static IResource getResource(ResourceLocation resourceLocation)
        {
            try
            {
                return Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
            }
            catch (IOException ignored)
            {
                return null;
            }
        }
    }

    private static final class CustomBucketBaseSprite extends TextureAtlasSprite
    {
        private final ResourceLocation bucket = new ResourceLocation("items/bucket_empty");
        private final ImmutableList<ResourceLocation> dependencies = ImmutableList.of(bucket);

        private CustomBucketBaseSprite(ResourceLocation resourceLocation)
        {
            super(resourceLocation.toString());
        }

        @Override
        public boolean hasCustomLoader(@Nonnull IResourceManager manager, @Nonnull ResourceLocation location)
        {
            return true;
        }

        @Override
        public Collection<ResourceLocation> getDependencies()
        {
            return dependencies;
        }

        @Override
        public boolean load(@Nonnull IResourceManager manager, @Nonnull ResourceLocation location, @Nonnull Function<ResourceLocation, TextureAtlasSprite> textureGetter)
        {
            final TextureAtlasSprite sprite = textureGetter.apply(bucket);
            width = sprite.getIconWidth();
            height = sprite.getIconHeight();
            final int[][] pixels = sprite.getFrameTextureData(0);
            this.clearFramesTextureData();
            this.framesTextureData.add(pixels);
            return false;
        }
    }

    /**
     * Creates a bucket cover sprite from the vanilla resource.
     */
    private static final class CustomBucketCoverSprite extends TextureAtlasSprite
    {
        private final ResourceLocation bucket = new ResourceLocation("items/bucket_empty");
        private final ResourceLocation bucketCoverMask = new ResourceLocation(ForgeVersion.MOD_ID, "items/vanilla_bucket_cover_mask");
        private final ImmutableList<ResourceLocation> dependencies = ImmutableList.of(bucket, bucketCoverMask);

        private CustomBucketCoverSprite(ResourceLocation resourceLocation)
        {
            super(resourceLocation.toString());
        }

        @Override
        public boolean hasCustomLoader(@Nonnull IResourceManager manager, @Nonnull ResourceLocation location)
        {
            return true;
        }

        @Override
        public Collection<ResourceLocation> getDependencies()
        {
            return dependencies;
        }

        @Override
        public boolean load(@Nonnull IResourceManager manager, @Nonnull ResourceLocation location, @Nonnull Function<ResourceLocation, TextureAtlasSprite> textureGetter)
        {
            final TextureAtlasSprite sprite = textureGetter.apply(bucket);
            final TextureAtlasSprite alphaMask = textureGetter.apply(bucketCoverMask);
            width = sprite.getIconWidth();
            height = sprite.getIconHeight();
            final int[][] pixels = new int[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1][];
            pixels[0] = new int[width * height];

            try (
                 IResource empty = LoaderDynCustomBucket.getResource(new ResourceLocation("sfhcore", "textures/items/bucket_empty.png"));
                 IResource mask = LoaderDynCustomBucket.getResource(new ResourceLocation(ForgeVersion.MOD_ID, "textures/items/vanilla_bucket_cover_mask.png"))
            ) {
                // use the alpha mask if it fits, otherwise leave the cover texture blank
                if (empty != null && mask != null && Objects.equals(empty.getResourcePackName(), mask.getResourcePackName()) &&
                        alphaMask.getIconWidth() == width && alphaMask.getIconHeight() == height)
                {
                    final int[][] oldPixels = sprite.getFrameTextureData(0);
                    final int[][] alphaPixels = alphaMask.getFrameTextureData(0);

                    for (int p = 0; p < width * height; p++)
                    {
                        final int alphaMultiplier = alphaPixels[0][p] >>> 24;
                        final int oldPixel = oldPixels[0][p];
                        final int oldPixelAlpha = oldPixel >>> 24;
                        final int newAlpha = oldPixelAlpha * alphaMultiplier / 0xFF;
                        pixels[0][p] = (oldPixel & 0xFFFFFF) + (newAlpha << 24);
                    }
                }
            }
            catch (IOException e)
            {
                FMLLog.log.error("Failed to close resource", e);
            }

            this.clearFramesTextureData();
            this.framesTextureData.add(pixels);
            return false;
        }
    }

    private static final class BakedDynCustomBucketOverrideHandler extends ItemOverrideList
    {
        public static final BakedDynCustomBucketOverrideHandler INSTANCE = new BakedDynCustomBucketOverrideHandler();
        private BakedDynCustomBucketOverrideHandler()
        {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
        	if (!(stack.getItem() instanceof CustomBucket))
        	{
        		return originalModel;
        	}
        	
        	CustomBucket bucket = (CustomBucket) stack.getItem();
        	
            Fluid fluid = bucket.getFluid();
            
            // not a fluid item apparently
            if (fluid == null)
            {
                // empty bucket
                return originalModel;
            }

            BakedDynCustomBucket model = (BakedDynCustomBucket)originalModel;

            String name = fluid.getName();

            if (!model.cache.containsKey(name))
            {
                IModel parent = model.parent.process(ImmutableMap.of("fluid", name));
                Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                IBakedModel bakedModel = parent.bake(new SimpleModelState(model.transforms), model.format, textureGetter);
                model.cache.put(name, bakedModel);
                return bakedModel;
            }

            return model.cache.get(name);
        }
    }

    // the dynamic bucket is based on the empty bucket
    private static final class BakedDynCustomBucket extends BakedItemModel
    {
        private final ModelDynCustomBucket parent;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final VertexFormat format;

        public BakedDynCustomBucket(ModelDynCustomBucket parent,
                              ImmutableList<BakedQuad> quads,
                              TextureAtlasSprite particle,
                              VertexFormat format,
                              ImmutableMap<TransformType, TRSRTransformation> transforms,
                              Map<String, IBakedModel> cache)
        {
            super(quads, particle, transforms, BakedDynCustomBucketOverrideHandler.INSTANCE);
            this.format = format;
            this.parent = parent;
            this.cache = cache;
        }
    }

}
