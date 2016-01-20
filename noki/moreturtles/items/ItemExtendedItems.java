package noki.moreturtles.items;

import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.moreturtles.MoreTurtlesData;


/**********
 * @class ItemExtendedItem
 *
 * @description タートルアップグレード用のアイテムです。<br>
 * ツール系タートルに使います。機能は一切ありません。<br>
 * メタデータ依存のマルチアイテムです(染料などに近い)。<br>
 * 作成元のテクスチャそれぞれに独自のテクスチャを重ねています。
 * @description_en Item for turtle upgrades.<br>
 * It's used for tool-type turtles and has no functionality.<br>
 * It's a multi-item, dependent of its metadata (similar to DyePowder etc).<br>
 * Its texture overlaid the ingredient item's texture.
 *  
 * @note 複数のテクスチャを重ねたアイテムを用意するのに、他のクラスは必要ありません(既存のメソッドで出来ます。)<br>
 * 便利！
 * @note_en You don't need any other class to create overlaid textures item (just use existing methods).<br>
 * How nice!
 */
public class ItemExtendedItems extends Item {
	
	//******************************//
	// define member variables.
	//******************************//
	@SuppressWarnings("serial")
	private static HashMap<Integer, EachItem> extendedItems = 
			new HashMap<Integer, EachItem>() {{ put(0, new EachItem(0, null, 0, "extensionFrame")); }};
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemExtendedItems(String unlocalizedName) {
		
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(MoreTurtlesData.tabMoreTurtles);
		this.setHasSubtypes(true);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		
		int meta = extendedItems.containsKey(itemstack.getItemDamage()) ? itemstack.getItemDamage() : 0;
		
		return this.getUnlocalizedName() + "." + extendedItems.get(meta).name;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	//about List.
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		
		for(EachItem each: extendedItems.values()) {
			list.add(new ItemStack(item, 1, each.index));
		}
		
	}
	
	//**********
	// methods and class for control extended items.
	//**********
	public static void addItem(int index, Item item, int metadata, String name) {
		
		extendedItems.put(index, new EachItem(index, item, metadata, name));
		
	}
	
	private static class EachItem {
		
		public int index;
		public Item item;
		@SuppressWarnings("unused")
		public int metadata;
		public String name;
		
		public EachItem(int index, Item item, int metadata, String name) {
			this.index = index;
			this.item = item;
			this.metadata = metadata;
			this.name = name;
		}
		
	}
	
	public Item getEachExtendedItem(int metadata) {
		
		int meta = extendedItems.containsKey(metadata) ? metadata : 0;
		
		return extendedItems.get(meta).item;
		
	}
	
}
