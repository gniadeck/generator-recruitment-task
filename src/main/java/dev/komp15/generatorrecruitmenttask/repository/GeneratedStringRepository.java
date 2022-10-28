package dev.komp15.generatorrecruitmenttask.repository;

import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GeneratedStringRepository extends JpaRepository<GeneratedString, Long> {


    @Modifying
    @Query(value = "INSERT INTO GENERATED_STRING (DATA, JOB_ID) VALUES (:data, :jobId)", nativeQuery = true)
    void insertStringForJob(String data, Long jobId);
}
