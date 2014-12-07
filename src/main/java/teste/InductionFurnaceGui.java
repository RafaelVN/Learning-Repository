package teste;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class InductionFurnaceGui extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation(
			testemod.MODID, "textures/gui/InductionFurnaceGui.png");

	public static final ResourceLocation addons = new ResourceLocation(
			testemod.MODID, "textures/gui/InductionFurnaceAddons.png");

	public InductionFurnaceTileEntity tileEntity;

	public InductionFurnaceGui(InventoryPlayer ivPlayer,
			InductionFurnaceTileEntity tileEntity) {
		super(new InductionFurnaceContainer(ivPlayer, tileEntity));
		this.tileEntity = tileEntity;

		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = tileEntity.isInvNameLocalized() ? tileEntity.getInvName()
				: I18n.format(tileEntity.getInvName(), new Object[0]);

		String inventoryName = I18n
				.format("container.inventory", new Object[0]);

		fontRendererObj.drawString(name,
				xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 13,
				4210752);

		fontRendererObj.drawString(inventoryName, xSize / 2
				- this.fontRendererObj.getStringWidth(inventoryName) / 2,
				163 - 8, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {

		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		Minecraft.getMinecraft().getTextureManager().bindTexture(addons);
		if (tileEntity.isBurning()) {
			// if (tileEntity.isSmelting)
			drawTexturedModalRect(guiLeft + 120, guiTop + 25, 0, 0, 16, 88);
		}
		int fuelScale = tileEntity.getFuelRemainingScaled(29);
		drawTexturedModalRect(guiLeft + 114, guiTop + 119, 16, 0, fuelScale + 1, 10);
		System.out.println(tileEntity.getFuelRemainingScaled(29));
		
		for (int i = 0; i < 5; i++) {
			int cookTimeScaled = tileEntity.getCookProgressScaled(10, i);
			drawTexturedModalRect(guiLeft + 137, guiTop + 38 + (18 * i)
					- (cookTimeScaled + 1), 16, 20 - (cookTimeScaled + 1), 1, cookTimeScaled + 1);
			// System.out.println(tileEntity.getCookProgressScaled(10, i));
		}
	}

	public void initGui() {
		super.initGui();
		this.buttonList.add(new customButton(0, guiLeft + 15, guiTop + 15, 16,
				16, "no use"));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			System.out.println("clique detect");
			tileEntity.sendPowerSwitchMsg();
		}
	}

}