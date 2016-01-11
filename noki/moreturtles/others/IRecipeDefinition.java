package noki.moreturtles.others;

import net.minecraft.item.ItemStack;

public interface IRecipeDefinition {
	
	boolean isShaped();
	
	Object[] getComponent();
	
	boolean isReversible();
	
	ItemStack getReversed();

}
