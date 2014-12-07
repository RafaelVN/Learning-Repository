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

	public int lastPowerFlag, lastIsBurning;

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
	// chamada quando o bloco ativado pelo client

	// ids :
	// 0-4 cookTime[]
	// 5 fuel;
	// 6 currentItemfuel
	// 7 powerFlag
	// 8 burning state
	@Override
	public void addCraftingToCrafters(ICrafting iCraftting) {
		super.addCraftingToCrafters(iCraftting);
		for (int i = 0; i < 5; i++) {
			iCraftting
					.sendProgressBarUpdate(this, i, tileEntity.getCookTime(i));
		}
		iCraftting.sendProgressBarUpdate(this, 5, tileEntity.getFuelValue());
		iCraftting.sendProgressBarUpdate(this, 6,
				tileEntity.getCurrentItemFuel());
		iCraftting.sendProgressBarUpdate(this, 7, tileEntity.isPowerOn() ? 1
				: 0);
		iCraftting.sendProgressBarUpdate(this, 8, tileEntity.isBurning() ? 1
				: 0);
	}

	@Override
	// quanda a gui ativada, constantemente detecta mudança e caso haja alguma,
	// chama update no client
	public void detectAndSendChanges() {

		super.detectAndSendChanges();
		ICrafting iCraftting;

		// eu acho que se refere a cada player que tem ativo o bloco
		for (int ic = 0; ic < crafters.size(); ic++) {
			iCraftting = (ICrafting) crafters.get(ic);

			for (int i = 0; i < 5; i++) {
				if (lastCookTime[i] != tileEntity.getCookTime(i)) {
					iCraftting.sendProgressBarUpdate(this, i,
							tileEntity.getCookTime(i));
				}
			}
			if (lastBurnTime != tileEntity.getFuelValue())
				iCraftting.sendProgressBarUpdate(this, 5,
						tileEntity.getFuelValue());

			if (lastItemBurnTime != tileEntity.getCurrentItemFuel())
				iCraftting.sendProgressBarUpdate(this, 6,
						tileEntity.getCurrentItemFuel());

			boolean b = lastPowerFlag == 1 ? true : false;
			if (b != tileEntity.isPowerOn())
				iCraftting.sendProgressBarUpdate(this, 7,
						tileEntity.isPowerOn() ? 1 : 0);

			b = lastIsBurning == 1 ? true : false;
			if (b != tileEntity.isBurning())
				iCraftting.sendProgressBarUpdate(this, 8,
						tileEntity.isBurning() ? 1 : 0);
		}

		lastCookTime = tileEntity.getAllCookTimes();
		lastBurnTime = tileEntity.getFuelValue();
		lastItemBurnTime = tileEntity.getCurrentItemFuel();
		lastPowerFlag = tileEntity.isPowerOn() ? 1 : 0;
		lastIsBurning = tileEntity.isBurning() ? 1 : 0;

	}

	// no client e recebidos os valores atualizados do tileentity do server
	@Override
	public void updateProgressBar(int varId, int newValue) {
		if (varId >= 0 && varId <= 4) {
			tileEntity.setCookTime(newValue, varId);
			System.out.println("cooktime change");
		}
		if (varId == 5) {
			tileEntity.setFuelValue(newValue);
			System.out.println("fuel change");
		}
		if (varId == 6) {
			tileEntity.setCurrentItemFuel(newValue);
			System.out.println("currentItemFuel change");
		}
		if (varId == 7) {
			tileEntity.setPower(newValue == 1 ? true : false);
			System.out.println("powerFlag change :" + tileEntity.isPowerOn());
		}
		if (varId == 8) {
			tileEntity.setBurning(newValue == 1 ? true : false);
			System.out.println("burning state change");
		}

		// System.out.println(tileEntity.powerFlag);
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
