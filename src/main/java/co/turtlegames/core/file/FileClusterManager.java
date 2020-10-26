package co.turtlegames.core.file;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.util.AuthInfo;
import io.minio.ErrorCode;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileClusterManager extends TurtleModule {

    private MinioClient _minioClient;

    public FileClusterManager(JavaPlugin plugin) {
        super(plugin, "File Cluster Manager");

        try {
            _minioClient = new MinioClient(AuthInfo.FILESTORE_HOST, AuthInfo.FILESTORE_ACCESS_KEY, AuthInfo.FILESTORE_SECRET_KEY);
        } catch(MinioException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void initializeModule() {

    }

    public boolean doesFileExist(String bucket, String objectName) throws IOException {

        try {
            _minioClient.statObject(bucket, objectName);
            return true;
        } catch (ErrorResponseException ex) {

            if(ex.errorResponse().errorCode() == ErrorCode.NO_SUCH_KEY
                || ex.errorResponse().errorCode() == ErrorCode.NO_SUCH_OBJECT)
                    return false;

            throw new IOException(ex);

        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new IOException(ex);
        }

    }

    public InputStream grabFileStream(String bucket, String objectName) {

        try {

            if(!this.doesFileExist(bucket, objectName))
                return null;

            return _minioClient.getObject(bucket, objectName);

        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public void putFileStream(String bucket, String objectName, InputStream stream, PutObjectOptions options) throws IOException{

        try {

            _minioClient.putObject(bucket, objectName, stream, options);
            stream.close();

        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new IOException(ex);
        }

    }

    public void putByteStream(String bucket, String objectName, ByteArrayInputStream stream) throws IOException {

        PutObjectOptions options = new PutObjectOptions(stream.available(), -1);
        this.putFileStream(bucket, objectName, stream, options);

    }

    public void validateBucket(String bucketName) {

        try {

            if(!_minioClient.bucketExists(bucketName))
                _minioClient.makeBucket(bucketName);

        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException ex) {
            ex.printStackTrace();
        }

    }

}
