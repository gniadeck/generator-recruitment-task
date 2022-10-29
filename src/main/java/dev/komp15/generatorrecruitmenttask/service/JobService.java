package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.dto.JobDTO;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class JobService {

    private final JobRepository jobRepository;
    private StringGeneratorService stringGeneratorService;

    public JobDTO addJob(JobCreationRequestDTO request) {
        Job job = new Job(request);

        jobRepository.save(job);
        stringGeneratorService.execute(job);

        return new JobDTO(job);
    }


    public Job findJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Job with id " + id + " does not exist."));
    }

    public synchronized List<JobDTO> getRunningJobs() {
        return jobRepository.findByStatusEquals(JobStatus.EXECUTING).stream()
                .map(JobDTO::new)
                .collect(Collectors.toList());
    }

}
