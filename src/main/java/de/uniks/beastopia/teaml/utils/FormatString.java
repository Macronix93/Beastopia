package de.uniks.beastopia.teaml.utils;

public class FormatString {

    public static String formatString(String itemName, int length) {
        if (itemName.length() > length) {
            int lastSpace = itemName.lastIndexOf(' ', length);
            if (lastSpace != -1) { // \n after last space
                if (itemName.length() > length * 2) {
                    String lastPart = itemName.substring(lastSpace + length);
                    return itemName.substring(0, lastSpace) + "\n" + formatString(lastPart, length);
                }
                return itemName.substring(0, lastSpace) + "\n" + formatString(itemName.substring(lastSpace + 1), length);
            } else { // if too long and no space
                if (itemName.length() > length * 2) {
                    String lastPart = itemName.substring(length);
                    return itemName.substring(0, 12) + "-\n" + formatString(lastPart, length);
                }
                return itemName.substring(0, 12) + "-\n" + itemName.substring(length);
            }
        }
        return itemName;
    }
}
