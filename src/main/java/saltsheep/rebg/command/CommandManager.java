package saltsheep.rebg.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandManager {

	@SideOnly(Side.CLIENT)
	public static void registerClient(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandREBG());
	}
	
	@SideOnly(Side.SERVER)
	public static void registerServer(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandREBG());
	}

}
