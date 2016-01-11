package noki.moreturtles.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import noki.moreturtles.MoreTurtlesCore;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.items.ItemBlockExtendedBlocks;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**********
 * @class RegisterUpgradeBlocks
 *
 * @description MoreTurtlesで追加されるブロックとそのレシピを登録します。
 * @description_en Registers blocks of this Mod, and their recipes.
 */
public class RegisterBlocks {
	
	//******************************//
	// define member variables.
	//******************************//
	public static Block extendedBlocks;
	public static String extendedBlocksName = "MTExtendedBlocks";

	
	//******************************//
	// define member methods.
	//******************************//
	public static void registerPre() {
		
		extendedBlocks = new BlockExtendedBlocks(extendedBlocksName);
		GameRegistry.registerBlock(extendedBlocks, ItemBlockExtendedBlocks.class, extendedBlocksName);
		
		MoreTurtlesCore.proxy.registerBlockJsonPre();
						
	}
	
	public static void register() {
		
		//--register blocks.-------//
		BlockExtendedBlocks.addBlock(1, Blocks.enchanting_table, 0, "enchantingTable");
		BlockExtendedBlocks.addBlock(2, Blocks.enchanting_table, 0, "enchantingTableFortune");
		BlockExtendedBlocks.addBlock(3, Blocks.enchanting_table, 0, "enchantingTableSilk");
		BlockExtendedBlocks.addBlock(4, Blocks.enchanting_table, 0, "enchantingTableLooting");
		BlockExtendedBlocks.addBlock(5, Blocks.enchanting_table, 0, "enchantingTableLuck");
		BlockExtendedBlocks.addBlock(6, Blocks.daylight_detector, 0, "daylightDetector");
		BlockExtendedBlocks.addBlock(7, Blocks.grass, 0, "grass");
		BlockExtendedBlocks.addBlock(8, Blocks.end_stone, 0, "endStone");
		BlockExtendedBlocks.addBlock(9, Blocks.dispenser, 3, "dispenser");
		if(MoreTurtlesData.dependency_Toho) {
			BlockExtendedBlocks.addBlock(15, MoreTurtlesData.toho_danmakuCraftingB, 0, "danmaku");
		}
		
		//--register recipes.------//
		//	extension cube.
		GameRegistry.addRecipe(new ItemStack(extendedBlocks, 1, 0), 
				"xxx", "xyx", "xxx", 'x', Blocks.glass, 'y', new ItemStack(MoreTurtlesData.cc_disk));
		
		//	extended enchanting table.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 1),
				new ItemStack(extendedBlocks, 1, 0), new ItemStack(Blocks.enchanting_table));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.enchanting_table),
				new ItemStack(extendedBlocks, 1, 1));
		//	extended enchanting table fortune.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 2),
				new ItemStack(extendedBlocks, 1, 1), new ItemStack(Blocks.diamond_block));
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 1),
				new ItemStack(extendedBlocks, 1, 2));
		//	extended enchanting table silk.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 3),
				new ItemStack(extendedBlocks, 1, 1), new ItemStack(Blocks.ice));
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 1),
				new ItemStack(extendedBlocks, 1, 3));
		//	extended enchanting table looting.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 4),
				new ItemStack(extendedBlocks, 1, 1), new ItemStack(Items.ghast_tear));
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 1),
				new ItemStack(extendedBlocks, 1, 4));
		//	extended enchanting table luck.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 5),
				new ItemStack(extendedBlocks, 1, 1), new ItemStack(Items.fish, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 1),
				new ItemStack(extendedBlocks, 1, 5));
		//	extended daylight detector.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 6),
				new ItemStack(extendedBlocks, 1, 0), new ItemStack(Blocks.daylight_detector));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.daylight_detector),
				new ItemStack(extendedBlocks, 1, 6));
		//	extended grass.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 7),
				new ItemStack(extendedBlocks, 1, 0), new ItemStack(Blocks.grass));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.grass),
				new ItemStack(extendedBlocks, 1, 7));
		//	extended end stone.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 8),
				new ItemStack(extendedBlocks, 1, 0), new ItemStack(Blocks.end_stone));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.end_stone),
				new ItemStack(extendedBlocks, 1, 8));
		//	extended dispenser.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 9),
				new ItemStack(extendedBlocks, 1, 0), new ItemStack(Blocks.dispenser));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.dispenser),
				new ItemStack(extendedBlocks, 1, 9));
		
		//	extended danmaku crafting table.
		if(MoreTurtlesData.dependency_Toho) {
			GameRegistry.addShapelessRecipe(new ItemStack(extendedBlocks, 1, 15),
					new ItemStack(extendedBlocks, 1, 0), new ItemStack(MoreTurtlesData.toho_danmakuCraftingB, 1, 0));
			GameRegistry.addShapelessRecipe(new ItemStack(MoreTurtlesData.toho_danmakuCraftingB, 1, 0),
					new ItemStack(extendedBlocks, 1, 15));
		}
		
		MoreTurtlesCore.proxy.registerBlockJson();
		
	}
	
}
