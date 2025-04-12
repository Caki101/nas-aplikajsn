package com.backend.Backend.systemFiling;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    void init();

    String store(MultipartFile file, Long id);

    Resource load(String filename);

    Resource loadFirst(Long id);

    List<String> getAllFilenames(Long id);

    void delete(String filename);

    void deleteAll(Long id);
}
