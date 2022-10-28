package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.dto.JobDTO;
import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.GeneratedStringRepository;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import dev.komp15.generatorrecruitmenttask.utils.exceptions.NotValidException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class StringGeneratorService {

    private final JobRepository jobRepository;
    private GeneratorWorker generatorWorker;

    public JobDTO addJob(JobCreationRequestDTO request){
        Job job = new Job(request);

        if(isValid(job)){
            jobRepository.save(job);
            generatorWorker.execute(job);
        }

        return new JobDTO(job);
    }

    private boolean isValid(Job job){
        int maxJobSize = 1;
        for(int i = 1; i < job.getChars().length; i++){
            maxJobSize = maxJobSize *  i;
        }
        log.info("Max job size for job " + job.getId() + " - " + maxJobSize);

        boolean isValid = job.getMinLength() <= job.getMaxLength() && maxJobSize >= job.getJobSize();

        if(!isValid && !(job.getMinLength() <= job.getMaxLength())){
            throw new NotValidException("Minimum length should be smaller than maximum length");
        } else if(!isValid && !(maxJobSize >= job.getJobSize())){
            throw new NotValidException("Requested job size is not achievable!");
        }

        return isValid;
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
