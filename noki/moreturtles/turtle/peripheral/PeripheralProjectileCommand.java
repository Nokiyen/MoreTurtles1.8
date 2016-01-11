package noki.moreturtles.turtle.peripheral;

import java.lang.reflect.Method;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.RegistrySimple;
import net.minecraft.world.World;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;


/**********
 * @class PeripheralProjectileCommand
 *
 * @description Projectile Turtleのマンドを定義するクラスです。
 * @description_en Class of the command of Projectile Turtle.
 */
public class PeripheralProjectileCommand implements ITurtleCommand {
	
	//******************************//
	// define member variables.
	//******************************//
	MTTurtleAccess turtle;
	boolean setCharge;
	float charge = 0.0F;

	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralProjectileCommand(MTTurtleAccess turtle, boolean setCharge, float charge) {
		
		this.turtle = turtle;
		this.setCharge = setCharge;
		this.charge = charge;
		
	}

	@Override
	public TurtleCommandResult execute(ITurtleAccess givenTurtle) {
		
		//	check the item stack.
		int currentSlot = this.turtle.getSelectedSlot();
		ItemStack currentStack = this.turtle.getSlotContents(currentSlot);
		if(currentStack == null) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		}
		
		if(((RegistrySimple)BlockDispenser.dispenseBehaviorRegistry).containsKey(currentStack.getItem()) == false) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		}
		
		IBehaviorDispenseItem tmpItem =
				(IBehaviorDispenseItem)BlockDispenser.dispenseBehaviorRegistry.getObject(currentStack.getItem());
		//case of splash portion.
		//splash portion is IProjectile, but can't get form dispenseBehaviroRegistry...
		if(currentStack.getItem() == Items.potionitem && ItemPotion.isSplash(currentStack.getItemDamage())) {
			tmpItem = new ShootSplashPortion(currentStack);
		}
		else if(!(tmpItem instanceof BehaviorProjectileDispense)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		}
		
		//	get entity.
		double x = this.turtle.posX + this.turtle.getDirection().getFrontOffsetX() + 0.5D;
		double y = this.turtle.posY + this.turtle.getDirection().getFrontOffsetY() + 0.5D;
		double z = this.turtle.posZ + this.turtle.getDirection().getFrontOffsetZ() + 0.5D;
		
		BehaviorProjectileDispense item = (BehaviorProjectileDispense)tmpItem;
		
		Method method1 = null;
		Entity entity = null;
		try {
			method1 = ReflectionHelper.findMethod(BehaviorProjectileDispense.class, item,
				new String[]{"getProjectileEntity", "func_82499_a"}, World.class, IPosition.class);
			entity = (Entity)method1.invoke(item, this.turtle.world, new ShootPosition(x, y, z));
		}
		catch(Exception e) {
			return MTTurtleAccess.result(false, EFailureReason.JAVA);
		}
		if(method1 == null || entity == null) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);			
		}
		
		//	set entity fields.
		entity.motionX = (double)(-MathHelper.sin(this.turtle.playerTurtle.rotationYaw / 180.0F * (float)Math.PI) *
				MathHelper.cos(this.turtle.playerTurtle.rotationPitch / 180.0F * (float)Math.PI));
		entity.motionZ = (double)(MathHelper.cos(this.turtle.playerTurtle.rotationYaw / 180.0F * (float)Math.PI) *
				MathHelper.cos(this.turtle.playerTurtle.rotationPitch / 180.0F * (float)Math.PI));
		entity.motionY = (double)(-MathHelper.sin(this.turtle.playerTurtle.rotationPitch / 180.0F * (float)Math.PI));
		
		float charge;
		if(this.setCharge) {
			charge = this.charge*2.0F;
		}
		else {
			Method method2 = null;
			try {
				method2 = ReflectionHelper.findMethod(BehaviorProjectileDispense.class, item,
					new String[]{"func_82498_a"});
				charge = ((Float)method2.invoke(item, new Object[]{})).floatValue();
			}
			catch(Exception e) {
				return MTTurtleAccess.result(false, EFailureReason.JAVA);
			}
		}
		
		((IProjectile)entity).setThrowableHeading(entity.motionX, entity.motionY, entity.motionZ, charge*1.5F, 1.0F);
		
		//	shoot.
		this.turtle.world.spawnEntityInWorld(entity);
		
		Method method3 = null;
		try {
			method3 = ReflectionHelper.findMethod(BehaviorProjectileDispense.class, item,
				new String[]{"playDispenseSound", "func_82485_a"}, IBlockSource.class);
			method3.invoke(item, new ShootSource(this.turtle));
		}
		catch(Exception e) {
//			return MTTurtleAccess.result(false, EFailureReason.JAVA);
		}
		
		--currentStack.stackSize;
		if(currentStack.stackSize == 0) {
			this.turtle.setSlotContents(currentSlot, null);
		}
		
		return MTTurtleAccess.result(true);
		
	}
	
	private class ShootPosition implements IPosition {	
		private double x;
		private double y;
		private double z;
		
		public ShootPosition(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;			
		}

		@Override
		public double getX() {			
			return this.x;			
		}

		@Override
		public double getY() {			
			return this.y;			
		}

		@Override
		public double getZ() {			
			return this.z;
		}	
	}
	
	private class ShootSource implements IBlockSource {		
		private MTTurtleAccess turtle;
		
		public ShootSource(MTTurtleAccess turtle) {
			this.turtle = turtle;
		}

		@Override
		public World getWorld() {
			return this.turtle.world;
		}

		@Override
		public double getX() {
			return this.turtle.posX + 0.5D;
		}

		@Override
		public double getY() {
			return this.turtle.posY + 0.5D;
		}

		@Override
		public double getZ() {
			return this.turtle.posZ + 0.5D;
		}
		
		@Override
		public int getBlockMetadata() {
			return 0;
		}
		
		@Override
		public TileEntity getBlockTileEntity() {
			return null;
		}

		@Override
		public BlockPos getBlockPos() {
			return new BlockPos(this.turtle.posX, this.turtle.posY, this.turtle.posZ);
		}

		@Override
		public Block getBlock() {
			return null;
		}	
	}
	
	private class ShootSplashPortion extends BehaviorProjectileDispense {
		private ItemStack stack;
		
		public ShootSplashPortion(ItemStack stack) {
			this.stack = stack;
		}
		
		@Override
		protected IProjectile getProjectileEntity(World world, IPosition position) {
			return new EntityPotion(world, position.getX(), position.getY(), position.getZ(), stack.copy());
		}
		
		@Override
		protected float func_82498_a() {
			return super.func_82498_a() * 0.5F;
		}
		
		@Override
		protected float func_82500_b() {
			return super.func_82500_b() * 1.25F;
		}		
	}

}
