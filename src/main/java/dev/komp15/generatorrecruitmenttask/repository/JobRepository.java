package dev.komp15.generatorrecruitmenttask.repository;

import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatusEquals(@NotNull JobStatus status);
}
