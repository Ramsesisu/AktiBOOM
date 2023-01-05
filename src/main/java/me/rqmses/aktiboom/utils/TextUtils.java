package me.rqmses.aktiboom.utils;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class TextUtils {
    public static TextComponentString clickable(TextFormatting color, String text, String hover, ClickEvent.Action action, String value) {
        TextComponentString message = new TextComponentString(text);
        Style style = new Style().setColor(color);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(hover));
        ClickEvent clickEvent = new ClickEvent(action, value);
        message.setStyle(style.setHoverEvent(hoverEvent).setClickEvent(clickEvent));
        return message;
    }
}
