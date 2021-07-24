package com.bklg.csvdemo.repository;


import java.util.List;

import javax.transaction.Transactional;

import com.bklg.csvdemo.domain.Space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer>{
    
    @Modifying
    @Transactional
    @Query("UPDATE spaces SET id = id * -1 WHERE id = :id")
    int logicalDelete(@Param("id") Integer id);

    @Query(nativeQuery = true ,value = "SELECT s.* FROM spaces s WHERE s.id > 0 ORDER BY s.id")
    List<Space> findByIdGreterThanZero();
}
