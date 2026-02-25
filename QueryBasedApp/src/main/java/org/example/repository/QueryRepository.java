package org.example.repository;

import org.example.entity.QueryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryRepository extends JpaRepository<QueryEntity, Long> {

    @Query("SELECT q.queryText FROM QueryEntity q WHERE q.id = :id and q.description = :description")
    String getQueryById(@Param("id") Long id, @Param("description") String description);
}