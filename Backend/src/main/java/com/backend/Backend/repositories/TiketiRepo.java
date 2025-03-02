package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.Tiket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TiketiRepo extends CrudRepository<Tiket, Integer> {

    List<Tiket> findAllByGrad(String grad);
}
