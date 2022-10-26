package dev.komp15.generatorrecruitmenttask.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.komp15.generatorrecruitmenttask.entity.Job;
import dev.komp15.generatorrecruitmenttask.entity.JobStatus;
import lombok.Data;

@Data
public class JobDTO {
    private Long id;
    private Long minLength;
    private Long maxLength;
    private Long jobSize;
    private Character[] chars;
    @JsonIgnore
    private JobStatus status;


    public JobDTO(Job job) {
        this.id = job.getId();
        this.minLength = job.getMinLength();
        this.maxLength = job.getMaxLength();
        this.jobSize = job.getJobSize();
        this.chars = job.getChars();
        this.status = job.getStatus();
    }
}
