package noki.moreturtles.turtle.common;
 
import com.mojang.authlib.GameProfile;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
//import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
//import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeHooks;
import noki.moreturtles.MoreTurtlesData;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IPeripheral;
//import noki.moreturtles.MoreTurtlesConf;
//import dan200.computercraft.api.peripheral.IComputerAccess;
//import dan200.computercraft.api.peripheral.IPeripheral;
//import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;


/**********
 * @class MTTurtleAccess
 *
 * @description ITurtleAccessを拡張したクラスです。<br>
 * ITurtleAccessのメソッドをラップするとともに、汎用的なメソッドを追加します。
 * @description_en Advanced class of ITurtleAccess.<br>
 * Wraps all methods of ITurtleAccess and adds general methods for turtles.
 */
public class MTTurtleAccess {
	
	//******************************//
	// define member variables.
	//******************************//
	public ITurtleAccess turtle;
	
	public World world;
	public BlockPos position;
	public int posX;
	public int posY;
	public int posZ;
	public EntityPlayer playerTurtle;
	public String playerTurtleName = "playerTurtle";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public MTTurtleAccess(ITurtleAccess turtle) {
		
		this.turtle = turtle;
		this.setTurtleInfo();
		
	}
	
	
	public void setTurtleInfo() {
		
		this.world = this.turtle.getWorld();
		this.position = this.turtle.getPosition();
		this.posX = this.position.getX();
		this.posY = this.position.getY();
		this.posZ = this.position.getZ();
		
		if(this.playerTurtle == null || !(this.playerTurtle instanceof FakePlayer)) {
			this.playerTurtle = new PlayerTurtle((WorldServer)this.world, new GameProfile(null, "MoreTurtles"));
		}
		this.playerTurtle.setPosition(posX, posY, posZ);
		
	}
	
	//**********
	// methods for wrapping.
	//**********
	public World getWorld() {
		
		return this.turtle.getWorld();
		
	}
	
	public BlockPos getPosition() {
		
		return this.turtle.getPosition();
		
	}
	
	public boolean teleportTo(World world, BlockPos pos) {
		
		return this.turtle.teleportTo(world, pos);
		
	}
	
	public Vec3 getVisualPosition(float fraction) {
		
		return this.turtle.getVisualPosition(fraction);
		
	}
	
	public float getVisualYaw(float fraction) {
		
		return this.turtle.getVisualYaw(fraction);
		
	}
	
	public EnumFacing getDirection() {
		
		return this.turtle.getDirection();
		
	}
	
	public void setDirection(EnumFacing dir) {
		
		this.turtle.setDirection(dir);
		
	}
	
	public int getSelectedSlot() {
		
		return this.turtle.getSelectedSlot();
		
	}

	public void setSelectedSlot(int slot) {
		
		this.turtle.setSelectedSlot(slot);

	}
	
	public IInventory getInventory() {
		
		return this.turtle.getInventory();
		
	}
	
	public boolean isFuelNedded() {
		
		return this.turtle.isFuelNeeded();
		
	}
	
	public int getFuelLevel() {
		
		return this.turtle.getFuelLevel();
		
	}
	
	public void setFuelLevel(int fuel) {
		
		this.turtle.setFuelLevel(fuel);
		
	}
	
	public int getFuelLimit() {
		
		return this.turtle.getFuelLimit();
		
	}
	
	public boolean consumeFuel(int fuel) {
		
		if(MoreTurtlesData.consumeFuel == false) {
			return true;
		}
		return this.turtle.consumeFuel(fuel);
		
	}
	
	public void addFuel(int fuel) {
		
		this.turtle.addFuel(fuel);
		
	}
	
	public Object[] executeCommand(ILuaContext context, ITurtleCommand command) throws LuaException, InterruptedException {
		
		return this.turtle.executeCommand(context, command);
		
	}
	
	public void playAnimation(TurtleAnimation animation) {
		
		this.turtle.playAnimation(animation);
		
	}
	
	public ITurtleUpgrade getUpgrade(TurtleSide side) {
		
		return this.turtle.getUpgrade(side);
		
	}
	
	public void setUpgrade(TurtleSide side, ITurtleUpgrade upgrade) {
		
		this.turtle.setUpgrade(side, upgrade);
		
	}
	
	public IPeripheral getPeripheral(TurtleSide side) {
		
		return this.turtle.getPeripheral(side);
		
	}
	
	public NBTTagCompound getUpgradeNBTData(TurtleSide side) {
		
		return this.turtle.getUpgradeNBTData(side);
		
	}
	
	public void updateUpgradeNBTData(TurtleSide side) {
		
		this.turtle.updateUpgradeNBTData(side);
		
	}
	
	
	//**********
	// added methods.
	//**********
	/**
	 * @return can be NULL.
	 */
	public ITurtleUpgrade getOtherUpgrade(TurtleSide side) {
				
		return this.getUpgrade(getOppositeSide(side));
		
	}
	
	/**
	 * @return can be NULL.
	 */
	public String getOtherUpgradeName(TurtleSide side) {
				
		ITurtleUpgrade oppositeUpgrade = this.getOtherUpgrade(side);
		if(oppositeUpgrade != null) {
			return oppositeUpgrade.getUnlocalisedAdjective();
		}
		
		return null;
		
	}
	
	/**
	 * @return can be NULL.
	 */
	public ItemStack getOtherUpgradeItem(TurtleSide side) {
		
		ITurtleUpgrade oppositeUpgrade = this.getOtherUpgrade(side);
		if(oppositeUpgrade != null) {
			return oppositeUpgrade.getCraftingItem();
		}
		
		return null;
		
	}
	
