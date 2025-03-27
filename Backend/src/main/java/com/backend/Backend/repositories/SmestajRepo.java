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

    @Query(nativeQuery = true, value = "select * from filtered_admin_getS(?1,?2,?3,?4)")
    List<Smestaj> getAdminAll(@Param("limit") Integer limit,
                              @Param("offset") Integer offset,
                              @Param("filter") String filter,
                              @Param("asc_desc") String asc_desc);
}
