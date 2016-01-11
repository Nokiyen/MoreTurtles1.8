package noki.moreturtles.turtle.peripheral;
 
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.blocks.RegisterBlocks;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;


/**********
 * @class PeripheralDimensional
 *
 * @description Dimensional Turtleを定義するクラスです。
 * @descriptoin_en Class of Dimensional Turtle.
 * 
 * @see PeripheralDimensionalHosted, PeripheralDimensioalCommand.
 */
public class PeripheralDimensional implements ITurtleUpgrade {
	
	//******************************//
	// define member variables.
	//******************************//
	private int upgradeMeta = 8;
	private Block upgradeBlock = RegisterBlocks.extendedBlocks;
	private ItemStack upgradeItem = new ItemStack(upgradeBlock, 1, upgradeMeta);
	
	@SideOnly(Side.CLIENT)
	public static ModelResourceLocation model_left
		= new ModelResourceLocation("moreturtles:turtle_dimensional_left", "inventory");
	@SideOnly(Side.CLIENT)
	public static ModelResourceLocation model_right
		= new ModelResourceLocation("moreturtles:turtle_dimensional_right", "inventory");
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public ResourceLocation getUpgradeID() {
		
		return new ResourceLocation("moreturtles:dimensional_turtle");
		
	}
	
	@Override
	public int getLegacyUpgradeID() {
		
		return MoreTurtlesData.dimensionalTurtlePID;
		
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		
		return "Dimensional";
		
	}
	
	@Override
	public TurtleUpgradeType getType() {
		
		return  TurtleUpgradeType.Peripheral;
		
	}
	
	@Override
	public ItemStack getCraftingItem() {
		
		return this.upgradeItem;
		
	}
	
	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) {
		
		Matrix4f transform = null;
		ModelManager modelManager = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
		if(side == TurtleSide.Left) {
			return Pair.of(modelManager.getModel(model_left), transform);
		}
		return Pair.of(modelManager.getModel(model_right), transform);
		
	}
	
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		
		return new PeripheralDimensionalHosted(turtle, side);
		
	}
	
	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction) {
		
		return null;
		
	}
	
	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		
	}
	
}
