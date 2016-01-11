package noki.moreturtles.event;

import net.minecraftforge.common.MinecraftForge;

public class RegisterEvents {
	
	public static void register() {
		
		MinecraftForge.EVENT_BUS.register(new EventTurtle());
		
	}

}
