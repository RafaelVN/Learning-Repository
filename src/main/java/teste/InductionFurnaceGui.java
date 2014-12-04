package teste;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
//import net.minecraft.client.gui.g;
import net.minecraft.util.ResourceLocation;

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

		// Minecraft.getMinecraft().getTextureManager().deleteTexture(texture);
		Minecraft.getMinecraft().getTextureManager().bindTexture(addons);
		if (tileEntity.isBurning()) {
			// System.out.println("BURNING!!!");
			// System.out.println(tileEntity.burnTime);
			drawTexturedModalRect(guiLeft + 120, guiTop + 25, 0, 0, 16, 88);
			int k = this.tileEntity.getBurnTimeRemainingScaled(29);
			drawTexturedModalRect(guiLeft + 114, guiTop + 119, 16, 0, k + 1, 10);
		}
		for (int i = 0; i < 5; i++) {
			int k = tileEntity.getCookProgressScaled(10, i);
			drawTexturedModalRect(guiLeft + 137, guiTop + 38 + (18 * i)
					- (k + 1), 16, 20 - (k + 1), 1, k + 1);
		}
		// Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}