package teste;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = testemod.MODID, name = testemod.NAME, version = testemod.VERSION)
public class testemod {
	public static final String MODID = "testemod";
	public static final String NAME = "Testemod";
	public static final String VERSION = "1.0";

	public static Block inductionFurnace;
	public static Block inductionFurnaceAtive;

	@Instance("testemod")
	public static testemod instance;

	//@SidedProxy(clientSide = "teste.ClientProxy", serverSide = "teste.CommonProxy")
	//public static CommonProxy proxy;

	public static final int guiIdInductionFurnace = 0;

	@EventHandler
	public void init(FMLInitializationEvent event) {

		inductionFurnace = new InductionFurnaceBlock(Material.rock, false)
				.setHardness(0.5F).setStepSound(Block.soundTypeStone)
				.setBlockName("inductionFurnace")
				.setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerBlock(inductionFurnace, "InductionFurnace");

		inductionFurnaceAtive = new InductionFurnaceBlock(Material.rock, true)
				.setHardness(0.5F).setStepSound(Block.soundTypeStone)
				.setBlockName("inductionFurnace")
				.setCreativeTab(CreativeTabs.tabBlock).setLightLevel(0.875F);
		GameRegistry.registerBlock(inductionFurnaceAtive,
				"InductionFurnaceAtive");

		GameRegistry.registerTileEntity(InductionFurnaceTileEntity.class,
				"InductionFurnace");

		// GameRegistry.registerTileEntity(InductionFurnaceTileEntity.class,
		// "InductionFurnace");

		NetworkRegistry.INSTANCE.registerGuiHandler(testemod.instance,
				new InductionFurnaceGuiHandler());
		// TileEntity.addMapping(p_145826_0_, p_145826_1_);

		// Deprecated for removal in 1.8
		// LanguageRegistry.instance().addStringLocalization("container.inductionFurnace","Induction Furnace");
	}
}
