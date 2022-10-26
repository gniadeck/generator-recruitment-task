package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.dto.JobDTO;
import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor
public class GeneratorWorker{

    @PersistenceContext
    private EntityManager entityManager;
//    private Job job;
    private Random random = new Random();
//    @Autowired
    private JobRepository jobRepository;

    @Autowired
    public GeneratorWorker(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    @Async
    @Transactional
    public void execute(Job job) {
        job = jobRepository.findById(job.getId()).orElseThrow();
//        System.out.println("PERSISTED? " + entityManager.contains(job));
        job = jobRepository.findById(job.getId()).orElseThrow();
        job.setStatus(JobStatus.EXECUTING);

        try{
            Set<Integer> breakPoints = new HashSet<>();
            for(int i = 0; i < 10; i++){
                breakPoints.add((int) (job.getJobSize()/10*i));
            }
            for(int i = 0; i < job.getJobSize(); i++){
                if(i % 10000 == 0) System.out.println("Generating string for job id " + job.getId());

                if(breakPoints.contains(i)){
                    job.setPercentageDone(job.getPercentageDone()+10);
                    jobRepository.save(job);
                }

                generateStringForJob(job);
            }
            job.setStatus(JobStatus.DONE);
//            jobRepository.save(job);
        } catch(Throwable t){
            t.printStackTrace();
            log.error("Job " + job.getId() + " crashed!");
            job.setStatus(JobStatus.CRASHED);
        }
    }

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

//    @Transactional
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


}
