package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.KupljeniTiketi;
import org.springframework.data.repository.CrudRepository;

public interface KupljeniTiketiRepo extends CrudRepository<KupljeniTiketi, Long> {
}
