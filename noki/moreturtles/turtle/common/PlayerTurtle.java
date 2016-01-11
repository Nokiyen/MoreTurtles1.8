package noki.moreturtles.turtle.common;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class PlayerTurtle extends FakePlayer {

	public PlayerTurtle(WorldServer world, GameProfile name) {
		super(world, name);
	}	

}
