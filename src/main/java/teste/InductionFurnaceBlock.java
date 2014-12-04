package teste;

import java.util.Random;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class InductionFurnaceBlock extends BlockContainer /*
														 * implements
														 * ITileEntityProvider
														 */{

	public final static int BOTTOM_SIDE = 0;
	public final static int TOP_SIDE = 1;
	public final static int NORTH_SIDE = 2;
	public final static int SOUTH_SIDE = 3;
	public final static int WEST_SIDE = 4;
	public final static int EAST_SIDE = 5;

	@SideOnly(Side.CLIENT)
	private IIcon top, bottom, left, right, front, back, skinBase;

	public final boolean isBurning;

	private static boolean keepInventory;

	protected InductionFurnaceBlock(Material mat, boolean isBurning) {
		super(mat);
		this.isBurning = isBurning;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			FMLNetworkHandler.openGui(player, testemod.instance,
					testemod.guiIdInductionFurnace, world, x, y, z);
			world.notifyBlockChange(x, y, z, this);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		// System.out.println(meta);
		// System.out.println("Tile Entity criado :" + toString());
		// Item.getItemFromBlock(this);
		return new InductionFurnaceTileEntity();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		top = iconRegister.registerIcon("testemod:MachinePlateBorded");
		bottom = iconRegister
				.registerIcon("testemod:MachineLogisticEntranceSouthFront");
		left = iconRegister.registerIcon("testemod:MachinePlateFanLeft");
		right = iconRegister.registerIcon("testemod:MachinePlateFanRight");
		back = iconRegister.registerIcon("testemod:MachineBack");
		front = iconRegister.registerIcon("testemod:"
				+ (isBurning ? "InductionFurnaceFrontActive"
						: "InductionFurnaceFront"));
		skinBase = iconRegister.registerIcon("testemod:MachinePlateBorded");
	}

	@SideOnly(Side.CLIENT)
	@Override
	// side = the target side of block to place the icon
	// meta = the atual front side of block in world
	public IIcon getIcon(int side, int meta) {
		if (side == InductionFurnaceBlock.TOP_SIDE) {
			return top;
		}
		switch (meta) {

		case InductionFurnaceBlock.NORTH_SIDE: {
			switch (side) {
			case InductionFurnaceBlock.NORTH_SIDE:
				return front;
			case InductionFurnaceBlock.SOUTH_SIDE:
				return back;
			case InductionFurnaceBlock.WEST_SIDE:
				return right;
			case InductionFurnaceBlock.EAST_SIDE:
				return left;
			case InductionFurnaceBlock.BOTTOM_SIDE:
				return bottom;
			}
		}

		case InductionFurnaceBlock.WEST_SIDE: {
			switch (side) {
			case InductionFurnaceBlock.NORTH_SIDE:
				return left;
			case InductionFurnaceBlock.SOUTH_SIDE:
				return right;
			case InductionFurnaceBlock.WEST_SIDE:
				return front;
			case InductionFurnaceBlock.EAST_SIDE:
				return back;
			case InductionFurnaceBlock.BOTTOM_SIDE:
				return bottom;
			}
		}

		case InductionFurnaceBlock.EAST_SIDE: {
			switch (side) {
			case InductionFurnaceBlock.NORTH_SIDE:
				return right;
			case InductionFurnaceBlock.SOUTH_SIDE:
				return left;
			case InductionFurnaceBlock.WEST_SIDE:
				return back;
			case InductionFurnaceBlock.EAST_SIDE:
				return front;
			case InductionFurnaceBlock.BOTTOM_SIDE:
				return bottom;
			}
		}

		default: {
			switch (side) {
			case InductionFurnaceBlock.NORTH_SIDE:
				return back;
			case InductionFurnaceBlock.SOUTH_SIDE:
				return front;
			case InductionFurnaceBlock.WEST_SIDE:
				return left;
			case InductionFurnaceBlock.EAST_SIDE:
				return right;
			case InductionFurnaceBlock.BOTTOM_SIDE:
				return bottom;
			}
		}
		}
		return skinBase;
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(testemod.inductionFurnace);
	}

	@Override
	public Item getItem(World world, int par2, int par3, int par4) {
		return Item.getItemFromBlock(testemod.inductionFurnace);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		direction(world, x, y, z);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase player, ItemStack itemStack) {
		int l = MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		if (itemStack.hasDisplayName()) {
			((InductionFurnaceTileEntity) world.getTileEntity(x, y, z))
					.setGuiDisplayName(itemStack.getDisplayName());
		}
	}

	public void direction(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block direction1 = world.getBlock(x, y, z - 1);
			Block direction2 = world.getBlock(x, y, z + 1);
			Block direction3 = world.getBlock(x - 1, y, z - 1);
			Block direction4 = world.getBlock(x + 1, y, z - 1);

			int byte0 = 3;

			if (direction1.func_149730_j() && !direction2.func_149730_j()) {
				byte0 = 3;
			}

			if (direction2.func_149730_j() && !direction1.func_149730_j()) {
				byte0 = 2;
			}

			if (direction3.func_149730_j() && !direction4.func_149730_j()) {
				byte0 = 5;
			}

			if (direction4.func_149730_j() && !direction3.func_149730_j()) {
				byte0 = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, byte0, 2);
		}
	}

	// chamado par atualizar o bloco em uma derterminada coordenada
	public static void updateFurnaceBlockState(boolean isBurning, World world,
			int x, int y, int z) {

		// obtem o metada atual do bloco
		int direction = world.getBlockMetadata(x, y, z);

		// obtem o tileentity atual da coordenada
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		// sei lá
		keepInventory = true;

		// se ativo
		if (isBurning) {
			//System.out.println(isBurning);
			world.setBlock(x, y, z, testemod.inductionFurnaceAtive);
		} else {
			//System.out.println(isBurning);
			world.setBlock(x, y, z, testemod.inductionFurnace);
		}

		keepInventory = false;

		// atualiza o bloco novo com a metadata antiga
		world.setBlockMetadataWithNotify(x, y, z, direction, 2);

		// restaura a tileentity antiga na coordenada
		if (tileEntity != null) {
			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);
		}
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z,
			int i) {
		return Container.calcRedstoneFromInventory((IInventory) world
				.getTileEntity(x, y, z));
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z,
			Random random) {
		if (isBurning) {
			int direction = world.getBlockMetadata(x, y, z);
			float xx = (float) (x + 0.5F);
			float yy = (float) (y + random.nextFloat() * 6.0F / 16.0F);
			float zz = (float) (z + 0.5F);
			float zz2 = 0.52F;
			float xx2 = random.nextFloat() * 0.6F - 0.3F;

			// System.out.println(direction);
			int counter = 0;

			// strangely, this mode does not work
			/*
			 * switch (direction) { case 4: { world.spawnParticle("smoke",
			 * (double) (xx - zz2), (double) yy, (double) (zz + xx2), 0.0D,
			 * 0.0D, 0.0D); world.spawnParticle("flame", (double) (xx - zz2),
			 * (double) yy, (double) (zz + xx2), 0.0D, 0.0D, 0.0D); } case 5: {
			 * 
			 * world.spawnParticle("smoke", (double) (xx + zz2), (double) yy,
			 * (double) (zz + xx2), 0.0D, 0.0D, 0.0D);
			 * world.spawnParticle("flame", (double) (xx + zz2), (double) yy,
			 * (double) (zz + xx2), 0.0D, 0.0D, 0.0D); } case 2: {
			 * 
			 * world.spawnParticle("smoke", (double) (xx + xx2), (double) yy,
			 * (double) (zz - zz2), 0.0D, 0.0D, 0.0D);
			 * world.spawnParticle("flame", (double) (xx + xx2), (double) yy,
			 * (double) (zz - zz2), 0.0D, 0.0D, 0.0D); } case 3: {
			 * 
			 * world.spawnParticle("smoke", (double) (xx + xx2), (double) yy,
			 * (double) (zz + zz2), 0.0D, 0.0D, 0.0D);
			 * world.spawnParticle("flame", (double) (xx + xx2), (double) yy,
			 * (double) (zz + zz2), 0.0D, 0.0D, 0.0D); } }
			 */

			if (direction == 4 || direction == 5) {
				world.spawnParticle("smoke", (double) (xx + xx2), (double) yy,
						(double) (zz - zz2), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("smoke", (double) (xx + xx2), (double) yy,
						(double) (zz + zz2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("smoke", (double) (xx - zz2), (double)
				// yy,
				// (double) (zz + xx2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("flame", (double) (xx - zz2), (double)
				// yy,
				// (double) (zz + xx2), 0.0D, 0.0D, 0.0D);
				// } else if (direction == 5) {
				// world.spawnParticle("smoke", (double) (xx + xx2), (double)
				// yy,
				// (double) (zz - zz2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("smoke", (double) (xx + xx2), (double)
				// yy,
				// (double) (zz + zz2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("smoke", (double) (xx + zz2), (double)
				// yy,
				// (double) (zz + xx2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("flame", (double) (xx + zz2), (double)
				// yy,
				// (double) (zz + xx2), 0.0D, 0.0D, 0.0D);
			} else if (direction == 2 || direction == 3) {
				world.spawnParticle("smoke", (double) (xx - zz2), (double) yy,
						(double) (zz + xx2), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("smoke", (double) (xx + zz2), (double) yy,
						(double) (zz + xx2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("smoke", (double) (xx + xx2), (double)
				// yy,
				// (double) (zz - zz2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("flame", (double) (xx + xx2), (double)
				// yy,
				// (double) (zz - zz2), 0.0D, 0.0D, 0.0D);
			}// else if (direction == 3) {
				// world.spawnParticle("smoke", (double) (xx - zz2), (double)
				// yy,
				// (double) (zz + xx2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("smoke", (double) (xx + zz2), (double)
				// yy,
				// (double) (zz + xx2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("smoke", (double) (xx + xx2), (double)
				// yy,
				// (double) (zz + zz2), 0.0D, 0.0D, 0.0D);
				// world.spawnParticle("flame", (double) (xx + xx2), (double)
				// yy,
				// (double) (zz + zz2), 0.0D, 0.0D, 0.0D);
			// }
		}
	}

}
