package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query(value = "SELECT * FROM news ORDER BY date_time DESC LIMIT :limit", nativeQuery = true)
    List<News> findNewest(@Param("limit") int limit);
}
