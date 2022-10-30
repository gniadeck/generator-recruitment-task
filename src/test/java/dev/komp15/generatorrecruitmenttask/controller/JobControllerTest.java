package dev.komp15.generatorrecruitmenttask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.komp15.generatorrecruitmenttask.TestUtils;
import dev.komp15.generatorrecruitmenttask.dto.ExceptionDTO;
import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.dto.JobDTO;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import dev.komp15.generatorrecruitmenttask.repository.JobRepository;
import dev.komp15.generatorrecruitmenttask.service.StringGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JobControllerTest {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private TestUtils testUtils;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private StringGeneratorService stringGeneratorService;


    @PostConstruct
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.setDateFormat(new StdDateFormat());
    }

    @Test
    @Rollback
    public void getJobShouldGetJob() throws Exception {
        Job testJob = testUtils.getJobWithJobSize(100);
        jobRepository.save(testJob);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/job/" + testJob.getId()))
                .andReturn();

        Job respondedJob = objectMapper.readValue(response.getResponse().getContentAsString(), Job.class);

        assertEquals(testJob, respondedJob);
    }

    @Test
    public void getJobShouldThrowOnNotExistingId() throws Exception {

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/job/1"))
                .andReturn();

        ExceptionDTO exceptionDTO = objectMapper.readValue(response.getResponse().getContentAsString(), ExceptionDTO.class);

        assertEquals(500, exceptionDTO.code());
    }

    @Test
    public void runningJobsShouldReturnRunningJobs() throws Exception {

        List<Job> runningJobs = List.of(testUtils.getJobWithJobSize(50),
                testUtils.getJobWithJobSize(100), testUtils.getJobWithJobSize(25));

        jobRepository.saveAll(runningJobs);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/job/running"))
                .andReturn();

        List<Job> respondedJobs = Arrays.asList(objectMapper.readValue(response.getResponse().getContentAsString(), Job[].class));

        assertThat(respondedJobs)
                .containsExactlyElementsOf(runningJobs);

    }

    @Test
    @Transactional
    @Rollback
    public void postJobShouldDoJob() throws Exception {
        Job testJob = testUtils.getJobWithJobSize(1);


        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/job")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUtils.getCreationRequestFromJob(testJob))))
                .andReturn();

        JobDTO returnedJobDTO = objectMapper.readValue(response.getResponse().getContentAsString(), JobDTO.class);

        ArgumentCaptor<Job> jobCaptor = ArgumentCaptor.forClass(Job.class);

        verify(stringGeneratorService, times(1))
                .execute(jobCaptor.capture());

        assertEquals(returnedJobDTO.getId(), jobCaptor.getValue().getId());

    }







}
