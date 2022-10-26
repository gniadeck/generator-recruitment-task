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
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@EnableAsync
public class StringGeneratorService {

    private final JobRepository jobRepository;
    private final Random random = new Random();
    private final GeneratedStringRepository stringRepository;
    private final EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private final EntityManager entityManager;
    private GeneratorWorker generatorWorker;
    // TODO USE EXECUTOR SERVICE OR STUFF

//    @Transactional
//    @Async
    public JobDTO addJob(JobCreationRequestDTO request){
        Job job = new Job(request);

        if(isValid(job)){

            jobRepository.save(job);
//            entityManager.flush();
//            job = jobRepository.findById(job.getId()).orElseThrow();
            System.out.println("PERSISTED? " + entityManager.contains(job));
            generatorWorker.execute(job);
        } else {
            throw new NotValidException("Your request is not valid, please check parameters");
        }

        return new JobDTO(job);
    }

    @Transactional
//    @Async
    void execute(Job job) {
//        jobRepository.save(job);
//        Thread thread = new Thread(new GeneratorWorker(job));
//        Thread thread = new Thread(() -> {
//            jobRepository.save(job);
            System.out.println("PERSISTED? " + entityManager.contains(job));
            job.setStatus(JobStatus.EXECUTING);
            try{
            for(int i = 0; i < job.getJobSize(); i++){
                if(i % 10000 == 0) log.info("Generating string for job id " + job.getId());
                generateStringForJob(job);
            }
            job.setStatus(JobStatus.DONE);
//            jobRepository.save(job);
            } catch(Throwable t){
                log.error("Job " + job.getId() + " crashed!");
                job.setStatus(JobStatus.CRASHED);
            }
//        }
//        );
//        thread.start();
    }

    private boolean isValid(Job job){
        int maxJobSize = 1;
        for(int i = 1; i < job.getChars().length; i++){
            maxJobSize = maxJobSize *  i;
        }
        System.out.println("Max job size " + maxJobSize);
        return job.getMinLength() <= job.getMaxLength() && maxJobSize >= job.getJobSize();
    }

//    @Transactional
    void generateStringForJob(Job job){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < generateStringLength(job); i++){
            builder.append(generateRandomCharacter(job));
        }
        addToJob(builder.toString(), job);

//        jobRepository.save(job);
//        GeneratedString generatedString = new GeneratedString(builder.toString());
//
//        synchronized (StringGeneratorService.class) {
//            stringRepository.save(generatedString);
//            job.getGeneratedStrings().add(generatedString);
//        }

    }

    @Transactional
    void addToJob(String s, Job job){
        GeneratedString generatedString = new GeneratedString(s);
//        stringRepository.save(generatedString);
        job.getGeneratedStrings().add(generatedString);
//        jobRepository.saveAndFlush(job);
//        stringRepository.saveAndFlush(generatedString);
//        jobRepository.save(job);
//        jobRepository.save(job);
//        stringRepository.save(generatedString);
//        job.getGeneratedStrings().add(generatedString);
//        jobRepository.save(job);
//        System.out.println("job: " + job);
    }

    private long generateStringLength(Job job){
        return job.getMinLength() + random.nextLong(job.getMaxLength()-job.getMinLength());
    }

    private char generateRandomCharacter(Job job){
        return job.getChars()[random.nextInt(job.getChars().length)];
    }

    public Job findJobById(Long id) {
        return jobRepository.findById(id).orElseThrow();
    }

    public synchronized List<JobDTO> getRunningJobs() {
       return jobRepository.findByStatusEquals(JobStatus.EXECUTING).stream()
               .map(JobDTO::new)
               .collect(Collectors.toList());
    }

}
