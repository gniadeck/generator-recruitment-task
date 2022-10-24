package dev.komp15.generatorrecruitmenttask.service;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.dto.JobDTO;
import dev.komp15.generatorrecruitmenttask.entity.GeneratedString;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.repository.GeneratedStringRepository;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import dev.komp15.generatorrecruitmenttask.utils.exceptions.NotValidException;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class StringGeneratorService {

    private final JobRepository jobRepository;
    private final Random random = new Random();
    private final GeneratedStringRepository stringRepository;
    private final EntityManagerFactory entityManagerFactory;
    // TODO USE EXECUTOR SERVICE OR STUFF

    @Transactional
    public Job addJob(JobCreationRequestDTO request){
        Job job = new Job(request);

        if(isValid(job)){
            jobRepository.save(job);
            execute(job);
        } else {
            throw new NotValidException("Your request is not valid, please check parameters");
        }

        return job;
    }

    private void execute(Job job) {
        Thread thread = new Thread(() -> {
            for(int i = 0; i < job.getJobSize(); i++){
                generateStringForJob(job);
            }
            job.setIsDone(true);
        });
        thread.start();
    }

    private boolean isValid(Job job){
        int maxJobSize = 1;
        for(int i = 1; i < job.getChars().length; i++){
            maxJobSize = maxJobSize *  i;
        }
        System.out.println("Max job size " + maxJobSize);
        return maxJobSize >= job.getJobSize();
    }

    private void generateStringForJob(Job job){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < generateStringLength(job); i++){
            builder.append(generateRandomCharacter(job));
        }
        addToJob(builder.toString(), job);
//        GeneratedString generatedString = new GeneratedString(builder.toString());
//
//        synchronized (StringGeneratorService.class) {
//            stringRepository.save(generatedString);
//            job.getGeneratedStrings().add(generatedString);
//        }

    }

    private synchronized void addToJob(String s, Job job){
        GeneratedString generatedString = new GeneratedString(s);

        stringRepository.save(generatedString);
        job.getGeneratedStrings().add(generatedString);
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

    public synchronized List<JobCreationRequestDTO> getRunningJobs() {
       EntityManager manager =  entityManagerFactory.unwrap(SessionFactory.class)
               .openSession();
       CriteriaQuery<Job> query = manager.getCriteriaBuilder().createQuery(Job.class);

       query = query.select(query.from(Job.class))
               .where(manager.getCriteriaBuilder().isFalse(query.from(Job.class).get("isDone")));

    List<Job> result = manager.createQuery(query).getResultList();
       return jobRepository.findJobByIsDoneEquals(false).stream()
               .map(JobCreationRequestDTO::new)
               .collect(Collectors.toList());
//       return ;
    }

}
