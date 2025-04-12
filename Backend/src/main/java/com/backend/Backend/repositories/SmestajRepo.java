package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.Smestaj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SmestajRepo extends CrudRepository<Smestaj, Long> {

    @Query(value = "select * from insert_if_not_exists(:#{#smestaj.ime_smestaja},:#{#smestaj.drzava},:#{#smestaj.grad},:#{#smestaj.ocena})",
            nativeQuery = true)
    Smestaj saveIfNotExists(@Param("smestaj") Smestaj smestaj);

    @Query(nativeQuery = true, value = "select * from smestaj")
    Page<Smestaj> getAdminAll(Pageable pageable);

    @Query(nativeQuery = true, value = "select s.id, count(*) " +
            "from kupljeni_tiketi kt join tiketi t " +
            "on kt.id_tiket = t.id join smestaj s " +
            "on t.smestaj_id = s.id " +
            "group by s.id " +
            "order by count(*) desc " +
            "limit 5")
    List<Object[]> top5Smestajs();

    @Query(nativeQuery = true, value = "select * from smestaj where id in ?1")
    List<Smestaj> adminFindAllByIds(Set<Long> ids);
}
