package teste;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class InductionFurnaceContainer extends Container {

	private InductionFurnaceTileEntity tileEntity;

	// how long this furnace will continue to burn for (fuel)
	public int lastBurnTime;

	// the start time for this fuel
	public int lastItemBurnTime;

	// how long time left before cooked
	public int[] lastCookTime = new int[5];

	public InductionFurnaceContainer(InventoryPlayer ivPlayer,
			InductionFurnaceTileEntity tileEntity) {

		// registra o tile entity e cria os slots

		this.tileEntity = tileEntity;

		// cria os slots em relação do numero do slot com a sua posição na gui

		// 0-4 - stocks
		// 5-9 - place
		// 11-4 - result
		for (int i = 0; i < 5; i++) {
			addSlotToContainer(new Slot(tileEntity,
					InductionFurnaceTileEntity.INGREDIENT_STOCK[i], 42,
					25 + (18 * i)));
			addSlotToContainer(new InductionFurnaceLockedSlot(tileEntity,
					InductionFurnaceTileEntity.INGREDIENT_PLACE[i], 120,
					25 + (18 * i)));
			addSlotToContainer(new SlotFurnace(ivPlayer.player, tileEntity,
					InductionFurnaceTileEntity.RESULT[i], 198, 25 + (18 * i)));
		}

		// 15 - fuel
		addSlotToContainer(new Slot(tileEntity,
				InductionFurnaceTileEntity.FUEL_PLACE, 120, 135));

		// 16-43 Player inventory back
		// 44-52 Player inventory hand
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(ivPlayer, j + (i * 9) + 9,
						48 + (18 * j), 175 + (18 * i)));
			}
		}
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(ivPlayer, i, 48 + (18 * i), 233));
		}
	}

	// registra valores na tileentity no server para ser enviado para os client
	// que possui ativado tileentity especificado

	// ids :
	// 0-4 cookTime[]
	// 5 burnTime;
	// 6 currentItemBurnTime
	@Override
	public void addCraftingToCrafters(ICrafting iCraftting) {
		super.addCraftingToCrafters(iCraftting);
		for (int i = 0; i < 5; i++) {
			iCraftting.sendProgressBarUpdate(this, i, tileEntity.cookTime[i]);
		}
		iCraftting.sendProgressBarUpdate(this, 5, tileEntity.burnTime);
		iCraftting.sendProgressBarUpdate(this, 6,
				tileEntity.currentItemBurnTime);
	}

	@Override
	// quanda a gui ativada, constantemente detecta mudança e caso haja alguma,
	// chama update no client
	public void detectAndSendChanges() {

		super.detectAndSendChanges();

		// eu acho que se refere a cada player que tem ativo o bloco
		for (int ic = 0; ic < crafters.size(); ic++) {
			ICrafting iCraftting = (ICrafting) crafters.get(ic);

			for (int i = 0; i < 5; i++) {
				if (lastCookTime[i] != tileEntity.cookTime[i]) {
					iCraftting.sendProgressBarUpdate(this, i,
							tileEntity.cookTime[i]);
				}
			}
			if (lastBurnTime != tileEntity.burnTime) {
				iCraftting.sendProgressBarUpdate(this, 5, tileEntity.burnTime);
			}
			if (lastItemBurnTime != tileEntity.currentItemBurnTime) {
				iCraftting.sendProgressBarUpdate(this, 6,
						tileEntity.currentItemBurnTime);
			}
		}

		lastCookTime = tileEntity.cookTime.clone();
		lastBurnTime = tileEntity.burnTime;
		lastItemBurnTime = tileEntity.currentItemBurnTime;
	}

	// no client e recebidos os valores atualizados do tileentity do server
	@Override
	public void updateProgressBar(int varId, int newValue) {
		if (varId >= 0 && varId <= 4) {
			tileEntity.cookTime[varId] = newValue;
		}
		if (varId == 5)
			tileEntity.burnTime = newValue;
		if (varId == 6)
			tileEntity.currentItemBurnTime = newValue;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return null;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

}
