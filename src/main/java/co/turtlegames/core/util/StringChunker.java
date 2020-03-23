package co.turtlegames.core.util;

import org.bukkit.ChatColor;

import java.util.Arrays;

public class StringChunker {

    public static String[] chunk(String str, int chunkSize) {

        int numberOfChunks = (int) Math.ceil((str.length() * 1.0f)/chunkSize);
        String[] chunks = new String[numberOfChunks];

        String lastColours = "";
        for(int i = 0; i < numberOfChunks; i++) {

            int maxIndex = Math.min(str.length(), chunkSize * (i + 1));
            String chunk = lastColours + str.substring(i * chunkSize, maxIndex);

            if(chunk.length() > chunkSize)
                chunk = chunk.substring(0, chunkSize);

            lastColours = ChatColor.getLastColors(chunk);

            chunks[i] = chunk;

        }

        return chunks;

    }

}
