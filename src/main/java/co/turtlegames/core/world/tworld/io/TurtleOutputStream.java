package co.turtlegames.core.world.tworld.io;

import co.turtlegames.core.world.tworld.util.IODataUtil;
import com.github.luben.zstd.Zstd;
import net.minecraft.server.v1_8_R3.NibbleArray;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.BitSet;

public class TurtleOutputStream extends DataOutputStream {

    public TurtleOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    public void writeIntArray(int[] array) throws IOException {

        for(int i : array)
            this.writeInt(i);

    }

    public void writeByteArray(byte[] array) throws IOException {

        for(byte byt : array)
            this.writeByte(byt);

    }

    public void writeBitSet(BitSet bitSet, int length) throws IOException {

        this.writeByteArray(IODataUtil.toByteArray(bitSet, length));

    }

    public void compressAndWrite(byte[] toByteArray) throws IOException {

        int uncompressedSize = toByteArray.length;

        byte[] compressedData = Zstd.compress(toByteArray);
        int compressedSize = compressedData.length;

        this.writeInt(compressedSize);
        this.writeInt(uncompressedSize);

        this.writeByteArray(compressedData);

    }

    public void writeNibbleArray(NibbleArray blockLight) throws IOException {
        this.writeByteArray(blockLight.a());
    }

}