	/**
	 * @return can be NULL.
	 */
	public TurtleUpgradeType getOtherUpgradeType(TurtleSide side) {
		
		ITurtleUpgrade oppositeUpgrade = this.getOtherUpgrade(side);
		if(oppositeUpgrade != null) {
			return oppositeUpgrade.getType();
		}
		
		return null;
		
	}
	
	public ItemStack getSlotContents(int slotNum) {
		
		IInventory inventory = this.turtle.getInventory();
		return inventory.getStackInSlot(slotNum);
		
	}
	
	public void setSlotContents(int slotNum, ItemStack stack) {
		
		IInventory inventory = this.turtle.getInventory();
		inventory.setInventorySlotContents(slotNum, stack);
		
	}
	
	public boolean storeItemStack(ItemStack stack) {
		
		IInventory inventory = this.turtle.getInventory();
		int currentSlot = this.getSelectedSlot();
		int slotSize = inventory.getSizeInventory();
		
		int i = currentSlot;
		do {
			ItemStack currentStack = this.getSlotContents(i);
			
			if(currentStack == null) {
				this.setSlotContents(i, stack.copy());
				stack = null;
				return true;
			}
			else if(currentStack.isStackable() && currentStack.isItemEqual(stack)) {
				int space = currentStack.getMaxStackSize() - currentStack.stackSize;
				if(space == 0) {
					//	go to (*).
				}
				else if(space >= stack.stackSize) {
					currentStack.stackSize += stack.stackSize;
					stack = null;
					return true;
				}
				else {
					currentStack.stackSize = currentStack.getMaxStackSize();
					stack.stackSize -= space;
					//	go to (*).
				}
			}
			
			i = (i+1) % slotSize;	// (*).
		} while(i != currentSlot);
		
		return false;
		
	}
	
	public void dropItemStack(ItemStack stack, EnumFacing direction) {
		
		double zDir;
		double xDir;
		double yDir;
		if (direction != null) {
			xDir = direction.getFrontOffsetX();
			yDir = direction.getFrontOffsetY();
			zDir = direction.getFrontOffsetZ();
		}
		else {
			xDir = 0.0D;
			yDir = 0.0D;
			zDir = 0.0D;
		}
		
		double xPos = this.posX + 0.5D + xDir * 0.4D;
		double yPos = this.posY + 0.5D + yDir * 0.4D;
		double zPos = this.posZ + 0.5D + zDir * 0.4D;
		
		EntityItem entityItem = new EntityItem(this.world, xPos, yPos, zPos, stack);
	    entityItem.motionX = (xDir * 0.7D + world.rand.nextFloat() * 0.2D - 0.1D);
	    entityItem.motionY = (yDir * 0.7D + world.rand.nextFloat() * 0.2D - 0.1D);
	    entityItem.motionZ = (zDir * 0.7D + world.rand.nextFloat() * 0.2D - 0.1D);
	    entityItem.setDefaultPickupDelay();
	    world.spawnEntityInWorld(entityItem);
		
	}
	
	public void store(ItemStack stack) {
		
		if (!this.storeItemStack(stack)) {
			this.dropItemStack(stack, this.turtle.getDirection());
		}
		
	}
	
	public void consume(int selectedSlot) {
		
		ItemStack currentStack = this.getSlotContents(selectedSlot);
		--currentStack.stackSize;
		if (currentStack.stackSize == 0) {
			this.setSlotContents(selectedSlot, null);
		}
		
	}
	
	public void setCurrentItem(ItemStack stack) {
		
		this.playerTurtle.inventory.clear();
		this.playerTurtle.inventory.setInventorySlotContents(0, stack);
		this.playerTurtle.inventory.currentItem = 0;
		
	}
	
	public void clearInventory() {
		
		this.playerTurtle.inventory.clear();
		this.playerTurtle.inventory.currentItem = 0;
		
	}
		
	public boolean canTurtleHarvest(ItemStack stack, Block block, BlockPos pos) {
		
		this.playerTurtle.inventory.clear();
		this.playerTurtle.inventory.setInventorySlotContents(0, stack);
		this.playerTurtle.inventory.currentItem = 0;
		
		return ForgeHooks.canHarvestBlock(block, playerTurtle, this.world, pos);
		
	}
	
	public float getBlockStrength(ItemStack stack, IBlockState state, int metadata, BlockPos pos) {
		
		this.playerTurtle.inventory.clear();
		this.playerTurtle.inventory.setInventorySlotContents(0, stack);
		this.playerTurtle.inventory.currentItem = 0;

		return ForgeHooks.blockStrength(state, this.playerTurtle, this.world, pos);
		
	}
	
	public static TurtleCommandResult result(boolean result) {
		
		if(result == true) {
			return TurtleCommandResult.success();
		}
		else {
			return TurtleCommandResult.failure();
		}
		
	}
	
	public static TurtleCommandResult result(boolean result, EFailureReason reason) {
		
		if(result == true) {
			return TurtleCommandResult.success();
		}
		else {
			return TurtleCommandResult.failure(reason.getMessage());
		}
		
	}
	
	public static TurtleAnimation getSwingAnimation(TurtleSide side) {
		
		if(side == TurtleSide.Left) {
			return TurtleAnimation.SwingLeftTool;
		}
		else {
			return TurtleAnimation.SwingRightTool;
		}
		
	}
	
	public static TurtleAnimation getOtherSwingAnimation(TurtleSide side) {
		
		if(side == TurtleSide.Left) {
			return TurtleAnimation.SwingRightTool;
		}
		else {
			return TurtleAnimation.SwingLeftTool;
		}
		
	}
	
	public static TurtleSide getOppositeSide(TurtleSide side) {
		
		if(side == TurtleSide.Left) {
			return TurtleSide.Right;
		}
		else {
			return TurtleSide.Left;
		}

	}
}
