package dev.komp15.generatorrecruitmenttask.entity;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import dev.komp15.generatorrecruitmenttask.utils.validation.ValidationUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private LocalDateTime submitTime;
    @Positive
    private Long minLength;
    @Positive
    private Long maxLength;
    @NotEmpty
    private Character[] chars;
    @OneToMany
    private Set<GeneratedString> generatedStrings;
    @Positive
    private Long jobSize;
    @NotNull
    private Boolean isDone;

    public Job(JobCreationRequestDTO creationRequest){
        this.minLength = creationRequest.getMinLength();
        this.maxLength = creationRequest.getMaxLength();
        this.chars = creationRequest.getChars();
        this.jobSize = creationRequest.getJobSize();
        this.isDone = false;
        this.generatedStrings = new HashSet<>();
    }
}
