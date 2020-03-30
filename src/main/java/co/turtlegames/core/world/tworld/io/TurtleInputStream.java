package co.turtlegames.core.world.tworld.io;

import com.github.luben.zstd.Zstd;
import net.minecraft.server.v1_8_R3.NibbleArray;

import java.io.*;
import java.util.Arrays;
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

        for(int i = 0; i < length; i++)
            foundArray[i] = this.readByte();

        return foundArray;

    }

    public String readChars(int length) throws IOException {

        char[] chars = new char[length];

        for(int i = 0; i < length; i++)
            chars[i] = this.readChar();

        return new String(chars);

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

        System.out.println("Comp size in: " + compLength);
        System.out.println("Uncomp size in: " + uncompLength);


        byte[] compressedData = this.readByteArray(compLength);
        System.out.println(Arrays.toString(compressedData));

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
