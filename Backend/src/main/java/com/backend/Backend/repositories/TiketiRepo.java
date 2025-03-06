package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.Tiket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TiketiRepo extends CrudRepository<Tiket, Long> {

    @Query(nativeQuery = true, value = "select * from tiketiFilter(?,?,?,?)")
    List<Tiket> findAllTiketiFilter(@Param("what") String what,
                                    @Param("which") String which,
                                    @Param("orderBy") String orderBy,
                                    @Param("page") Integer page);
}
