package noki.moreturtles.turtle.tool;
 
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
 * @class TurtleFeeding
 *
 * @description Feeding Turtleを定義するクラスです。
 * @description_en Class of Feeding Turtle.
 */
public class ToolFeeding implements ITurtleUpgrade {
	
	//******************************//
	// define member variables.
	//******************************//
	private int upgradeMeta = 3;
	private ItemStack upgradeItem = new ItemStack(RegisterItems.extendedItems, 1, this.upgradeMeta);
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public ResourceLocation getUpgradeID() {
		
		return new ResourceLocation("moreturtles:feeding_turtle");
		
	}
	
	@Override
	public int getLegacyUpgradeID() {
		
		return MoreTurtlesData.feedingTurtleTID;
		
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		
		return "Feeding";
		
	}

	@Override
	public TurtleUpgradeType getType() {
		
		return  TurtleUpgradeType.Tool;
		
	}
	
	@Override
	public ItemStack getCraftingItem() {
		
		return upgradeItem;
		
	}
	
	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
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
		
		return MTTurtleAccess.result(false, EFailureReason.NO_DIG);
		
	}
	
	private TurtleCommandResult attack(MTTurtleAccess turtle, EnumFacing dir) {
		
		int newX = turtle.posX + dir.getFrontOffsetX();
		int newY = turtle.posY + dir.getFrontOffsetY();
		int newZ = turtle.posZ + dir.getFrontOffsetZ();
		
		//define the region for searching targets.
		AxisAlignedBB aabb = new AxisAlignedBB(newX, newY, newZ, newX+1.0D, newY+1.0D, newZ+1.0D);
		//get entities.
		@SuppressWarnings("rawtypes")
		List list = turtle.world.getEntitiesWithinAABBExcludingEntity(turtle.playerTurtle, aabb);
		if(list == null || list.size() == 0) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK_TARGET);
		}
		
		//get current slot.
		int currentSlot = turtle.getSelectedSlot();
		ItemStack currentItem = turtle.getSlotContents(currentSlot);
		if(currentItem == null) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		}
		
		//try to feed.
		for(Object each: list) {
			if(each != null && each instanceof EntityAnimal) {
				EntityAnimal target = (EntityAnimal)each;
				if(target.isBreedingItem(currentItem) && target.getGrowingAge() == 0 && target.isInLove() == false) {
					turtle.consume(currentSlot);
					target.setInLove(turtle.playerTurtle);
					
					return MTTurtleAccess.result(true);
				}
			}
		}
		
		return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		
	}
	
}
