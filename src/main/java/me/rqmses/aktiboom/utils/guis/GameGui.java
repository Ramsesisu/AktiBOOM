package me.rqmses.aktiboom.utils.guis;

import me.rqmses.aktiboom.utils.GameUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;

public class GameGui extends GuiContainer {

    public GameGui(Container container) {
        super(container);
        this.xSize = 176;
        this.ySize = 250;
        this.mouseHandled = false;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = GameUtils.category.toUpperCase();
        this.fontRenderer.drawString(s, 88 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.getSlotUnderMouse() != null) {
            if (this.getSlotUnderMouse().getStack().getItem() != Item.getItemById(160)) {
                this.renderHoveredToolTip(mouseX, mouseY);
            }
        }
    }
}