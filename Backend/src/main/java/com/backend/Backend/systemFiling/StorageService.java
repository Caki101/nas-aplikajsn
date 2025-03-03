package com.backend.Backend.systemFiling;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();

    String store(MultipartFile file, int id);

    MultipartFile load(String filename);

    void delete(String filename);
}
