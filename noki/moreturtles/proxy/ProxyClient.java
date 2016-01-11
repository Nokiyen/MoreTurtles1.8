package noki.moreturtles.proxy;

import java.io.IOException;
import com.google.common.base.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.moreturtles.ModInfo;
import noki.moreturtles.blocks.RegisterBlocks;
import noki.moreturtles.items.RegisterItems;
import noki.moreturtles.proxy.ProxyCommon;


/**********
 * @class ProxyClient
 *
 * @description クライアント用proxyクラス。
 * @description_en Proxy class for Client.
 */
public class ProxyClient implements ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void registerRenderers() {
		
		//	extended blocks.
/*		MoreTurtlesData.renderID_extendedBlocks = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new RenderBlockExtendedBlocks());*/
		
	}
	
	@Override
	public void registerItemJsonPre() {
		
		ModelBakery.addVariantName(RegisterItems.extendedItems,
				ModInfo.ID.toLowerCase() + ":" + "extension_frame",
				ModInfo.ID.toLowerCase() + ":" + "extended_bucket",
				ModInfo.ID.toLowerCase() + ":" + "extended_shears",
				ModInfo.ID.toLowerCase() + ":" + "extended_wheat",
				ModInfo.ID.toLowerCase() + ":" + "extended_bowl",
				ModInfo.ID.toLowerCase() + ":" + "extended_fishing_rod");
		
	}
	
	@Override
	public void registerItemJson() {
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ModelResourceLocation resource;
		
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extension_frame", "inventory");
		mesher.register(RegisterItems.extendedItems, 0, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_bucket", "inventory");
		mesher.register(RegisterItems.extendedItems, 1, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_shears", "inventory");
		mesher.register(RegisterItems.extendedItems, 2, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_wheat", "inventory");
		mesher.register(RegisterItems.extendedItems, 3, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_bowl", "inventory");
		mesher.register(RegisterItems.extendedItems, 4, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_fishing_rod", "inventory");
		mesher.register(RegisterItems.extendedItems, 5, resource);
		
	}
	
	@Override
	public void registerBlockJsonPre() {
		
		ModelBakery.addVariantName(Item.getItemFromBlock(RegisterBlocks.extendedBlocks),
				ModInfo.ID.toLowerCase() + ":" + "extension_cube",
				ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table",
				ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_fortune",
				ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_silk",
				ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_looting",
				ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_luck",
				ModInfo.ID.toLowerCase() + ":" + "extended_daylight_detector",
				ModInfo.ID.toLowerCase() + ":" + "extended_grass",
				ModInfo.ID.toLowerCase() + ":" + "extended_end_stone",
				ModInfo.ID.toLowerCase() + ":" + "extended_dispenser");
		
	}
	
	@Override
	public void registerBlockJson() {
				
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ModelResourceLocation resource;
		
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extension_cube", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 0, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 1, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_fortune", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 2, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_silk", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 3, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_looting", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 4, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_enchanting_table_luck", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 5, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_daylight_detector", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 6, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_grass", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 7, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_end_stone", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 8, resource);
		resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + "extended_dispenser", "inventory");
		mesher.register(Item.getItemFromBlock(RegisterBlocks.extendedBlocks), 9, resource);
		
	}
	
	@Override
	public void registerSidedEvent() {
		
		MinecraftForge.EVENT_BUS.register(new BakeEvent());
		
	}
	
	private class BakeEvent {
		@SubscribeEvent
		public void onModelBakeEvent(ModelBakeEvent event) {
			this.loadModel(event, "turtle_biome_left");
			this.loadModel(event, "turtle_biome_right");
			this.loadModel(event, "turtle_light_left");
			this.loadModel(event, "turtle_light_right");
			this.loadModel(event, "turtle_projectile_left");
			this.loadModel(event, "turtle_projectile_right");
			this.loadModel(event, "turtle_dimensional_left");
			this.loadModel(event, "turtle_dimensional_right");
			this.loadModel(event, "turtle_fortune_left");
			this.loadModel(event, "turtle_fortune_right");
			this.loadModel(event, "turtle_silk_left");
			this.loadModel(event, "turtle_silk_right");
			this.loadModel(event, "turtle_looting_left");
			this.loadModel(event, "turtle_looting_right");
			this.loadModel(event, "turtle_luck_left");
			this.loadModel(event, "turtle_luck_right");
		}
		
		private void loadModel(ModelBakeEvent event, String name) {
			try {
				IModel model = event.modelLoader.getModel(new ResourceLocation("moreturtles", "block/" + name));
				Function<ResourceLocation, TextureAtlasSprite> function = new Function<ResourceLocation, TextureAtlasSprite>() {
					@Override
					public TextureAtlasSprite apply(ResourceLocation location) {
						return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
					}
				};
				@SuppressWarnings("deprecation")
				IBakedModel bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, function);
				event.modelRegistry.putObject(new ModelResourceLocation("moreturtles:" + name, "inventory"), bakedModel);
			}
			catch(IOException e) {
		        System.out.println("Could not load model: name");
		        e.printStackTrace();
			}
		}
	}
	
}
