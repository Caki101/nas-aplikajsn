package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.Smestaj;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SmestajRepo extends CrudRepository<Smestaj, Long> {

    @Query(value = "select * from insert_if_not_exists(:#{#smestaj.ime_smestaja},:#{#smestaj.drzava},:#{#smestaj.grad},:#{#smestaj.ocena})",
            nativeQuery = true)
    Smestaj saveIfNotExists(@Param("smestaj") Smestaj smestaj);

    @Query(nativeQuery = true, value = "select * from smestaj order by ime_smestaja limit ?1 offset 10*?2")
    List<Smestaj> getAdminAllS(Integer limit, Integer offset);
}
