package saltsheep.rebg;

import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import saltsheep.rebg.command.CommandManager;

@Mod(modid = RegionalBackground.MODID, name = RegionalBackground.NAME, version = RegionalBackground.VERSION, useMetadata = true, dependencies="required-after:sheeplib;", acceptedMinecraftVersions="1.12.2",acceptableRemoteVersions="*")
public class RegionalBackground
{
    public static final String MODID = "regionalbackground";
    public static final String NAME = "RegionalBackground";
    public static final String VERSION = "1.1";
    public static RegionalBackground instance;

    private static Logger logger;

    public RegionalBackground() {
    	instance = this;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        //saltsheep.rebg.network.NetworkHandler.register();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    }
    
    @EventHandler
    @SideOnly(Side.CLIENT)
    public static void onServerStartingInClient(FMLServerStartingEvent event){
		CommandManager.registerClient(event);
	}
    
    @EventHandler
    @SideOnly(Side.SERVER)
    public static void onServerStartingInServer(FMLServerStartingEvent event){
		CommandManager.registerServer(event);
	}
    
    public static Logger getLogger() {
    	return logger;
    }
    
    public static MinecraftServer getMCServer() {
    	return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
    
    public static void printError(Throwable error) {
    	String messages = "";
    	for(StackTraceElement stackTrace : error.getStackTrace()) {
    		messages = messages+stackTrace.toString()+"\n";
		}
    	RegionalBackground.getLogger().error("警告！在咸羊我的mod里出现了一些错误，信息如下：\n"+messages+"出现错误类型:"+error.getClass()+"-"+error.getMessage());
    }
    
    public static void info(String str) {
    	logger.info(str);
    }
    
    public static void info(Object obj) {
    	if(obj == null)
    		logger.info("null has such obj.");
    	else
    		logger.info(obj.toString());
    }
    
    
}
