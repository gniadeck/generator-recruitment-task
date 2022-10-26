package dev.komp15.generatorrecruitmenttask.repository;

import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GeneratedStringRepository extends JpaRepository<GeneratedString, Long> {
}
