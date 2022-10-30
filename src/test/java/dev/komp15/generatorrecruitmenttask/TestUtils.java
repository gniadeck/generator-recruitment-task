package dev.komp15.generatorrecruitmenttask;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@Component
@Disabled
public class TestUtils {

    @Autowired
    private JobRepository jobRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveJob(Job job){
        jobRepository.save(job);
    }

    public Character[] getAmountOfCharacters(int amount){
        return Arrays.stream(getAllCharacters())
                .limit(amount)
                .toArray(Character[]::new);
    }

    public JobCreationRequestDTO getCreationRequestFromJob(Job job) {
        return new JobCreationRequestDTO(
                job.getMinLength(),
                job.getMaxLength(),
                job.getChars(),
                job.getJobSize()
        );
    }

    public Job getJobWithJobSize(int size) {
        return Job.builder()
                .minLength(1L)
                .maxLength(10L)
                .chars(getAllCharacters())
                .generatedStrings(new HashSet<>())
                .jobSize((long) size)
                .status(JobStatus.EXECUTING)
                .build();
    }

    public Character[] getAllCharacters() {
        return new Character[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'r', 's', 't', 'u', 'w', 'x', 'y', 'z'};
    }

}
