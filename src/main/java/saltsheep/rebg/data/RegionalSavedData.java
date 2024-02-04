package saltsheep.rebg.data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import saltsheep.lib.math.DotAbs;
import saltsheep.rebg.RegionalBackground;

public class RegionalSavedData extends WorldSavedData {

	//*降序队列
	private static Comparator<RegionNBT> cmp = new Comparator<RegionNBT>() {
		public int compare(RegionNBT e1, RegionNBT e2) {
			return e2.priority - e1.priority;
		}
	};
	
	private static final String NAME = "RegionalSavedData";
	
	private final Map<String,RegionNBT> registry = Maps.newHashMap();
	
	public RegionalSavedData() {
		this(NAME+":0");
	}
	
	public RegionalSavedData(WorldServer world) {
		this(NAME+":"+world.provider.getDimension());
	}
	
	public RegionalSavedData(String name) {
		super(name);
	}
	
	public static RegionalSavedData getData(WorldServer world) {
		MapStorage storage = RegionalBackground.getMCServer().getWorld(0).getMapStorage();
		if(storage.getOrLoadData(RegionalSavedData.class, NAME+":"+world.provider.getDimension()) == null) {
			storage.setData(NAME+":"+world.provider.getDimension(), new RegionalSavedData(world));
		}
		return (RegionalSavedData) storage.getOrLoadData(RegionalSavedData.class, NAME+":"+world.provider.getDimension());
	}
	
	public boolean register(RegionNBT region) {
		if(registry.containsKey(region.name))
			return false;
		registry.put(region.name, region);
		this.save();
		return true;
	}
	
	public boolean delete(String name) {
		if(!registry.containsKey(name))
			return false;
		registry.remove(name);
		this.save();
		return true;
	}
	
	@Nullable
	public RegionNBT getRegionSound(EntityPlayerMP player) {
		PriorityQueue<RegionNBT> queue = new PriorityQueue<RegionNBT>(cmp);
		queue.addAll(this.registry.values());
		while(!queue.isEmpty()) {
			RegionNBT region = queue.poll();
			if(region.isInRange(new DotAbs(player.posX,player.posY,player.posZ)))
				if(region.getSoundName()!=null)
					return region;
		}
		return null;
	}
	
	@Nullable
	public RegionNBT getRegionTitle(EntityPlayerMP player) {
		PriorityQueue<RegionNBT> queue = new PriorityQueue<RegionNBT>(cmp);
		queue.addAll(this.registry.values());
		while(!queue.isEmpty()) {
			RegionNBT region = queue.poll();
			if(region.isInRange(new DotAbs(player.posX,player.posY,player.posZ)))
				if(region.getTitleText()!=null)
					return region;
		}
		return null;
	}
	
	public List<RegionNBT> getRegions(){
		return Lists.newArrayList(registry.values());
	}
	
	@Nullable
	public RegionNBT getRegion(String name) {
		return this.registry.get(name);
	}
	
	public void save() {
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound regions = nbt.getCompoundTag("regionalSavedDataBySheep");
		for(String name:regions.getKeySet()) {
			this.register(RegionNBT.fromNBT(regions.getCompoundTag(name)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound regions = new NBTTagCompound();
		for(RegionNBT eachRegion:this.registry.values())
			regions.setTag(eachRegion.name, eachRegion.toNBT());
		compound.setTag("regionalSavedDataBySheep", regions);
		return compound;
	}

}
