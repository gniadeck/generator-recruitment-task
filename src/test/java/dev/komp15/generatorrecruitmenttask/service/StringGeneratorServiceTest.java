package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.dto.JobDTO;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.GeneratedStringRepository;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import dev.komp15.generatorrecruitmenttask.utils.exceptions.NotValidException;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class StringGeneratorServiceTest {

    @Autowired
    private StringGeneratorService service;
    @Autowired
    private GeneratedStringRepository generatedStringRepository;
    @Autowired
    private JobRepository jobRepository;
    @Test
    @Transactional
//    @Timeout(20000)
    public void doALotOfJobs() throws InterruptedException {
        JobCreationRequestDTO request = new JobCreationRequestDTO(
                1L,100L,getAllCharacters(), 500L
        );
        List<JobDTO> runningJobs = new ArrayList<>();
        for(int i = 0; i < 2000; i++){
            runningJobs.add(service.addJob(request));
        }
        long time = System.currentTimeMillis();
        while(true){
            Thread.sleep(500);
            boolean threadsWorking = runningJobs.stream()
                    .anyMatch(j -> j.getStatus().equals(JobStatus.EXECUTING));
//            System.out.println("Running jobs: " + service.getRunningJobs());
            if(!threadsWorking) break;
        }
        System.out.println("Elapsed time of generation: " + (System.currentTimeMillis()-time));

//        assertTrue(20L*5000L, generatedStringRepository.count());
        assertEquals(20*500000, generatedStringRepository.count());
//        System.out.println(runningJobs.get(0));
//        System.out.println(jobRepository.findById(runningJobs.get(0).getId()).orElseThrow());
    }

    private Character[] getAmountOfCharacters(int amount){
        return Arrays.stream(getAllCharacters())
                .limit(amount)
                .toArray(Character[]::new);
    }

    private Character[] getAllCharacters(){
        return new Character[]{'a','b','c','d','e','f','g','h','i','j','k','l','m',
                'n','o','p','r','s','t','u','w','x','y','z'};
    }

    @Test
    public void addJobShouldThrowOnJobsizeBiggerThanMaximum(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(1L)
                .maxLength(10L)
                .chars(getAmountOfCharacters(3))
                .jobSize(1000000L)
                .build();

        assertThrows(NotValidException.class, () -> service.addJob(jobCreationRequestDTO));
    }

    @Test
    public void addJobShouldThrowOnMinLengthBiggerThanMaxLength(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(100L)
                .maxLength(10L)
                .chars(getAllCharacters())
                .jobSize(1000000L)
                .build();

        assertThrows(NotValidException.class, () -> service.addJob(jobCreationRequestDTO));
    }

    @Test
    public void addJobShouldThrowOnNegativeMinLength(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(-1L)
                .maxLength(10L)
                .chars(getAllCharacters())
                .jobSize(100000L)
                .build();

        assertThrows(ConstraintViolationException.class, () -> service.addJob(jobCreationRequestDTO));
    }

    @Test
    public void addJobShouldThrowOnNegativeMaxLength(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(1L)
                .maxLength(-10L)
                .chars(getAllCharacters())
                .jobSize(100000L)
                .build();

        assertThrows(ConstraintViolationException.class, () -> service.addJob(jobCreationRequestDTO));
    }

    @Test
    public void addJobShouldThrowOnEmptyChars(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(1L)
                .maxLength(-10L)
                .chars(new Character[0])
                .jobSize(100000L)
                .build();

        assertThrows(NotValidException.class, () -> service.addJob(jobCreationRequestDTO));
    }

}
