package noki.moreturtles.turtle.tool;
 
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.items.ItemExtendedItems;
import noki.moreturtles.items.RegisterItems;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;


/**********
 * @class TurtleMilking
 *
 * @description Milking Turtleを定義するクラスです。
 * @description_en Class of Milking Turtle.
 */
public class ToolMilking implements ITurtleUpgrade {
	
	//******************************//
	// define member variables.
	//******************************//
	public int upgradeMeta = 4;
	public ItemStack upgradeItem = new ItemStack(RegisterItems.extendedItems, 1, this.upgradeMeta);
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public ResourceLocation getUpgradeID() {
		
		return new ResourceLocation("moreturtles:milking_turtle");
		
	}
	
	@Override
	public int getLegacyUpgradeID() {
		
		return MoreTurtlesData.milkingTurtleTID;
		
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		
		return "Milking";
		
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
				
		int newX = turtle.posX + dir.getFrontOffsetX();
		int newY = turtle.posY + dir.getFrontOffsetY();
		int newZ = turtle.posZ + dir.getFrontOffsetZ();
		
		//define the region for searching targets.
		AxisAlignedBB aabb = new AxisAlignedBB(newX, newY, newZ, newX+1.0D, newY+1.0D, newZ+1.0D);
		//get entities.
		@SuppressWarnings("rawtypes")
		List list = turtle.world.getEntitiesWithinAABBExcludingEntity(turtle.playerTurtle, aabb);
		if(list == null || list.size() == 0) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}
		
		//get current slot.
		int currentSlot = turtle.getSelectedSlot();
		ItemStack currentItem = turtle.getSlotContents(currentSlot);
		if(currentItem == null) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		}
		
		//try to get milk or stew.
		for(Object each: list) {
			if(each != null) {
				if(each instanceof EntityCow && currentItem.getItem() == Items.bucket) {
					turtle.consume(currentSlot);
					turtle.store(new ItemStack(Items.milk_bucket));
					
					return MTTurtleAccess.result(true);
				}
				if(each instanceof EntityMooshroom && currentItem.getItem() == Items.bowl) {
					turtle.consume(currentSlot);
					turtle.store(new ItemStack(Items.mushroom_stew));
					
					return MTTurtleAccess.result(true);
				}
			}
		}
		
		return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		
	}
	
	private TurtleCommandResult attack(MTTurtleAccess turtle, EnumFacing dir) {

		return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK);
		
	}
	
}
