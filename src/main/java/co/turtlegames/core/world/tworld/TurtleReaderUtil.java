package co.turtlegames.core.world.tworld;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.NibbleArray;

public class TurtleReaderUtil {

    public static class TurtleData {

        private byte[] _blocks;
        private NibbleArray _data;

        public TurtleData(byte[] blocks, NibbleArray data) {
            _blocks = blocks;
            _data = data;
        }

        public byte[] getBlocks() {
            return _blocks;
        }

        public NibbleArray getData() {
            return _data;
        }

    }

    public static TurtleData decodeInternalIdData(char[] idArray) {

        byte[] blockData = new byte[idArray.length];
        NibbleArray data = new NibbleArray();

        for(int k = 0; k < idArray.length; k++) {

            char value = idArray[k];

            int l = k & 0xF;
            int i1 = k >> 8 & 0xF;
            int j1 = k >> 4 & 0xF;

            if(value >> 12 != 0) {
                System.out.println("Something is supposed to happen");
            }

            blockData[k] = (byte) ( value >> 4 & 0xFF);
            data.a(l, i1, j1, value & 0xF);

        }

        return new TurtleData(blockData, data);

    }

    public static char[] encodeInternalIdData(TurtleData data) {

        byte[] blocks = data.getBlocks();
        NibbleArray bData = data.getData();

        char[] encoded = new char[blocks.length];

        for(int l = 0; l < encoded.length; l++) {

            int i1 = l & 0xF;
            int j1 = l >> 8 & 0xF;
            int k1 = l >> 4 & 0xF;

            int l1 = 0;
            int ex = l1;

            int id = blocks[l] & 0xFF;
            int iData = bData.a(i1, j1, k1);

            int packed = ex << 12 | id << 4 | iData;

            if(Block.d.a(packed) == null) {

                Block block = Block.getById(ex << 8 | id);

                try {
                    iData = block.toLegacyData(block.fromLegacyData(iData));
                } catch(Exception e) {
                    iData = block.toLegacyData(block.getBlockData());
                }

                packed = ex << 12 | id << 4 | iData;

            }

            encoded[l] = (char) packed;

        }

        return encoded;

    }

}
