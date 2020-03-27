package co.turtlegames.core.tworld;

import co.turtlegames.core.tworld.io.TurtleInputStream;
import co.turtlegames.core.tworld.io.TurtleOutputStream;
import org.bukkit.util.Vector;

import java.io.IOException;

public class TurtleWorldMetadata {

    public enum MetadataType {

        LOCATION(1),
        REGION(2);

        private byte _id;

        MetadataType(int id) {
            _id = (byte) id;
        }

        public byte getId() {
            return _id;
        }

        public static MetadataType getById(byte id) {

            for (MetadataType type : MetadataType.values()) {

                if (type.getId() == id)
                    return type;

            }

            return null;

        }

    }

    private MetadataType _type;

    private byte _gameId;

    private Vector _primaryVector;
    private Vector _secondaryVector;

    public TurtleWorldMetadata(MetadataType type, byte gameId, Vector primaryVector, Vector secondaryVector) {
        _type = type;
        _gameId = gameId;
        _primaryVector = primaryVector;
        _secondaryVector = secondaryVector;
    }

    public static TurtleWorldMetadata loadFromStream(TurtleInputStream stream) throws IOException {

        // byte id
        // byte gameId
        // byte[3] primaryPosition
        // (byte[3] secondaryPosition)

        MetadataType type = MetadataType.getById(stream.readByte());

        if(type == null)
            throw new IOException("Malformed tworld file! ");

        byte gameId = stream.readByte();

        Vector primary = new Vector(stream.readInt(), stream.readInt(), stream.readInt());

        Vector secondary = null;

        if (type == MetadataType.REGION)
            secondary = new Vector(stream.readInt(), stream.readInt(), stream.readInt());

        return new TurtleWorldMetadata(type, gameId, primary, secondary);

    }

    public void write(TurtleOutputStream stream) throws IOException {

        stream.writeByte(_type.getId());
        stream.writeByte(_gameId);

        stream.writeIntArray(new int[]{ _primaryVector.getBlockX(), _primaryVector.getBlockY(), _primaryVector.getBlockZ() });

        if (_secondaryVector == null)
            return;

        stream.writeIntArray(new int[]{ _secondaryVector.getBlockX(), _secondaryVector.getBlockY(), _secondaryVector.getBlockZ() });

    }

    public Byte getMetaType() {
        return _gameId;
    }

}
