package de.uniks.beastopia.teaml.utils;

public class FormatString {
    public static String formatString(String stringToFormat, int length) {
        if (stringToFormat.length() <= length) {
            return stringToFormat;
        }

        int lastSpace = stringToFormat.lastIndexOf(' ', length);
        if (lastSpace != -1) {
            return stringToFormat.substring(0, lastSpace) + "\n" + formatString(stringToFormat.substring(lastSpace + 1), length);
        } else {
            return stringToFormat.substring(0, length) + "-\n" + formatString(stringToFormat.substring(length), length);
        }
    }
}
