package dev.komp15.generatorrecruitmenttask.dto;

import dev.komp15.generatorrecruitmenttask.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class JobCreationRequestDTO {
    private Long minLength;
    private Long maxLength;
    private Character[] chars;
    private Long jobSize;


    public JobCreationRequestDTO(Job job) {
        this.minLength = job.getMinLength();
        this.maxLength = job.getMaxLength();
        this.chars = job.getChars();
        this.jobSize = job.getJobSize();
    }
}
