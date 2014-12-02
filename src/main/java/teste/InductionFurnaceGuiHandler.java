package teste;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class InductionFurnaceGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity != null) {
			switch (ID) {
			case testemod.guiIdInductionFurnace:
				if (entity instanceof InductionFurnaceTileEntity) {
					return new InductionFurnaceContainer(player.inventory,(InductionFurnaceTileEntity) entity);
				}
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity != null) {
			switch (ID) {
			case testemod.guiIdInductionFurnace:
				if (entity instanceof InductionFurnaceTileEntity) {
					return new InductionFurnaceGui(player.inventory,(InductionFurnaceTileEntity) entity);
				}
			}
		}
		return null;
	}
}
