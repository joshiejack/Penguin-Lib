package uk.joshiejack.penguinlib.util.helpers;

import com.google.common.collect.Lists;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

public class StringHelper {
    public static String[] getListFromDatabase(String data) {
        return data.replace("\"", "").replace(", ", ",").split(",");
    }

    public static TranslationTextComponent localize(String text) {
        return new TranslationTextComponent(text);
    }

    public static TranslationTextComponent format(String text, Object... objects) {
        return new TranslationTextComponent(text, objects);
    }

    public static boolean isEarlierThan(String name, char c) {
        return Character.toLowerCase(name.charAt(0)) < c;
    }

    public static String convertNumberToString(long number) {
        return convertNumberToString(number, false);
    }

    public static String convertNumberToString(long number, boolean shortform) {
        if (number < 0) number = -number;
        if (number < 1000) return "" + number;
        long remainder = number % 1000;
        int decimal = remainder == 0 ? 0 : shortform || remainder % 100 == 0 ? 1 : remainder % 10 == 0 ? 2 : 3;
        int exp = (int) (Math.log(number) / Math.log(1000));
        return String.format("%." + decimal + "f%c", number / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
    }

    private static boolean unicode;

    public static void enableUnicode() {
        Minecraft mc = Minecraft.getInstance();
        unicode = mc.options.forceUnicodeFont;
        AbstractOption.FORCE_UNICODE_FONT.set(mc.options, "true");
    }

    public static void disableUnicode() {
        AbstractOption.FORCE_UNICODE_FONT.set(Minecraft.getInstance().options, String.valueOf(unicode));
    }

    public static String[] decompose(String namespace, char character) {
        String[] astring = new String[]{"minecraft", namespace};
        int i = namespace.indexOf(character);
        if (i >= 0) {
            astring[1] = namespace.substring(i + 1);
            if (i >= 1) {
                astring[0] = namespace.substring(0, i);
            }
        }

        return astring;
    }

    public static String join(char character, Object... vars) {
        return StringUtils.join(Lists.newArrayList(vars).stream().map(Object::toString).collect(Collectors.toList()), character);
    }

    public static ITextComponent withClickableCommand(String command, String tooltip, Object... formatting) {
        return withClickableCommand(TextFormatting.AQUA, command, tooltip, formatting);
    }

    public static ITextComponent withClickableCommand(TextFormatting color, String command, String tooltip, Object... formatting) {
        return new TranslationTextComponent(command, formatting).withStyle((p_241055_1_) -> p_241055_1_.withColor(color)
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command + join(' ', formatting)))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent(tooltip))));
    }
}
