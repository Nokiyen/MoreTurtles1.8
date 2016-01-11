package noki.moreturtles.blocks;

import java.util.HashMap;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import noki.moreturtles.MoreTurtlesData;


/**********
 * @class BlockExtendedBlocks
 *
 * @description タートルアップグレード用のブロックです。<br>
 * 周辺機器系タートルに使います。機能は一切ありません。<br>
 * メタデータ依存のマルチブロックです(羊毛ブロックなどに近い)。<br>
 * 作成元のテクスチャそれぞれに独自のテクスチャを重ねています。
 * @description_en Block for turtle upgrades.<br>
 * It's used for peripheral-type turtles and has no functionality.<br>
 * It's a multi-block, dependent of its metadata (similar to Wool etc).<br>
 * Its texture overlaid the ingredient block's texture.
 * 
 * @see BlockExtendedBlocksItem, RenderBlockExtendedBlocks.
 * 
 * @note メタデータ依存マルチブロックを作るには、ItemBlockクラスを用意しなければなりません。<br>
 * 複数のテクスチャを重ねたブロックを作るには、専用のブロックレンダークラスを用意しなければなりません。
 * @note_en You have to create ItemBlock class to make metadata-sensitive multi-block.<br>
 * You have to create a class for special block rendering, to make an overlaid textures block.
 */
public class BlockExtendedBlocks extends Block {

	//******************************//
	// define member variables.
	//******************************//
	@SuppressWarnings("serial")
	private static HashMap<Integer, EachBlock> extendedBlocks = 
			new HashMap<Integer, EachBlock>() {{ put(0, new EachBlock(0, null, 0, "extensionCube")); }};
	private static final PropertyInteger METADATA = PropertyInteger.create("metadata", 0, 15);
	
	
	//******************************//
	// define member methods.
	//******************************//
	protected BlockExtendedBlocks(String unlocalizedName) {
		
		super(Material.glass);
		this.setUnlocalizedName(unlocalizedName);
		this.setHardness(0.3F);
		this.setStepSound(soundTypeGlass);
		this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, 0));
		this.setCreativeTab(MoreTurtlesData.tabMoreTurtles);
		
	}
	
/*	@Override
	public int damageDropped(int metadata) {
		
		return metadata;
		
	}*/
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	//about List.
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		
		for(EachBlock each: extendedBlocks.values()) {
			list.add(new ItemStack(item, 1, each.index));
		}
		
	}
	
	@Override
	public boolean isOpaqueCube() {
		
		return false;
		
	}
	
	@Override
	public boolean isFullCube() {
		
		return false;
		
	}
	
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		
		return EnumWorldBlockLayer.TRANSLUCENT;
		
	}
	
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		
		return this.getBlockColor();
		
	}
	
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		
		return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
		
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return (Integer)state.getValue(METADATA);
		
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata) {
		
		return this.getDefaultState().withProperty(METADATA, metadata);
		
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		
		return this.getMetaFromState(state);
		
	}
	
	@Override
	protected BlockState createBlockState() {
		
		return new BlockState(this, METADATA);
		
	}
	
	//**********
	// methods and class for control extended blocks.
	//**********	
	public static void addBlock(int index, Block block, int metadata, String name) {
		
		extendedBlocks.put(index, new EachBlock(index, block, metadata, name));
		
	}
	
	private static class EachBlock {
		
		public int index;
		public Block block;
		public int metadata;
		public String name;
		
		public EachBlock(int index, Block block, int metadata, String name) {
			this.index = index;
			this.block = block;
			this.metadata = metadata;
			this.name = name;
		}
		
	}
	
	public Block getEachExtendedBlock(int metadata) {
		
		int meta = extendedBlocks.containsKey(metadata) ? metadata : 0;
		
		return extendedBlocks.get(meta).block;
		
	}
	
	public int getEachExtendedMetadata(int metadata) {
		
		int meta = extendedBlocks.containsKey(metadata) ? metadata : 0;
		
		return extendedBlocks.get(meta).metadata;
		
	}

	public String getEachExtendedName(int metadata) {
		
		int meta = extendedBlocks.containsKey(metadata) ? metadata : 0;
		
		return extendedBlocks.get(meta).name;
		
	}
	
}
