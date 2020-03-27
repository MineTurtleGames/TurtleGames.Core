package co.turtlegames.core.tworld.io;

import com.github.luben.zstd.Zstd;
import net.minecraft.server.v1_8_R3.NibbleArray;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.BitSet;

public class TurtleInputStream extends DataInputStream  {

    public TurtleInputStream(InputStream stream) throws IOException {
       super(stream);
    }

    public BitSet readBitSet(int length) throws IOException {
        return BitSet.valueOf(this.readByteArray(length));
    }

    public byte[] readByteArray(int length) throws IOException {

        byte[] foundArray = new byte[length];
        read(foundArray);

        return foundArray;

    }

    public int[] readIntArray(int length) throws IOException {

        int[] array = new int[length];

        for(int i = 0; i < length; i++)
            array[i] = this.readInt();

        return array;

    }

    public byte[] readCompressedData() throws IOException {

        int compLength = this.readInt();
        int uncompLength = this.readInt();

        byte[] compressedData = this.readByteArray(compLength);
        byte[] uncompressedData = Zstd.decompress(compressedData, uncompLength);

        if(uncompressedData.length != uncompLength)
            throw new IllegalArgumentException("Malformed compressed data");

        return uncompressedData;

    }

    public NibbleArray readNibbleArray(int length) throws IOException {

        NibbleArray nibbleArray = new NibbleArray(new byte[length]);
        this.read(nibbleArray.a());

        return nibbleArray;

    }

}
