package com.backend.Backend.systemFiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class FileSystemStorageService implements StorageService {
    public final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageConfig properties) {
        if(properties.getLocation().trim().isEmpty()){
            throw new RuntimeException("Location is empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public MultipartFile load(String filename) {
        return null;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    /**
     * documentation
     *
     * @return Saved file name.
     */
    @Override
    public String store(MultipartFile file, int blog_id) {
        String file_name;
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            file_name = blog_id + "_img_" + Timestamp.valueOf(LocalDateTime.now())
                    .toString()
                    .replace(':','_')
                    .replace('-','_')
                    .replace(' ','-');
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            Path destinationFile = this.rootLocation.resolve(Paths.get(file_name+fileExtension)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store files outside current folder");
            }

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            file_name += fileExtension;
        }
        catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }

        return file_name;
    }

    /** documentation */
    @Override
    public void delete(String file_name) {
        try{
            Path destinationFile = this.rootLocation.resolve(Paths.get(file_name)).normalize().toAbsolutePath();

            Files.deleteIfExists(destinationFile);
        }
        catch (Exception e){
            throw new RuntimeException("Could not delete file", e);
        }
    }
}
