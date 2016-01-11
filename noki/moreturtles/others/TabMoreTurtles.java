package noki.moreturtles.others;

import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.blocks.RegisterBlocks;


/**********
 * @class TabMoreTurtles
 *
 * @description MoreTurtlesのクリエイティブタブです。
 * @description_en MoreTurtles' creative tab.
 */
public class TabMoreTurtles extends CreativeTabs {

	//******************************//
	// define member variables.
	//******************************//
	public static String label = "MoreTurtles";

	
	//******************************//
	// define member methods.
	//******************************//
	public TabMoreTurtles() {
		
		super(label);
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		
		return new ItemStack(RegisterBlocks.extendedBlocks, 1, 0);
		
	}
	
	@Override
	public Item getTabIconItem() {
		
		return Item.getItemFromBlock(RegisterBlocks.extendedBlocks);
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllReleventItems(List list) {
		
		super.displayAllReleventItems(list);
		this.addUpgradedTurtles(list);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	private void addUpgradedTurtles(List list) {
		
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.liquidTurtleTID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.shearsTurtleTID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.feedingTurtleTID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.milkingTurtleTID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.fisheryTurtleTID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.silkTouchTurtlePID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.fortuneTurtlePID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.lootingTurtlePID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.luckTurtlePID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.lightTurtlePID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.biomeTurtlePID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.dimensionalTurtlePID));
		list.add(this.getUpgradedTurtleStack(MoreTurtlesData.projectileTurtlePID));
		
	}
	
	@SideOnly(Side.CLIENT)
	private ItemStack getUpgradedTurtleStack(int upgradeId) {
		
		ItemStack stack = new ItemStack(MoreTurtlesData.cc_turtleExpanded);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort("leftUpgrade", (short)upgradeId);
		stack.setTagCompound(nbt);
		return stack;
		
	}
	
}
