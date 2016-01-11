package noki.moreturtles.turtle.tool;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.FishingHooks;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.items.ItemExtendedItems;
import noki.moreturtles.items.RegisterItems;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import noki.moreturtles.turtle.common.EFailureReason;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;


/**********
 * @class TurtleFishery
 *
 * @description Fishery Turtleを定義するクラスです。
 * @description_en Class of Fishery Turtle.
 */
public class ToolFishery implements ITurtleUpgrade {
	
	//******************************//
	// define member variables.
	//******************************//
	private int upgradeMeta = 5;
	private ItemStack upgradeItem = new ItemStack(RegisterItems.extendedItems, 1, this.upgradeMeta);
	
	private boolean start = false;
	private EnumFacing direction = null;
	private long startTime;
	
	private int luckLevel = 0;
	private int addedFuelLevel = 0;
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public ResourceLocation getUpgradeID() {
		
		return new ResourceLocation("moreturtles:fishery_turtle");
		
	}
	
	@Override
	public int getLegacyUpgradeID() {
		return MoreTurtlesData.fisheryTurtleTID;
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		
		return "Fishery";
		
	}
	
	@Override
	public TurtleUpgradeType getType() {
		
		return  TurtleUpgradeType.Tool;
		
	}
	
	@Override
	public ItemStack getCraftingItem() {
		
		return this.upgradeItem;
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) {
		
		float xOffset = (side == TurtleSide.Left) ? -0.40625F : 0.40625F;
		Matrix4f transform = new Matrix4f(0.0F, 0.0F, -1.0F, 1.0F + xOffset, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F,
				0.0F, 0.0F, 0.0F, 1.0F);
		Minecraft mc = Minecraft.getMinecraft();
		return Pair.of(mc.getRenderItem().getItemModelMesher().getItemModel(
				new ItemStack(((ItemExtendedItems)this.upgradeItem.getItem()).getEachExtendedItem(this.upgradeMeta))), transform);		
	}
	
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		
		return null;
		
	}
	
	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		
	}
	
	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction) {
		
		MTTurtleAccess turtleAccess = new MTTurtleAccess(turtle);
		
		switch( verb ) {
			case Dig:
				return this.dig(turtleAccess, direction);
			case Attack:
				return this.attack(turtleAccess, direction);
		}
		return MTTurtleAccess.result(false, EFailureReason.UNKNOWN);
		
	}

	private TurtleCommandResult dig(MTTurtleAccess turtle, EnumFacing dir) {
		
		if(this.start == false) {
			this.direction = null;//reset.
			this.startTime = 0;//reset.
			return MTTurtleAccess.result(false, EFailureReason.NO_CAST);
		}
		this.start = false;//reset.
		
		if(this.direction != null && this.direction != dir) {
			this.direction = null;//reset.
			this.startTime = 0;//reset.
			return MTTurtleAccess.result(false, EFailureReason.WRONG_DIR);
		}
		this.direction = null;//reset.
		
		int newX = turtle.posX + dir.getFrontOffsetX();
		int newY = turtle.posY + dir.getFrontOffsetY();
		int newZ = turtle.posZ + dir.getFrontOffsetZ();
		
		if(this.checkWater(turtle.world, newX, newY, newZ) == 0) {
			this.startTime = 0;//reset.
			return MTTurtleAccess.result(false, EFailureReason.NO_WATER);
		}
		
		//check waters in the 9*9*9 area and calculate the provability about waters.
		int waterCount = 0;
		for(int i = turtle.posX-4; i <= turtle.posX+4; i++) {
			for(int j = turtle.posY-4; j <= turtle.posY+4; j++) {
				for(int k = turtle.posZ-4; k <= turtle.posZ+4; k++) {
					waterCount += this.checkWater(turtle.world, i, j, k);
				}
			}
		}
		double waterValue = waterCount / (9*9*9-1);
		
		//calculate the provability about time;
		long currentTime = turtle.world.getTotalWorldTime();
		long passedTime = currentTime - this.startTime;
		this.startTime = 0;//reset.
		if(passedTime == 0) {
			return MTTurtleAccess.result(false, EFailureReason.NO_FISH);
		}
		double timeValue = passedTime/400;
		
		//check the final provability.
		double totalValue = waterValue + timeValue;
		double targetProv = Math.abs(turtle.world.rand.nextGaussian());
		if(targetProv <= totalValue) {
			//need fuel.
			if(turtle.consumeFuel(MoreTurtlesData.consumedFuelLevel + this.addedFuelLevel) == false) {
				return MTTurtleAccess.result(false, EFailureReason.NO_FUEL);
			}
			
			turtle.store(FishingHooks.getRandomFishable(turtle.world.rand, turtle.world.rand.nextFloat(), this.luckLevel, 0));
			return MTTurtleAccess.result(true);
		}
		
		return MTTurtleAccess.result(false, EFailureReason.NO_FISH);
		
	}
	
	private TurtleCommandResult attack(MTTurtleAccess turtle, EnumFacing dir) {
		
		int newX = turtle.posX + dir.getFrontOffsetX();
		int newY = turtle.posY + dir.getFrontOffsetY();
		int newZ = turtle.posZ + dir.getFrontOffsetZ();
		
		if(this.checkWater(turtle.world, newX, newY, newZ) == 0) {
			return MTTurtleAccess.result(false, EFailureReason.NO_WATER);
		}
		
		this.start = true;
		this.direction = dir;
		this.startTime = turtle.world.getTotalWorldTime();

		return MTTurtleAccess.result(true);
		
	}
	
	private int checkWater(World world, int x, int y, int z) {
		
		IBlockState state = world.getBlockState(new BlockPos(x, y, z));
		if(state.getBlock() == Blocks.water || state.getBlock() == Blocks.flowing_water) {
			return 1;
		}
		
		return 0;
		
	}
	
	public void setLuckLevel(int level) {
		
		this.luckLevel = level;
		
	}
	
	public void setAddedFuelLevel(int level) {
		
		this.addedFuelLevel = level;
		
	}
	
}
