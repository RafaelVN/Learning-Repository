package teste;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;

public class customButton extends GuiButton {

	public customButton(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_,
			String p_i1020_4_) {
		super(p_i1020_1_, p_i1020_2_, p_i1020_3_, p_i1020_4_);
	}

	public customButton(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_,
			int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
		super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_,
				p_i1021_6_);
	}

	public void drawButton(Minecraft p_146112_1_, int p_146112_2_,
			int p_146112_3_) {
		if (this.visible) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0,
					this.width, this.height);
			// this.drawString(p_73731_1_, p_73731_2_, p_73731_3_, p_73731_4_,
			// p_73731_5_);
			// drawRect(xPosition, yPosition, width, height, 50);
			boolean b = this.field_146123_n = p_146112_2_ >= this.xPosition
					&& p_146112_3_ >= this.yPosition
					&& p_146112_2_ < this.xPosition + this.width
					&& p_146112_3_ < this.yPosition + this.height;
			//System.out.println(this.getHoverState(b));
		}
	}
	
	
	//public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_)
    //{
    //    if (super.mousePressed(p_146116_1_,p_146116_2_,p_146116_3_)){
    //    	System.out.println("true click");
    //    	return true;
    //    }
    //    System.out.println("false click");
    //     return false;
    //}
	

}
