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

    @Query(nativeQuery = true, value =
            "with avg_cena as (" +
                "select avg(cena) as avg_cena from tiketi)" +
            "select * from tiketi, avg_cena order by abs(cena-avg_cena) limit 1")
    Tiket findAveragePriceTiket();

    @Query(nativeQuery = true, value = "select * from tiketi where broj_tiketa > 0")
    List<Tiket> findAllAvailable();
}
