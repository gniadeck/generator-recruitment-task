package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import org.assertj.core.api.NumberAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
public class GeneratorWorkerTest {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private GeneratorWorker generatorWorker;

    @Test
    @Rollback
    public void executeShouldGenerateGivenAmountOfStrings() throws ExecutionException, InterruptedException {

        Random random = new Random();

        for(int i = 0; i < 10; i++){
            int jobSize = random.nextInt(300);
            Job testedJob = getJobWithJobSize(jobSize);
            jobRepository.save(testedJob);
            assertEquals(jobSize, generatorWorker.execute(testedJob)
                    .get()
                    .getGeneratedStrings()
                    .size());
        }

    }

    @Test
    @Rollback
    public void executeShouldSetJobStatusToDone() throws ExecutionException, InterruptedException {
        Job testedJob = getJobWithJobSize(10);
        jobRepository.save(testedJob);
        assertEquals(JobStatus.DONE, generatorWorker.execute(testedJob).get().getStatus());
    }

    @Test
    @Rollback
    public void executeShouldGenerateGivenStringsInLengthRange() throws ExecutionException, InterruptedException {

        Random random = new Random();

        for(int i = 0; i < 10; i++){
            int jobSize = random.nextInt(300);
            Job testedJob = getJobWithJobSize(jobSize);
            jobRepository.save(testedJob);
            Job doneJob = generatorWorker.execute(testedJob).get();

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

        CompletableFuture<Job> bigJobExecuted = generatorWorker.execute(bigJob);
        CompletableFuture<Job> smallJobExecuted = generatorWorker.execute(smallJob);

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
    public void executeShouldGenerateGivenStringsInCharacterRange() throws ExecutionException, InterruptedException {

        Random random = new Random();

        for(int i = 0; i < 10; i++){
            int jobSize = random.nextInt(300);
            Job testedJob = getJobWithJobSize(jobSize);
            jobRepository.save(testedJob);
            Job doneJob = generatorWorker.execute(testedJob).get();


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
//
//
//
//    private Job getJobWithCharactersAndSize(Character[] chars, int size){
//        return Job.builder()
//                .minLength(1L)
//                .maxLength(10L)
//                .chars(chars)
//                .generatedStrings(new HashSet<>())
//                .jobSize((long) size)
//                .status(JobStatus.EXECUTING)
//                .build();
//    }


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

//    private Job getJobInRange(int min, int max){
//        return Job.builder()
//                .minLength((long) min)
//                .maxLength((long) max)
//                .chars(getAllCharacters())
//                .generatedStrings(new HashSet<>())
//                .jobSize(100L)
//                .status(JobStatus.EXECUTING)
//                .build();
//    }

    private Character[] getAllCharacters(){
        return new Character[]{'a','b','c','d','e','f','g','h','i','j','k','l','m',
                'n','o','p','r','s','t','u','w','x','y','z'};
    }














}
