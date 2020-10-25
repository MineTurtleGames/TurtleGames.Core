package co.turtlegames.core.util;

import org.bukkit.ChatColor;

import java.util.Arrays;

public class StringChunker {

    public static String[] chunk(String str, int chunkSize) {

        if(str.length() == 0)
            return new String[] { "" };

        String[] split = new String[(str.length() / chunkSize) + 1];

        String toAppend = "";
        int cursor = 0;
        int i = 0;

        while(cursor < str.length()) {

            int endIndex = Math.min(cursor + (chunkSize - toAppend.length()), str.length());
            String chunk = toAppend + str.substring(cursor, endIndex);

            toAppend = ChatColor.getLastColors(chunk);

            if(chunk.endsWith(ChatColor.COLOR_CHAR + "")) {

                toAppend += ChatColor.COLOR_CHAR;
                chunk = chunk.substring(0, chunk.length() - 1);

                cursor++;

            }

            split[i] = chunk;
            i++;

            cursor += chunk.length();

        }

        return split;

    }

}
