package saltsheep.rebg.event;

import java.util.Map;

import com.google.common.collect.Maps;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import saltsheep.lib.sound.SoundHelper;
import saltsheep.rebg.data.RegionNBT;
import saltsheep.rebg.data.RegionalSavedData;

@EventBusSubscriber
public class EventHandler {

	public static Map<String,RegionNBT> sound = Maps.newHashMap();
	public static Map<String,Long> playTime = Maps.newHashMap();
	public static Map<String,RegionNBT> title = Maps.newHashMap();
	
	@SubscribeEvent
	public static void work(PlayerTickEvent event) {
		if(event.player.isServerWorld()&&event.player instanceof EntityPlayerMP&&!(event.player instanceof FakePlayer)) {
			if(event.player.world.getTotalWorldTime()%10==0) {
				RegionNBT newSound = RegionalSavedData.getData((WorldServer) event.player.world).getRegionSound((EntityPlayerMP) event.player);
				RegionNBT newTitle = RegionalSavedData.getData((WorldServer) event.player.world).getRegionTitle((EntityPlayerMP) event.player);
				if(sound.get(event.player.getName())==null||!sound.get(event.player.getName()).equalsSound(newSound)) {
					sound.put(event.player.getName(), newSound);
					playTime.put(event.player.getName(), (long) 0);
					if(newSound!=null) {
						PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
						packetbuffer.writeString("");
						packetbuffer.writeString("");
						((EntityPlayerMP)event.player).connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetbuffer));
					}
				}
				if(title.get(event.player.getName())==null||!title.get(event.player.getName()).equalsTitle(newTitle)) {
					title.put(event.player.getName(), newTitle);
					if(newTitle!=null)
						((EntityPlayerMP)event.player).connection.sendPacket(new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(newTitle.getTitleText())));
				}
				if(canPlay((EntityPlayerMP) event.player))
					playMusic((EntityPlayerMP) event.player);
			}
		}
	}
	
	public static void playMusic(EntityPlayerMP player) {
		RegionNBT region = sound.get(player.getName());
		SoundHelper.playSoundToPlayerInCommon(player, region.getSoundName(), SoundCategory.RECORDS, region.volume, region.pitch, false);
		//DataHasSound.setHasSoundByServer(player, true, region.getSoundName(), region.time);
		playTime.put(player.getName(), System.currentTimeMillis());
	}
	
	public static boolean canPlay(EntityPlayerMP player) {
		if(!playTime.containsKey(player.getName()))
			playTime.put(player.getName(), (long) 0);
		if(sound.get(player.getName())==null)
			return false;
		if(System.currentTimeMillis()-playTime.get(player.getName())>=sound.get(player.getName()).time)
			return true;
		return false;
	}
	
}
