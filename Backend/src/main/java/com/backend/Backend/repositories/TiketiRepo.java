package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.Tiket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Query(nativeQuery = true, value = "select t.* from tiketi t join kupljeni_tiketi kt on t.id = kt.id_tiket join users u on kt.id_user = u.id where u.username = ?1 order by t.polazak")
    List<Tiket> findAllBought(@Param("username") String username);

    @Query(nativeQuery = true, value = "update tiketi set broj_tiketa = broj_tiketa-1 where id = ?1")
    @Modifying
    @Transactional
    void buyTiket(@Param("id") Long id);

    Optional<Tiket> findFirstById(Long id);

    @Query(nativeQuery = true, value = "select * from bestoffer3()")
    List<Tiket> findBestOffers();

    @Query(nativeQuery = true, value = "select * from tiketi where id in ?1 and broj_tiketa > 0")
    List<Tiket> findAllByIds(Set<Long> ids);
}