package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.TestUtils;
import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureTestDatabase
public class StringGeneratorServiceTest {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private StringGeneratorService stringGeneratorService;
    @Autowired
    private TestUtils testUtils;

    @Test
    @Rollback
    @Transactional
    public void executeShouldGenerateGivenAmountOfStrings() throws ExecutionException, InterruptedException {

        Random random = new Random();

        for(int i = 0; i < 10; i++){

            int jobSize = random.nextInt(300)+1;
            Job testedJob = getJobWithJobSize(jobSize);
            testUtils.saveJob(testedJob);

            Job result = stringGeneratorService.execute(testedJob).get();
            result = jobRepository.findById(result.getId()).orElseThrow();

            assertEquals(jobSize, result
                    .getGeneratedStrings()
                    .size());

        }

    }

    @Test
    @Rollback
    public void executeShouldSetJobStatusToDone() throws ExecutionException, InterruptedException {
        Job testedJob = getJobWithJobSize(10);
        jobRepository.save(testedJob);
        assertEquals(JobStatus.DONE, stringGeneratorService.execute(testedJob).get().getStatus());
    }

    @Test
    @Rollback
    @Transactional
    public void executeShouldGenerateGivenStringsInLengthRange() throws ExecutionException, InterruptedException {

        Random random = new Random();

        for(int i = 0; i < 10; i++){
            int jobSize = random.nextInt(300);
            Job testedJob = getJobWithJobSize(jobSize);
            testUtils.saveJob(testedJob);
            Job doneJob = stringGeneratorService.execute(testedJob).get();
            doneJob = jobRepository.findById(doneJob.getId()).orElseThrow();

            for(GeneratedString s : doneJob.getGeneratedStrings()){
                assertThat(s.getData().length())
                        .isBetween(Math.toIntExact(testedJob.getMinLength()), Math.toIntExact(testedJob.getMaxLength()));
            }

        }

    }

    @Test
    @Rollback
    public void executeShouldCompleteSmallerJobsBeforeBigger() throws ExecutionException, InterruptedException {

        Job smallJob = getJobWithJobSize(100);
        Job bigJob = getJobWithJobSize(10000);
        jobRepository.save(smallJob);
        jobRepository.save(bigJob);

        CompletableFuture<Job> bigJobExecuted = stringGeneratorService.execute(bigJob);
        CompletableFuture<Job> smallJobExecuted = stringGeneratorService.execute(smallJob);

        bigJob = bigJobExecuted.get();
        smallJob = smallJobExecuted.get();

        assertThat(getExecutionDuration(bigJob))
                .isGreaterThan(getExecutionDuration(smallJob));

    }

    private Duration getExecutionDuration(Job job){
        return Duration.between(job.getSubmitTime(), job.getFinishedTime());
    }

    @Test
    @Rollback
    @Transactional
    public void executeShouldGenerateGivenStringsInCharacterRange() throws ExecutionException, InterruptedException {

        Random random = new Random();

        for(int i = 0; i < 10; i++){
            int jobSize = random.nextInt(300)+1;
            Job testedJob = getJobWithJobSize(jobSize);
            testUtils.saveJob(testedJob);
            Job doneJob = stringGeneratorService.execute(testedJob).get();
            doneJob = jobRepository.findById(doneJob.getId()).orElseThrow();


            List<String> requestedCharacters = Arrays.stream(testedJob.getChars())
                    .map(Object::toString)
                    .toList();

            for(GeneratedString s : doneJob.getGeneratedStrings()){
                List<String> generatedCharacters = Arrays.stream(s.getData().split(""))
                        .toList();

                assertThat(generatedCharacters)
                        .isSubsetOf(requestedCharacters);

            }

        }

    }

    private Job getJobWithJobSize(int size){
        return Job.builder()
                .minLength(1L)
                .maxLength(10L)
                .chars(getAllCharacters())
                .generatedStrings(new HashSet<>())
                .jobSize((long) size)
                .status(JobStatus.EXECUTING)
                .build();
    }



    private Character[] getAllCharacters(){
        return new Character[]{'a','b','c','d','e','f','g','h','i','j','k','l','m',
                'n','o','p','r','s','t','u','w','x','y','z'};
    }














}
