package com.pixelmkmenu.pixelmkmenu;

public enum ObfuscationMapping {
	minecraftTimer("timer", "Q", "field_71428_T"),
	debugFPS("debugFPS", "ac", "field_71470_ab"),
	shapedRecipeWidth("recipeWidth", "a", "field_77576_b"),
	shapedRecipeHeight("recipeHeight", "b", "field_77577_c"),
	shapedRecipeItems("recipeItems", "c", "field_77574_d"),
	shapelessRecipeItems("recipeItems", "b", "field_77579_b"),
	textFieldXPos("xPosition", "f", "field_146209_f"),
	textFieldYPos("yPosition", "g", "field_146210_g"),
	textFieldWidth("width", "h", "field_146218_h"),
	textFieldHeight("height", "i", "field_146219_i"),
	textFieldIsEnabled("isEnabled", "p", "field_146226_p"),
	textFieldScrollPos("lineScrollOffset", "q", "field_146225_q"),
	textFieldSelectionStart("cursorPosition", "r", "field_146224_r"),
	textFieldSelectionEnd("selectionEnd", "s", "field_146223_s"),
	textFieldEnabledColour("enabledColor", "t", "field_146222_t"),
	textFieldDisabledColour("disabledColor", "u", "field_146221_u"),
	creativeBinSlot(null, "C", "field_147064_C"),
	creativeGuiScroll("currentScroll", "x", "field_147067_x"),
	serverEntityTracker("theEntityTracker", "K", "field_73062_L"),
	itemsList("itemList", "a", "field_148330_a"),
	damagedBlocks("damagedBlocks", "O", "field_72738_E"),
	currentLocale("i18nLocale", "a", "field_135054_a"),
	translateTable(null, "a", "field_135032_a"),
	downloadedImage("bufferedImage", "h", "field_110560_d"),
	renderZoom("cameraZoom", "af", "field_78503_V"),
	renderOfsetX("cameraYaw", "ag", "field_78502_W"),
	renderOfsetY("cameraPitch", "ah", "field_78509_X"),
	getSlotAtPosition("getSlotAtPosition", "c", "func_146975_c"),
	handleMouseClick("handleMouseClick", "a", "func_146984_a"),
	selectTab("setCurrentCreativeTab", "b", "func_147050_b"),
	scrollTo("scrollTo", "a", "func_148329_a"),
	renderSkyBox("renderSkybox", "c", "func_73971_c"),
	resize("resize", "a", "func_71370_a"),
	guiScreenMouseClicked("mouseClicked", "a", "func_73864_a"),
	guiScreenMouseMovedOrUp("mouseReleased", "b", "func_146286_b"),
	guiScreenKeyTyped("keyTyped", "a", "func_73869_a"),
	guiScreenSelectedButton("selectedButton", "a", "field_146290_a"),
	worldRenderers("worldRenderers", "v", "field_72765_l"),
	worldType("terrainType", "b", "field_76098_b"),
	fontRendererPosY("posY", "j", "field_78296_k"),
	worldSelected(null, "i", "field_146634_i"),
	guiSelectWorldParent("parentScreen", "a", "field_146632_a"),
	modelBipedMain("modelBipedMain", "f", "field_77109_a"),
	modelArmorChestplate("modelArmorChestplate", "g", "field_77108_b"),
	modelArmor("modelArmor", "h", "field_77111_i"),
	spawnerLogic(null, "a", "field_145882_a"),
	soundSystemThread("sndSystem", "e", "field_148620_e"),
	lastClicked("lastClicked", "q", "field_148167_s"),
	skinTexture("downloadImageSkin", "a", "field_110316_a"),
	cloakTexture("downloadImageCape", "c", "field_110315_c"),
	skinResource("locationSkin", "d", "field_110312_d"),
	cloakResource("locationCape", "e", "field_110313_e"),
	imageUrl("imageUrl", "f", "field_110562_b"),
	imageThread("imageThread", "i", "field_110561_e"),
	imageBuffer("imageBuffer", "g", "field_110563_c"),
	imageFile(null, "e", "field_152434_e"),
	resourceToTextureMap("mapTextureObjects", "b", "field_110585_a"),
	optionsBackground("optionsBackground", "b", "field_110325_k"),
	panoramaTexture(null, "L", "field_110351_G"),
	lastPosZ("lastPosZ", "o", "field_147373_o"),
	lastPosX("lastPosX", "p", "field_147382_p"),
	lastPosY("lastPosY", "q", "field_147381_q"),
	hasMoved("hasMoved", "r", "field_147380_r"),
	rainingStrength("rainingStrength", "n", "field_73004_o"),
	thunderingStrength("thunderingStrength", "p", "field_73017_q"),
	standing_sign("standing_sign", "an", "field_150472_an"),
	wall_sign("wall_sign", "as", "field_150444_as"),
	mob_spawner("mob_spawner", "ac", "field_150474_ac"),
	internetServerList(null, "i", "field_146804_i"),
	serverSelectionList(null, "h", "field_146803_h"),
	guiResourcePacksParentScreen(null, "f", "field_146965_f"),
	abstractResourcePackFile("resourcePackFile", "a", "field_110597_b"),
	mcFramebuffer("framebufferMc", "au", "field_147124_at"),
	eventSounds("soundPool", "a", "field_148736_a"),
	worldInfo("worldInfo", "x", "field_72986_A"),
	SlotCreativeInventory(null, "bfn", "net.minecraft.client.gui.inventory.GuiContainerCreative$CreativeSlot"),
	ContainerCreative(null, "bfm", "net.minecraft.client.gui.inventory.GuiContainerCreative$ContainerCreative"),
	CallableMinecraftVersion(null, "c", "net.minecraft.crash.CrashReport$1");
	
	public final String mcpName;
	public final String obfuscatedName;
	public final String seargeName;
	
	ObfuscationMapping(String mcpName, String obfuscatedName, String seargeName){
		this.mcpName = (mcpName != null) ? mcpName : seargeName;
		this.obfuscatedName = obfuscatedName;
		this.seargeName = seargeName;
	}
	
	public String getName() {
		return ModUtil.getObfuscatedFieldName(this.mcpName, this.obfuscatedName, this.seargeName);
	}
}
