package com.jumkid.media.repository;
/*
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2019 Jumkid Innovation All rights reserved.
 */
import com.jumkid.media.model.MediaFile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Optional;

@Component
@org.springframework.context.annotation.Configuration
public class HadoopFileStorage implements FileStorage<MediaFile> {

    private static final Logger logger = LoggerFactory.getLogger(HadoopFileStorage.class);

    @Value("${mediaserver.data.home}")
    private String defaultStorePath;

    private final Configuration conf;

    private final FilePathManager filePathManager;

    @Autowired
    public HadoopFileStorage(FilePathManager filePathManager,
                             @Value("${hdfs.namenode.host}") String nameNodeHost,
                             @Value("${hdfs.namenode.port}") int nameNodePort) {
        this.filePathManager = filePathManager;

        conf = new Configuration();
        String hdfsUri = "hdfs://" + nameNodeHost + ":" + nameNodePort;

        conf.set("fs.defaultFS", hdfsUri);
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        //TODO - do a simple HDFS status check
    }

    @Override
    public MediaFile saveFile(byte[] bytes, MediaFile mediaFile) {
        FSDataOutputStream outputStream = null;
        try (FileSystem fs = FileSystem.get(conf)) {
            //get file full path for the media file
            String filePath = defaultStorePath + filePathManager.getFullPath(mediaFile);
            String folderPath = filePath.substring(0, filePath.lastIndexOf(FilePathManager.DELIMITER));

            Path newFolderPath= new Path(folderPath);
            if(!fs.exists(newFolderPath)) {
                // Create new Directory
                fs.mkdirs(newFolderPath);
                logger.info("Path {} created.", folderPath);
            }

            //---- write file
            logger.info("write file into hdfs start");
            //Create a path
            Path writePath = new Path(filePath);
            //Init output stream
            outputStream = fs.create(writePath);
            //Cassical output stream usage
            outputStream.write(bytes);
            logger.info("write file into hdfs ended");

            mediaFile.setLogicalPath(folderPath);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to save media file {}", e.getMessage());
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                logger.error("Failed to save media file {}", ioe.getMessage());
            }
        }

        return mediaFile;
    }

    @Override
    public Optional<byte[]> getFileBinary(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<FileChannel> getFileRandomAccess(String id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteFile(String logicalPath) {
        try (FileSystem fs = FileSystem.get(conf)) {

            Path path = new Path(logicalPath);
            if(!fs.exists(path)) {
                logger.error("the path of target file to be deleted does not exist");
                return false;
            }

            return fs.delete(path, true);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("Failed to delete media file {}", ioe.getMessage());
            return false;
        }
    }

    @Override
    public Optional<FileChannel> getThumbnail(String id, boolean large) {
        return Optional.empty();
    }
}
