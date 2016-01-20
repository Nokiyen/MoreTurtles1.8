package noki.moreturtles.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.moreturtles.blocks.BlockExtendedBlocks;
import noki.moreturtles.blocks.RegisterBlocks;


/**********
 * @class BlockExtendedBlocksItem
 *
 * @description BlockExtendedBlocksのItemBlockクラスです。<br>
 * ItemBlockクラスを用意することで、メタデータによってテクスチャを変えるなどのブロックを用意できます。
 * @description_en ItemBlock class of BlockExtendedBlocks.<br>
 * An appropriate ItemBlock class enables you to create metadata-sensitive blocks (textures for each metadata, etc).
 */
public class ItemBlockExtendedBlocks extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	public BlockExtendedBlocks correspondedBlock;

	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockExtendedBlocks(Block block) {
		super(block);
		this.correspondedBlock = (BlockExtendedBlocks)block;
		this.setHasSubtypes(true);
		this.setUnlocalizedName(RegisterBlocks.extendedBlocksName);
	}
	
	@Override
	public int getMetadata(int metadata) {
		
		return metadata;
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
				
		return this.getUnlocalizedName() + "." + this.correspondedBlock.getEachExtendedName(itemstack.getItemDamage());
		
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		
		switch(stack.getItemDamage()) {
		case 2:
		case 3:
		case 4:
		case 5:
			return EnumRarity.UNCOMMON;
		default:
			return EnumRarity.COMMON;	
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		
		return RegisterBlocks.extendedBlocks.getRenderColor(null);
		
	}
	
}
