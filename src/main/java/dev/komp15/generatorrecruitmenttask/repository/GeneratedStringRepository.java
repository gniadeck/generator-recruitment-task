package dev.komp15.generatorrecruitmenttask.repository;

import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneratedStringRepository extends JpaRepository<GeneratedString, Long> {
}
