package me.rqmses.aktimod.utils;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class TextUtils {
    public static TextComponentString clickable() {
        TextComponentString message = new TextComponentString("Test");
        Style style = new Style();
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Test2"));
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "Test3");
        message.setStyle(style.setHoverEvent(hoverEvent).setClickEvent(clickEvent));
        return message;
    }
}
