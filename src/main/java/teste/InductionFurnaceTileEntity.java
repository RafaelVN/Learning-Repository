package teste;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class InductionFurnaceTileEntity extends TileEntity implements
		ISidedInventory {

	// Slots id
	public static final int[] INGREDIENT_STOCK = new int[] { 0, 1, 2, 3, 4 };

	public static final int[] INGREDIENT_PLACE = new int[] { 5, 6, 7, 8, 9 };

	public static final int[] RESULT = new int[] { 10, 11, 12, 13, 14 };

	// testing
	public static final int FUEL_PLACE = 15;

	private String localizedName;

	// private static final int[] slots_top = new int[] { 0 };
	// private static final int[] slots_bottom = new int[] { 2, 1 };
	// private static final int[] slots_sides = new int[] { 1 };

	private ItemStack[] slots = new ItemStack[16];

	public int getSizeIventory() {
		return slots.length;
	}

	public String getInvName() {
		return isInvNameLocalized() ? localizedName
				: "container.inductionFurnace";
	}

	public boolean isInvNameLocalized() {
		return localizedName != null && localizedName.length() > 0;
	}

	public void setGuiDisplayName(String name) {
		localizedName = name;
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	// retorna o slot do parametro
	// Get usado pelo container (mais especificamente pelo slot) para obter o
	// itemstack na tileentity para Slot no container
	// a combinação de set e get com a variável especifica e id a vincula com o
	// Slot para a mesma id
	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	// diminui a quantidade de unidades no slot e retorna a quantidade removida
	// ou a quantidade antiga do slot(caso o resto da subitração chegue a 0 ou
	// menor)
	@Override
	public ItemStack decrStackSize(int slot, int qtd) {
		// System.out.println("decr inv");
		if (slots[slot] != null) {
			ItemStack itemStack;

			// se a quantidade atual do slot for menor ou igual ao do parametro
			if (slots[slot].stackSize <= qtd) {
				itemStack = slots[slot];
				slots[slot] = null;
				return itemStack;
				// do contrário
			} else {
				// diminiu qtd no slot
				itemStack = slots[slot].splitStack(qtd);
				// eu tenho uma pequena impressão de que esta rotina jamais será
				// chamada mais deixa para lá
				if (slots[slot].stackSize == 0) {
					slots[slot] = null;
				}
				return itemStack;
			}
		}
		return null;
	}

	// retorna o item no slot e o remove
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (slots[slot] != null) {
			ItemStack itemStack = slots[slot];
			slots[slot] = null;
			return itemStack;
		}
		return null;
	}

	// Set usado pelo container (mais especificamente pelo slot) para setar o
	// itemstack do Slot na variável aki na tileentity
	// a combinação de set e get com a variável especifica e id a vincula com o
	// Slot para a mesma id
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		slots[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		// System.out
		// .println("Read !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		/*
		 * super.readFromNBT(nbt); NBTTagList list = nbt.getTagList("Items",
		 * 10); slots = new ItemStack[getSizeInventory()];
		 * 
		 * for (int i = 0; i < list.tagCount(); i++) { NBTTagCompound compound =
		 * (NBTTagCompound) list.getCompoundTagAt(i); byte b =
		 * compound.getByte("Slot");
		 * 
		 * if (b >= 0 && b < slots.length) { slots[b] =
		 * ItemStack.loadItemStackFromNBT(compound); } } burnTime =
		 * nbt.getShort("BurnTime"); cookTime = nbt.getShort("CookTime");
		 * currentItemBurnTime = nbt.getShort("CurrentItemBurnTime"); //
		 * currentItemBurnTime = getItemBurnTime(slots[1]);
		 * 
		 * if (nbt.hasKey("CustomName")) { localizedName =
		 * nbt.getString("CustomName"); }
		 */
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		// System.out.println(nbt.toString());
		// System.out
		// .println("Write !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		/*
		 * super.writeToNBT(nbt); nbt.setShort("BurnTime", (short) burnTime);
		 * nbt.setShort("CookTime", (short) cookTime);
		 * nbt.setShort("CurrentItemBurnTime", (short) currentItemBurnTime);
		 * NBTTagList list = new NBTTagList();
		 * 
		 * for (int i = 0; i < slots.length; ++i) { if (slots[i] != null) {
		 * NBTTagCompound compound = new NBTTagCompound();
		 * compound.setByte("Slot", (byte) i); slots[i].writeToNBT(compound);
		 * list.appendTag(compound); } } nbt.setTag("Items", list);
		 * 
		 * if (isInvNameLocalized()) { nbt.setString("CustomName",
		 * localizedName); }
		 */
	}

	public void writeOnlyTileToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	}

	// determina se pode ser usado pelo jogador nas circunstãncias atual
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// considerar o bloco ativo no mundo , a rotina abaixo sempre ira
		// retorna true contudo caso o bloco não se
		// encontrar no mundo então ira retorna falso;
		// ex: se ele estiver vinculado a um item e não a um bloco no mundo

		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false
				: player.getDistanceSq((double) xCoord + 0.5D,
						(double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64.0D;

	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote) {
			updateImpl();
		}
	}

	private boolean canPlaceStocked(int i) {
		if (slots[INGREDIENT_PLACE[i]] == null
				&& slots[INGREDIENT_STOCK[i]] != null) {
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(
					slots[INGREDIENT_STOCK[i]]);
			if (itemStack == null) {
				return false;
			}
			if (slots[RESULT[i]] == null) {
				return true;
			} else if (!slots[RESULT[i]].isItemEqual(itemStack)) {
				return false;
			}
			int result = slots[RESULT[i]].stackSize + itemStack.stackSize;
			return (result <= getInventoryStackLimit() && result <= itemStack
					.getMaxStackSize());
		}
		return false;
	}

	// obtem o tempo de combustão do item caso exista
	public static int getItemFuelValue(ItemStack itemStack) {
		if (itemStack == null) {
			return 0;
		} else {

			Item item = itemStack.getItem();

			if (item instanceof ItemBlock
					&& Block.getBlockFromItem(item) != Blocks.air) {

				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.wooden_slab) {
					return 150;
				}
				if (block.getMaterial() == Material.wood) {
					return 300;
				}
				if (block == Blocks.coal_block) {
					return 16000;
				}
			}

			if (item instanceof ItemTool
					&& ((ItemTool) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item instanceof ItemSword
					&& ((ItemSword) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item instanceof ItemHoe
					&& ((ItemHoe) item).getToolMaterialName().equals("WOOD"))
				return 200;

			if (itemStack.getItem() == Items.stick)
				return 100;
			if (itemStack.getItem() == Items.coal)
				return 1600;
			if (itemStack.getItem() == Items.lava_bucket)
				return 20000;
			if (itemStack.getItem() == Item.getItemFromBlock(Blocks.sapling))
				return 100;
			if (itemStack.getItem() == Items.blaze_rod)
				return 2400;

			// isn't a real recipe
			if (itemStack.getItem() == Items.quartz)
				return 250;

			return GameRegistry.getFuelValue(itemStack);
		}
	}

	// analisa se um item tem característica de combustão
	public static boolean isItemFuel(ItemStack itemStack) {
		return getItemFuelValue(itemStack) > 0;
	}

	// testa a validade de um item para determinado slot
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		// System.out.println("Sloting");
		// return slot == 2 ? false : (slot == 1 ? isItemFuel(itemStack) :
		// true);
		return true;
	}

	// determina a acessibilidade dos slots com relação aos lados do bloco
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		// return side == 0 ? slots_bottom : (side == 1 ? slots_top :
		// slots_sides);
		return null;
	}

	// analisa se o item pode ser inserido em um slot por meio de um dos lados
	// do bloco
	// para sistemas de automatização
	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		// System.out.println("Inserting");
		return this.isItemValidForSlot(slot, itemStack);
	}

	// analisa se o item pode ser extraido em um slot por meio de um dos lados
	// do bloco
	// para sistemas de automatização
	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		// System.out.println("Extracting");
		// return side != 0 || slot != 1 || itemStack.getItem() == Items.bucket;
		return true;
	}

	public int getFuelRemainingScaled(int scale) {
		if (currentItemFuel == 0) {
			currentItemFuel = furnaceSpeed;
		}
		return fuel * scale / currentItemFuel;
	}

	public int getCookProgressScaled(int scale, int i) {
		return cookTime[i] * scale / furnaceSpeed;
	}

	public void sendPowerSwitchMsg() {
		if (worldObj.isRemote) {
			NBTTagCompound nbt = new NBTTagCompound();
			writeOnlyTileToNBT(nbt);
			nbt.setInteger("msg", PacketMannager.POWER_SWITCH_MSG);

			int dimensionId = worldObj.provider.dimensionId;
			nbt.setInteger("dimension", dimensionId);

			testemod.networkWrapper.sendToServer(new PacketMannager(nbt));
		}
	}

	public static class PacketMannager implements IMessage,

	IMessageHandler<PacketMannager, IMessage> {

		public static final int POWER_SWITCH_MSG = 0;

		public NBTTagCompound nbt;

		// desired by packet inner system
		public PacketMannager() {
		}

		public PacketMannager(NBTTagCompound nbt) {
			this.nbt = nbt;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			nbt = ByteBufUtils.readTag(buf);
		}

		@Override
		public void toBytes(ByteBuf buf) {
			ByteBufUtils.writeTag(buf, nbt);
		}

		@Override
		public IMessage onMessage(PacketMannager message, MessageContext ctx) {
			NBTTagCompound nbtMsg = message.nbt;
			int msg = nbtMsg.getInteger("msg");
			int x, y, z, dimensionId;
			TileEntity tileEntity;
			InductionFurnaceTileEntity castedTileEntity;
			boolean powerFlag;

			switch (msg) {
			case POWER_SWITCH_MSG: {

				dimensionId = nbtMsg.getInteger("dimension");
				x = nbtMsg.getInteger("x");
				y = nbtMsg.getInteger("y");
				z = nbtMsg.getInteger("z");

				World world = DimensionManager.getWorld(dimensionId);
				if (world != null) {
					System.out.println(world);
					tileEntity = world.getTileEntity(x, y, z);
					System.out.println(tileEntity);
					System.out.println(x + "," + y + "," + z + ","
							+ dimensionId);
					if (tileEntity != null) {
						if (tileEntity instanceof InductionFurnaceTileEntity) {
							// castedTileEntity = (InductionFurnaceTileEntity)
							// tileEntity;
							((InductionFurnaceTileEntity) tileEntity)
									.switchPower();
							// powerFlag = castedTileEntity.powerFlag;
							// castedTileEntity.powerFlag = !powerFlag;
						}
					}
				}
			}
			}
			return null;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// determine if the furnace is active
	private boolean isBurning;

	// on/off flag
	private boolean powerFlag;

	// furnace burning speed
	// will be change
	public int furnaceSpeed = 100;

	// how long this furnace will continue to burn for
	private int fuel;

	// the start time for this fuel
	private int currentItemFuel;

	// how long time left before cooked
	private int[] cookTime = new int[5];

	public void setBurning(boolean b) {
		if (worldObj.isRemote) {
			isBurning = b;
		}
	}

	public boolean isBurning() {
		return isBurning;
	}

	void switchPower() {
		powerFlag = !powerFlag;
	}

	public void setPower(boolean power) {
		if (worldObj.isRemote) {
			powerFlag = power;
		}
	}

	public boolean isPowerOn() {
		return powerFlag;
	}

	public void setFuelValue(int fuel) {
		if (worldObj.isRemote) {
			this.fuel = fuel;
		}
	}

	public int getFuelValue() {
		return fuel;
	}

	public void setCurrentItemFuel(int currentItemFuel) {
		if (worldObj.isRemote) {
			this.currentItemFuel = currentItemFuel;
		}
	}

	public int getCurrentItemFuel() {
		return currentItemFuel;
	}

	public void setCookTime(int cookTime, int i) {
		if (worldObj.isRemote) {
			this.cookTime[i] = cookTime;
		}
	}

	public int getCookTime(int i) {
		return cookTime[i];
	}

	public int[] getAllCookTimes() {
		return cookTime.clone();
	}

	private void updateImpl() {
		boolean lastState = isBurning;
		boolean isStateChanged;
		boolean[] isSmelting = new boolean[5];
		boolean isSomeSmelting = isStateChanged = false;

		// ////////////////////////////////////////////////////////////////////////
		if (hasFuel() && powerFlag) { // caso exista combustível carregado
			for (int i = 0; i < 5; i++) {
				if (trySmelt(i)) {
					consumeFuelandCook(i);
					if (cookTime[i] == furnaceSpeed) {
						makeSmelt(i);
					}
					isSomeSmelting = true;
				}
			}
			isBurning = isSomeSmelting;

			// ////////////////////////////////////////////////////////////////////////
		} else if (hasReserveFuelStock() && powerFlag) { // do contrário caso
															// exista
			// combustível em estoque
			for (int i = 0; i < 5; i++) {
				if (trySmelt(i)) {
					loadFuel();
					consumeFuelandCook(i);
					if (cookTime[i] == furnaceSpeed) {
						makeSmelt(i);
					}
					isSomeSmelting = true;
				}
			}
			isBurning = isSomeSmelting;

			// /////////////////////////////////////////////////////////////////////////
		} else { // do contrário
			isBurning = false;
		}

		if (lastState != isBurning) {
			isStateChanged = true;
			if (!isBurning) {
				// se houve uma mudança de estado e esta mudança acarretou em um
				// desligamento
				// reseta todas as cookTime
				for (int i = 0; i < 5; i++) {
					cookTime[i] = 0;
				}
			}
		}
		if (isStateChanged) {
			// isTileEntityStateChanged = true;
			InductionFurnaceBlock.updateFurnaceBlockState(isBurning, worldObj,
					xCoord, yCoord, zCoord);
		}
	}

	private void loadFuel() {
		if (fuel <= 0) {
			currentItemFuel = fuel = getItemFuelValue(slots[FUEL_PLACE]);
			slots[FUEL_PLACE].stackSize--;
			if (slots[FUEL_PLACE].stackSize == 0) {
				// return null to any non container items, return item container
				// in case of buckets and others
				slots[FUEL_PLACE] = slots[FUEL_PLACE].getItem()
						.getContainerItem(slots[FUEL_PLACE]);
			}
		}
	}

	private void consumeFuelandCook(int i) {
		// will be changed
		fuel--;
		cookTime[i]++;
	}

	private boolean hasReserveFuelStock() {
		return getItemFuelValue(slots[FUEL_PLACE]) > 0;
	}

	private boolean canSmelt(ItemStack item, int i) {
		// se o slot de ingrediente estiver vazio
		// ItemStack slotTargeted;
		if (item == null) {
			return false;
		} else {
			// obtem o produto do ingrediente no registro de recipes (caso
			// exista a recipe para o ingrediente)
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(
					item);

			// caso não tenha encontrado um resultado para o ingrediente
			if (itemStack == null)
				return false;

			// do contrário caso há espaço no slot de produto
			if (slots[RESULT[i]] == null)
				return true;

			// caso o slot de produto se encontra ocupado
			// e o tipo de item não se equivale com do ingrediente
			if (!slots[RESULT[i]].isItemEqual(itemStack))
				return false;

			// do contrário se analisa a quantidade em itemstack
			int result = slots[RESULT[i]].stackSize + itemStack.stackSize;
			return (result <= getInventoryStackLimit() && result <= itemStack
					.getMaxStackSize());
		}
	}

	private void placeStocked(int i) {
		ItemStack item = slots[INGREDIENT_STOCK[i]].copy();
		item.stackSize = 1;
		slots[INGREDIENT_PLACE[i]] = item;
		slots[INGREDIENT_STOCK[i]].stackSize--;
		if (slots[INGREDIENT_STOCK[i]].stackSize <= 0) {
			slots[INGREDIENT_STOCK[i]] = null;
		}
	}

	private boolean trySmelt(int i) {
		if (canSmelt(slots[INGREDIENT_PLACE[i]], i)) {
			return true;
		} else if (canSmelt(slots[INGREDIENT_STOCK[i]], i)) {
			placeStocked(i);
			return true;
		}
		return false;
	}

	private void makeSmelt(int i) {

		// obtem o resultado para o tipo de ingrediente
		ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(
				slots[INGREDIENT_PLACE[i]]);

		// caso o slot de produto se encontre vazio é feito uma cópia do
		// itemStack resultado
		if (slots[RESULT[i]] == null) {
			slots[RESULT[i]] = itemStack.copy();

			// do contrário o itemStack produto é incrementado com o
			// resultado
		} else if (slots[RESULT[i]].isItemEqual(itemStack)) {
			slots[RESULT[i]].stackSize += itemStack.stackSize;
		}

		// é deduzido uma unidade do ingrediente
		slots[INGREDIENT_PLACE[i]].stackSize--;

		// caso a diminuição fez com que a quantidada fosse a 0 , remove o
		// itemStack do slot ingrediente
		if (slots[INGREDIENT_PLACE[i]].stackSize <= 0) {
			slots[INGREDIENT_PLACE[i]] = null;
		}

		// reseta o tempo de cook do slot i
		cookTime[i] = 0;

		// if (!(burnTime > 1)) {
		// if (canPlaceStocked(i) && getItemBurnTime(slots[FUEL_PLACE]) > 0) {
		// placeStocked(i);
		// }
		// } else if (canPlaceStocked(i)) {
		// placeStocked(i);
		// }
	}

	private boolean hasFuel() {
		return fuel > 0;
	}
}
