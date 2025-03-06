package com.backend.Backend.systemFiling;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    void init();

    String store(MultipartFile file, int id);

    Resource load(String filename);

    Resource loadFirst(Integer id);

    List<String> getAllFilenames(Integer id);

    void delete(String filename);

    void deleteAll(Integer id);
}
