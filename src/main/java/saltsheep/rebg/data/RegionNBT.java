package saltsheep.rebg.data;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import saltsheep.lib.math.Dot;
import saltsheep.lib.math.Range;

public class RegionNBT extends Range {

	private final List<RangeNBT> ranges;
	public final String name;
	public final int priority;
	private String soundName;
	//*(ms)
	public long time;
	public float volume;
	public float pitch;
	private String titleText;
	
	public RegionNBT(String name,int priority,@Nullable String soundName,long time,float volume,float pitch,@Nullable String titleText) {
		this(Lists.newArrayList(),name,priority,soundName,time,volume,pitch,titleText);
	}
	
	public RegionNBT(List<RangeNBT> ranges,String name,int priority,@Nullable String soundName,long time,float volume,float pitch,@Nullable String titleText) {
		this.ranges = ranges;
		this.name = name;
		this.priority = priority;
		this.soundName = soundName;
		this.time = time;
		this.volume = volume;
		this.pitch = pitch;
		this.titleText = titleText;
		if(this.soundName==null)
			this.soundName = "";
		if(this.titleText==null)
			this.titleText = "";
	}
	
	@Override
	public boolean isInRange(Dot dot) {
		for(RangeNBT eachRange:ranges)
			if(eachRange.isInRange(dot))
				return true;
		return false;
	}
	
	public void addRange(RangeNBT range) {
		this.ranges.add(range);
	}
	
	public void clearRange() {
		this.ranges.clear();
	}
	
	@Nullable
	public String getSoundName() {
		if(this.soundName.isEmpty())
			return null;
		return this.soundName;
	}
	
	@Nullable
	public String getTitleText() {
		if(this.titleText.isEmpty())
			return null;
		return this.titleText;
	}
	
	public void setSoundName(@Nullable String soundName) {
		if(soundName==null)
			this.soundName = "";
		else
			this.soundName = soundName;
	}
	
	public void setTitleText(@Nullable String titleText) {
		if(titleText==null)
			this.titleText = "";
		else
			this.titleText = titleText;
	}
	
	public boolean equalsSound(Object obj) {
		if(obj!=null&&obj instanceof RegionNBT) {
			RegionNBT compare = (RegionNBT) obj;
			return this.soundName.equals(compare.soundName)&&this.volume==compare.volume&&this.pitch==compare.pitch;
		}
		return false;
	}
	
	public boolean equalsTitle(Object obj) {
		if(obj!=null&&obj instanceof RegionNBT) {
			RegionNBT compare = (RegionNBT) obj;
			return this.titleText.equals(compare.titleText);
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name:").append(this.name);
		builder.append(',').append("priority:").append(this.priority);
		builder.append('\n').append("sound:").append(this.soundName).append("-(").append(this.time).append(',').append(this.volume).append(',').append(this.pitch).append(')');
		builder.append('\n').append("title:").append(this.titleText);
		for(RangeNBT eachRange:this.ranges) {
			builder.append('\n').append(eachRange.toString());
		}
		return builder.toString();
	}
	
	public NBTTagCompound toNBT() {
		NBTTagCompound region = new NBTTagCompound();
		NBTTagList ranges = new NBTTagList();
		for(RangeNBT eachRange:this.ranges)
			ranges.appendTag(eachRange.toNBT());
		region.setTag("ranges", ranges);
		region.setString("name", this.name);
		region.setInteger("priority", this.priority);
		region.setString("soundName", this.soundName);
		region.setLong("time", this.time);
		region.setFloat("volume", this.volume);
		region.setFloat("pitch", this.pitch);
		region.setString("titleText", this.titleText);
		return region;
	}
	
	public static RegionNBT fromNBT(NBTTagCompound region) {
		NBTTagList rangesAsNBT = region.getTagList("ranges", 10);
		List<RangeNBT> ranges = Lists.newArrayList();
		for(int i=0;i<rangesAsNBT.tagCount();i++)
			ranges.add(RangeNBT.fromNBT(rangesAsNBT.getCompoundTagAt(i)));
		String name = region.getString("name");
		int priority = region.getInteger("priority");
		String soundName = region.getString("soundName");
		long time = region.getLong("time");
		float volume = region.getFloat("volume");
		float pitch = region.getFloat("pitch");
		String titleText = region.getString("titleText");
		return new RegionNBT(ranges,name,priority,soundName,time,volume,pitch,titleText);
	}

}
