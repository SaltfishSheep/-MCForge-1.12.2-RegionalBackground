package saltsheep.rebg.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import saltsheep.lib.command.CommandFather;
import saltsheep.lib.command.CommandSon;
import saltsheep.lib.common.BaseType;
import saltsheep.lib.exception.InformationUnsameException;
import saltsheep.lib.exception.InputIllegalException;
import saltsheep.lib.math.RangeCube;
import saltsheep.lib.math.RangePlaneSquare;
import saltsheep.rebg.data.RangeNBT;
import saltsheep.rebg.data.RegionNBT;
import saltsheep.rebg.data.RegionalSavedData;

public class CommandREBG extends CommandFather {

	@Override
	public String getName() {
		return "region";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "§c/region compound add <区域名> <优先级(整数)>添加新区域管理\n§c/region compound del <区域名>删除指定区域管理\n§c/region compound list列出所有区域管理\n§c/region compound sound <区域名> <音乐名> <时长(秒)> <音量> <音高>设置区域音乐\n§c/region compound title <区域名> <标题>设置区域标题\n§c/region compound view <区域名>获取指定区域所有信息\n§c/region range clear <区域名>情况指定区域管理的全部范围\n§c/region range add square <区域名> <x1> <z1> <x2> <z2>添加平面范围\n§c/region range add cube <区域名> <x1> <y1> <z1> <x2> <y2> <z2>添加长方体范围";
	}

	@Override
	protected void initCommandSons() {
		try {
			this.sons.add(new CommandSon(true, 4, new String[] {"compound","add"}, new BaseType[] {BaseType.STRING,BaseType.INT}, 
				(objs)->{
					if(RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).register(new RegionNBT((String) objs[1], (int) objs[2], null, 0, 0, 0, null))) {
						RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).save();
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Add region successful"));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Failed to add region."));
				}
			));
			this.sons.add(new CommandSon(true, 3, new String[] {"compound","del"}, new BaseType[] {BaseType.STRING}, 
				(objs)->{
					if(RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).delete((String) objs[1])) {
						RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).save();
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Delete region successful."));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Failed to delete region."));
				}
			));
			this.sons.add(new CommandSon(true, 2, new String[] {"compound","list"}, null, 
				(objs)->{
					StringBuilder builder = new StringBuilder();
					for(RegionNBT each:RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).getRegions()) {
						builder.append(each.name).append(',');
					}
					((ICommandSender) objs[0]).sendMessage(new TextComponentString(builder.toString()));
				}
			));
			this.sons.add(new CommandSon(true, 7, new String[] {"compound","sound"}, new BaseType[] {BaseType.STRING,BaseType.STRING,BaseType.DOUBLE,BaseType.FLOAT,BaseType.FLOAT}, 
				(objs)->{
					RegionalSavedData data = RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world);
					RegionNBT region = data.getRegion((String) objs[1]);
					if(region!=null) {
						region.setSoundName((String) objs[2]);
						region.time = (long) (((double)objs[3])*1000);
						region.volume = (float) objs[4];
						region.pitch = (float) objs[5];
						RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).save();
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Edit region successful,now information:\n"+region.toString()));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Warning!Null has such region."));
				}
			));
			this.sons.add(new CommandSon(true, 4, new String[] {"compound","title"}, new BaseType[] {BaseType.STRING,BaseType.STRING}, 
				(objs)->{
					RegionalSavedData data = RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world);
					RegionNBT region = data.getRegion((String) objs[1]);
					if(region!=null) {
						region.setTitleText((String) objs[2]);
						RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).save();
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Edit region successful,now information:\n"+region.toString()));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Warning!Null has such region."));
				}
			));
			this.sons.add(new CommandSon(true, 3, new String[] {"compound","view"}, new BaseType[] {BaseType.STRING}, 
				(objs)->{
					RegionalSavedData data = RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world);
					RegionNBT region = data.getRegion((String) objs[1]);
					if(region!=null) {
						((ICommandSender) objs[0]).sendMessage(new TextComponentString(region.toString()));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Warning!Null has such region."));
				}
			));
			this.sons.add(new CommandSon(true, 3, new String[] {"range","clear"}, new BaseType[] {BaseType.STRING}, 
				(objs)->{
					RegionalSavedData data = RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world);
					RegionNBT region = data.getRegion((String) objs[1]);
					if(region!=null) {
						region.clearRange();
						RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).save();
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Edit region successful,now information:\n"+region.toString()));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Warning!Null has such region."));
				}
			));
			this.sons.add(new CommandSon(true, 8, new String[] {"range","add","square"}, new BaseType[] {BaseType.STRING,BaseType.DOUBLE,BaseType.DOUBLE,BaseType.DOUBLE ,BaseType.DOUBLE}, 
				(objs)->{
					RegionalSavedData data = RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world);
					RegionNBT region = data.getRegion((String) objs[1]);
					if(region!=null) {
						region.addRange(new RangeNBT(new RangePlaneSquare((double)objs[2], (double)objs[3], (double)objs[4], (double)objs[5])));
						RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).save();
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Edit region successful,now information:\n"+region.toString()));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Warning!Null has such region."));
				}
			));
			this.sons.add(new CommandSon(true, 10, new String[] {"range","add","cube"}, new BaseType[] {BaseType.STRING,BaseType.DOUBLE,BaseType.DOUBLE,BaseType.DOUBLE ,BaseType.DOUBLE,BaseType.DOUBLE,BaseType.DOUBLE}, 
				(objs)->{
					RegionalSavedData data = RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world);
					RegionNBT region = data.getRegion((String) objs[1]);
					if(region!=null) {
						region.addRange(new RangeNBT(new RangeCube((double)objs[2], (double)objs[3], (double)objs[4], (double)objs[5], (double)objs[6], (double)objs[7])));
						RegionalSavedData.getData((WorldServer) ((EntityPlayer)objs[0]).world).save();
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Edit region successful,now information:\n"+region.toString()));
					}else
						((ICommandSender) objs[0]).sendMessage(new TextComponentString("Warning!Null has such region."));
				}
			));
		} catch (InformationUnsameException | InputIllegalException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.executeIn(server, sender, args);
	}

}
