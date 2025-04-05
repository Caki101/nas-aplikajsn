package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.KupljeniTiketi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KupljeniTiketiRepo extends CrudRepository<KupljeniTiketi, Long> {
    Page<KupljeniTiketi> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "select id_tiket,count(*) from kupljeni_tiketi kt group by id_tiket order by count(*) desc limit 5")
    List<Object[]> top5Tikets();
}
