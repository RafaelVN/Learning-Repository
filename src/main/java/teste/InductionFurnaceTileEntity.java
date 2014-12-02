package teste;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InductionFurnaceTileEntity extends TileEntity implements
		ISidedInventory {

	private String localizedName;

	private static final int[] slots_top = new int[] { 0 };
	private static final int[] slots_bottom = new int[] { 2, 1 };
	private static final int[] slots_sides = new int[] { 1 };

	private ItemStack[] slots = new ItemStack[3];

	// furnace burning speed
	public int furnaceSpeed = 200;

	// how long this furnace will continue to burn for (fuel)
	public int burnTime;

	// the start time for this fuel
	public int currentItemBurnTime;

	// how long time left before cooked
	public int cookTime;

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
	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	// diminui a quantidade de unidades no slot e retorna a quantidade removida
	// ou a quantidade antiga do slot(caso o resto da subitra��o chegue a 0 ou
	// menor)
	@Override
	public ItemStack decrStackSize(int slot, int qtd) {
		if (slots[slot] != null) {
			ItemStack itemStack;

			// se a quantidade atual do slot for menor ou igual ao do parametro
			if (slots[slot].stackSize <= qtd) {
				itemStack = slots[slot];
				slots[slot] = null;
				return itemStack;
				// do contr�rio
			} else {
				// diminiu qtd no slot
				itemStack = slots[slot].splitStack(qtd);
				// eu tenho uma pequena impress�o de que esta rotina jamais ser�
				// chamada mais deixa para l�
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

		System.out
				.println("Read !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Items", 10);
		slots = new ItemStack[getSizeInventory()];

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound compound = (NBTTagCompound) list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");

			if (b >= 0 && b < slots.length) {
				slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		burnTime = nbt.getShort("BurnTime");
		cookTime = nbt.getShort("CookTime");
		currentItemBurnTime = getItemBurnTime(slots[1]);

		if (nbt.hasKey("CustomName")) {
			localizedName = nbt.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		System.out
				.println("Write !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		super.writeToNBT(nbt);
		nbt.setShort("BurnTime", (short) burnTime);
		nbt.setShort("CookTime", (short) cookTime);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < slots.length; ++i) {
			if (slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
				slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}
		nbt.setTag("Items", list);

		if (isInvNameLocalized()) {
			nbt.setString("CustomName", localizedName);
		}
	}

	// determina se pode ser usado pelo jogador nas circunst�ncias atual
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// considerar o bloco ativo no mundo , a rotina abaixo sempre ira
		// retorna true contudo caso o bloco n�o se
		// encontrar no mundo ent�o ira retorna falso;
		// ex: se ele estiver vinculado a um item e n�o a um bloco no mundo

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

	public boolean isBurning() {
		return burnTime > 0;
	}

	@Override
	public void updateEntity() {

		boolean flag1 = burnTime > 0;
		boolean flag2 = false;

		if (isBurning()) {
			burnTime--;
		}

		// somente o serve processa , ele envia os dados para o cliente via
		// Container.addSlotToContainer quando interagido por um client player
		if (!worldObj.isRemote) {
			if (burnTime == 0 && canSmelt()) {
				currentItemBurnTime = burnTime = getItemBurnTime(slots[1]);

				if (isBurning()) {
					flag2 = true;

					if (slots[1] != null) {
						slots[1].stackSize--;

						if (slots[1].stackSize == 0) {
							slots[1] = slots[1].getItem().getContainerItem(
									slots[1]);
						}
					}
				}
			}

			if (isBurning() && canSmelt()) {
				cookTime++;

				if (cookTime == furnaceSpeed) {
					cookTime = 0;
					smeltItem();
					flag1 = true;
				}
			} else {
				cookTime = 0;
			}

			if (flag1 != isBurning()) {
				flag2 = true;
				InductionFurnaceBlock.updateFurnaceBlockState(burnTime > 0,
						worldObj, xCoord, yCoord, zCoord);
			}
		}
		if (flag2) {
			markDirty();
		}
	}

	// testa se existe condi��o de efetuar o smelt
	private boolean canSmelt() {
		// se o slot de ingrediente estiver vazio
		if (slots[0] == null) {
			return false;
		} else {
			// obtem o produto do ingrediente no registro de recipes (caso
			// exista a recipe para o ingrediente)
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(
					slots[0]);

			// caso n�o tenha encontrado um resultado para o ingrediente
			if (itemStack == null)
				return false;

			// do contr�rio caso h� espa�o no slot de produto
			if (slots[2] == null)
				return true;

			// caso o slot de produto se encontra ocupado
			// e o tipo de item n�o se equivale com do ingrediente
			if (!slots[2].isItemEqual(itemStack))
				return false;

			// do contr�rio se analisa a quantidade em itemstack
			int result = slots[2].stackSize + itemStack.stackSize;
			return (result <= getInventoryStackLimit() && result <= itemStack
					.getMaxStackSize());
		}
	}

	// efeuta o smelt
	public void smeltItem() {
		if (canSmelt()) {

			// obtem o resultado para o tipo de ingrediente
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(
					slots[0]);

			// caso o slot de produto se encontre vazio � feito uma c�pia do
			// itemStack resultado
			if (slots[2] == null) {
				this.slots[2] = itemStack.copy();

				// do contr�rio o itemStack produto � incrementado com o
				// resultado
			} else if (this.slots[2].isItemEqual(itemStack)) {
				slots[2].stackSize += itemStack.stackSize;
			}

			// � deduzido uma unidade do ingrediente
			slots[0].stackSize--;

			// caso a diminui��o fez com que a quantidada fosse a 0 , remove o
			// itemStack do slot ingrediente
			if (slots[0].stackSize <= 0) {
				slots[0] = null;
			}
		}
	}

	// obtem o tempo de combust�o do item caso exista
	public static int getItemBurnTime(ItemStack itemStack) {
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

	// analisa se um item tem caracter�stica de combust�o
	public static boolean isItemFuel(ItemStack itemStack) {
		return getItemBurnTime(itemStack) > 0;
	}

	// testa a validade de um item para determinado slot
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : (slot == 1 ? isItemFuel(itemStack) : true);
	}

	// determina a acessibilidade dos slots com rela��o aos lados do bloco
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slots_bottom : (side == 1 ? slots_top : slots_sides);
	}

	// analisa se o item pode ser inserido em um slot por meio de um dos lados
	// do bloco
	// para sistemas de automatiza��o
	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	// analisa se o item pode ser extraido em um slot por meio de um dos lados
	// do bloco
	// para sistemas de automatiza��o
	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return side != 0 || slot != 1 || itemStack.getItem() == Items.bucket;
	}

	public int getBurnTimeRemainingScaled(int i) {
		if (currentItemBurnTime == 0) {
			currentItemBurnTime = furnaceSpeed;
		}
		return burnTime * i / currentItemBurnTime;
	}

	public int getCookProgressScaled(int i) {
		return cookTime * i / furnaceSpeed;
	}

}
