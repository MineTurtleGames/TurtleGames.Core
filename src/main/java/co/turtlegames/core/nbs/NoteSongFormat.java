package co.turtlegames.core.nbs;

import co.turtlegames.core.world.tworld.io.TurtleInputStream;

import java.io.DataInputStream;

public class NoteSongFormat {

    private String _name;

    private String _author;
    private String _originalAuthor;

    private String _description;

    private short _length;
    private short _height;

    private NoteSongFormat() {

    }

    private void loadFromStream(TurtleInputStream stream) {

    }

}
