package noki.moreturtles.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noki.moreturtles.MoreTurtlesCore;
import noki.moreturtles.MoreTurtlesData;


/**********
 * @class RegisterUpgradeItems
 *
 * @description MoreTurtlesで追加されるアイテムとそのレシピを登録します。
 * @description_en Registers items of this Mod, and their recipes.
 */
public class RegisterItems {
	
	//******************************//
	// define member variables.
	//******************************//
	public static Item extendedItems;
	public static String extendedItemsName = "MTExtendedItems";

	
	//******************************//
	// define member methods.
	//******************************//
	public static void registerPre() {
		
		extendedItems = new ItemExtendedItems(extendedItemsName);
		GameRegistry.registerItem(extendedItems, extendedItemsName);
		
		MoreTurtlesCore.proxy.registerItemJsonPre();
						
	}
	
	public static void register() {
		
		//--register items.--------//
		ItemExtendedItems.addItem(1, Items.bucket, 0, "bucket");
		ItemExtendedItems.addItem(2, Items.shears, 0, "shears");
		ItemExtendedItems.addItem(3, Items.wheat, 0, "wheat");
		ItemExtendedItems.addItem(4, Items.bowl, 0, "bowl");
		ItemExtendedItems.addItem(5, Items.fishing_rod, 0, "fishingRod");
//		ItemExtendedItems.addItem(6, Items.bow, 0, "bow");
/*		if(MoreTurtlesData.dependency_Bamboo) {
			ItemExtendedItems.addItem(15, MoreTurtlesData.bamboo_bambooI, 0, "bamboo");
		}*/
		if(MoreTurtlesData.dependency_Tofu) {
			ItemExtendedItems.addItem(14, MoreTurtlesData.tofu_tofuStickI, 0, "tofuStick");
		}
		if(MoreTurtlesData.dependency_Maid) {
			ItemExtendedItems.addItem(13, Items.sugar, 0, "sugar");
		}
		
		//--register recipes.------//
		//	extension frame.
		GameRegistry.addRecipe(new ItemStack(extendedItems, 1, 0), 
				"xxx", "xyx", "xxx", 'x', Blocks.glass_pane, 'y', new ItemStack(MoreTurtlesData.cc_disk, 1, 0));
		
		//	extended bucket.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 1),
				new ItemStack(extendedItems, 1, 0), new ItemStack(Items.bucket));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.bucket),
				new ItemStack(extendedItems, 1, 1));
		//	extended shears.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 2),
				new ItemStack(extendedItems, 1, 0), new ItemStack(Items.shears));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.shears),
				new ItemStack(extendedItems, 1, 2));
		//	extended wheat.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 3),
				new ItemStack(extendedItems, 1, 0), new ItemStack(Items.wheat));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.wheat),
				new ItemStack(extendedItems, 1, 3));
		//extended bowl.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 4),
				new ItemStack(extendedItems, 1, 0), new ItemStack(Items.bowl));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.bowl),
				new ItemStack(extendedItems, 1, 4));
		//extended fishing rod.
		GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 5),
				new ItemStack(extendedItems, 1, 0), new ItemStack(Items.fishing_rod));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.fishing_rod),
				new ItemStack(extendedItems, 1, 5));
		//extended bow.
/*		GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 6),
				new ItemStack(extendedItems, 1, 0), new ItemStack(Items.bow));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.bow),
				new ItemStack(extendedItems, 1, 6));*/

		//extended bamboo.
/*		if(MoreTurtlesData.dependency_Bamboo) {
			GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 15),
					new ItemStack(extendedItems, 1, 0), new ItemStack(MoreTurtlesData.bamboo_bambooI));
			GameRegistry.addShapelessRecipe(new ItemStack(MoreTurtlesData.bamboo_bambooI),
					new ItemStack(extendedItems, 1, 15));
		}*/
		//extended kinugoshi tofu.
		if(MoreTurtlesData.dependency_Tofu) {
			GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 14),
					new ItemStack(extendedItems, 1, 0), new ItemStack(MoreTurtlesData.tofu_tofuStickI));
			GameRegistry.addShapelessRecipe(new ItemStack(MoreTurtlesData.tofu_tofuStickI),
					new ItemStack(extendedItems, 1, 14));
		}
		//extended sugar.
		if(MoreTurtlesData.dependency_Maid) {
			GameRegistry.addShapelessRecipe(new ItemStack(extendedItems, 1, 13),
					new ItemStack(extendedItems, 1, 0), new ItemStack(Items.sugar));
			GameRegistry.addShapelessRecipe(new ItemStack(Items.sugar),
					new ItemStack(extendedItems, 1, 13));
		}
		
		MoreTurtlesCore.proxy.registerItemJson();
		
	}
	
}
