package dev.komp15.generatorrecruitmenttask;

import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Disabled
public class TestUtils {

    @Autowired
    private JobRepository jobRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveJob(Job job){
        jobRepository.save(job);
    }

}
