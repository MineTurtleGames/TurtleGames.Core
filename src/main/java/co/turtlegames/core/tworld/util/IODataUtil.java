package co.turtlegames.core.tworld.util;

import net.minecraft.server.v1_8_R3.NibbleArray;

import java.util.BitSet;

public class IODataUtil {

    public static void copyAll(NibbleArray from, NibbleArray to) {

        byte[] bFrom = from.a();
        byte[] bTo = to.a();

        for(int i = 0; i < bFrom.length; i++)
            bTo[i] = bFrom[i];

    }

    public static byte[] toByteArray(BitSet set, int length) {

        byte[] data = new byte[length];

        for(int byteN = 0; byteN < length; byteN ++) {

            byte value = 0x00;

            for(int i = 0; i < 8; i++) {

                boolean boolValue = set.get(byteN * 8 + i);

                if(boolValue)
                    value |= 0x1 << i;

            }

            data[byteN] = value;

        }

        return data;

    }

}
