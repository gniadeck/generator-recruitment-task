package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.repository.GeneratedStringRepository;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class JobServiceTest {

    @Autowired
    private JobService service;
    @Autowired
    private GeneratedStringRepository generatedStringRepository;
    @Autowired
    private JobRepository jobRepository;

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

        Throwable t = assertThrows(Throwable.class, () -> service.addJob(jobCreationRequestDTO));

        assertThat(t)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void addJobShouldThrowOnMinLengthBiggerThanMaxLength(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(100L)
                .maxLength(10L)
                .chars(getAllCharacters())
                .jobSize(1000000L)
                .build();

        Throwable t = assertThrows(Throwable.class, () -> service.addJob(jobCreationRequestDTO));

        assertThat(t)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void addJobShouldThrowOnNegativeMinLength(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(-1L)
                .maxLength(10L)
                .chars(getAllCharacters())
                .jobSize(100000L)
                .build();

        Throwable t = assertThrows(Throwable.class, () -> service.addJob(jobCreationRequestDTO));

        assertThat(t)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void addJobShouldThrowOnNegativeMaxLength(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(1L)
                .maxLength(-10L)
                .chars(getAllCharacters())
                .jobSize(100000L)
                .build();

        Throwable t = assertThrows(Throwable.class, () -> service.addJob(jobCreationRequestDTO));

        assertThat(t)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void addJobShouldThrowOnEmptyChars(){
        JobCreationRequestDTO jobCreationRequestDTO = JobCreationRequestDTO.builder()
                .minLength(1L)
                .maxLength(-10L)
                .chars(new Character[0])
                .jobSize(100000L)
                .build();


        Throwable t = assertThrows(Throwable.class, () -> service.addJob(jobCreationRequestDTO));

        assertThat(t)
                .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

}
