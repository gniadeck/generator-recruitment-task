package dev.komp15.generatorrecruitmenttask.entity;

import dev.komp15.generatorrecruitmenttask.dto.JobCreationRequestDTO;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @CreationTimestamp
    private LocalDateTime submitTime;
    private LocalDateTime finishedTime;
    @Positive
    private Long minLength;
    @Positive
    private Long maxLength;
    @NotEmpty
    private Character[] chars;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @ToString.Exclude
    private Set<GeneratedString> generatedStrings;
    @Positive
    private Long jobSize;
    @NotNull
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public Job(JobCreationRequestDTO creationRequest){
        this.minLength = creationRequest.getMinLength();
        this.maxLength = creationRequest.getMaxLength();
        this.chars = creationRequest.getChars();
        this.jobSize = creationRequest.getJobSize();
        this.status = JobStatus.EXECUTING;
        this.generatedStrings = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Job job = (Job) o;
        return id != null && Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
