package saltsheep.rebg.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import saltsheep.lib.math.Dot;
import saltsheep.lib.math.DotAbs;
import saltsheep.lib.math.Range;
import saltsheep.lib.math.RangeCube;
import saltsheep.lib.math.RangePlaneSquare;
import saltsheep.lib.nbt.SheepNBT;

public class RangeNBT extends Range {

	public static final int RANGE_SQUARE = 0;
	public static final int RANGE_CUBE = 1;
	
	private final Range rangeIn;
	
	public RangeNBT(Range rangeIn) {
		this.rangeIn = rangeIn;
	}
	
	@Override
	public boolean isInRange(Dot dot) {
		return this.rangeIn.isInRange(dot);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(this.rangeIn instanceof RangePlaneSquare) {
			builder.append("square:from-(").append(((RangePlaneSquare) this.rangeIn).xMin).append(',').append(((RangePlaneSquare) this.rangeIn).yMin).append("),to-(").append(((RangePlaneSquare) this.rangeIn).xMax).append(',').append(((RangePlaneSquare) this.rangeIn).yMax).append(')');
		}else if(this.rangeIn instanceof RangeCube) {
			DotAbs from = ((RangeCube) this.rangeIn).posMin;
			DotAbs to = ((RangeCube) this.rangeIn).posMax;
			builder.append("cube:from-(").append(from.xAbs()).append(',').append(from.yAbs()).append(',').append(from.zAbs()).append("),to-(").append(to.xAbs()).append(',').append(to.yAbs()).append(',').append(to.zAbs()).append(')');
		}
		return builder.toString();
	}
	
	public NBTTagCompound toNBT() {
		NBTTagCompound region = new NBTTagCompound();
		if(this.rangeIn instanceof RangePlaneSquare) {
			RangePlaneSquare asSquare = (RangePlaneSquare) this.rangeIn;
			region.setInteger("type", RANGE_SQUARE);
			new SheepNBT(region).setList("from", new Double[]{asSquare.xMin,asSquare.yMin}).setList("to", new Double[]{asSquare.xMax,asSquare.yMax});
		}else if(this.rangeIn instanceof RangeCube) {
			RangeCube asCube = (RangeCube) this.rangeIn;
			region.setInteger("type", RANGE_CUBE);
			new SheepNBT(region).setList("from", new Double[]{asCube.posMin.xAbs(),asCube.posMin.yAbs(),asCube.posMin.zAbs()}).setList("to", new Double[]{asCube.posMax.xAbs(),asCube.posMax.yAbs(),asCube.posMax.zAbs()});
		}
		return region;
	}
	
	public static RangeNBT fromNBT(NBTTagCompound region) {
		switch(region.getInteger("type")) {
		case RANGE_SQUARE:
			NBTTagList from1 = region.getTagList("from", 6);
			NBTTagList to1 = region.getTagList("to", 6);
			return new RangeNBT(new RangePlaneSquare(from1.getDoubleAt(0), from1.getDoubleAt(1), to1.getDoubleAt(0), to1.getDoubleAt(1)));
		case RANGE_CUBE:
			NBTTagList from2 = region.getTagList("from", 6);
			NBTTagList to2 = region.getTagList("to", 6);
			return new RangeNBT(new RangeCube(from2.getDoubleAt(0), from2.getDoubleAt(1), from2.getDoubleAt(2), to2.getDoubleAt(0), to2.getDoubleAt(1), to2.getDoubleAt(2)));
		default:
			return new RangeNBT(Range.NOMATCH);
		}
	}

}
