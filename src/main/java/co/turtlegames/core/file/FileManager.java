package co.turtlegames.core.file;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.util.AuthInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.lokra.seaweedfs.core.FileSource;
import org.lokra.seaweedfs.core.FileTemplate;

import java.io.*;

public class FileManager extends TurtleModule {

    private FileSource _source;

    public FileManager(JavaPlugin plugin) {
        super(plugin, "File Manager");
    }

    public void initializeModule() {

        _source = new FileSource();
        _source.setHost(AuthInfo.MASTER_SERVER_HOST);
        _source.setPort(9333);

        try {
            _source.startup();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!getPlugin().getDataFolder().exists())
            getPlugin().getDataFolder().mkdirs();

    }

    public void deinitializeModule() {

        try {
            _source.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public File getFile(String name) {

        FileTemplate template = new FileTemplate(_source.getConnection());

        try {

            InputStream inputStream = template.getFileStream(name).getInputStream();
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            File target = new File(getPlugin().getDataFolder(), name);
            OutputStream outputStream = new FileOutputStream(target);
            outputStream.write(buffer);
            outputStream.close();

            return target;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void saveFile(String name, File file) {

        if (!file.exists())
            return;

        FileTemplate template = new FileTemplate(_source.getConnection());

        try {
            FileInputStream stream = new FileInputStream(file);
            template.saveFileByStream(name, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
