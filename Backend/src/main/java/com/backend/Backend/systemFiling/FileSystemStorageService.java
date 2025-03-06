package com.backend.Backend.systemFiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    final Path rootLocation;
    ResourceLoader resourceLoader;

    @Autowired
    public FileSystemStorageService(StorageConfig properties,
                                    ResourceLoader resourceLoader) {
        if(properties.getLocation().trim().isEmpty()){
            throw new RuntimeException("Location is empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.resourceLoader = resourceLoader;
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

    @Override
    public Resource load(String filename) {
        Resource resource = resourceLoader.getResource("file:" + rootLocation + "/" + filename);

        if (!resource.exists()) resource = resourceLoader.getResource("file:" + rootLocation + "/default.png");

        return resource;
    }

    @Override
    public Resource loadFirst(Integer id) {
        Resource resource;

        try (Stream<Path> paths = Files.walk(rootLocation)) {
            Optional<Path> first_img = paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(id.toString()))
                    .findFirst();

            resource = first_img.map(path -> resourceLoader.getResource("file:" + path)).orElseGet(() -> resourceLoader.getResource("file:" + rootLocation + "/default.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resource;
    }

    @Override
    public List<String> getAllFilenames(Integer id) {
        List<String> filenames = new ArrayList<>();

        try (Stream<Path> files = Files.walk(rootLocation)) {
            files.filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().startsWith(id.toString()))
                    .forEach(file -> filenames.add(file.getFileName().toString()));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filenames;
    }

    /**
     * documentation
     *
     * @return Saved file name.
     */
    @Override
    public String store(MultipartFile file, int id) {
        String file_name;
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            file_name = id + "_img_" + Timestamp.valueOf(LocalDateTime.now())
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

    @Override
    public void deleteAll(Integer id) {

    }
}
