package dev.komp15.generatorrecruitmenttask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class GeneratedString {
    @Id
    @GeneratedValue
    private Long id;
    private String data;
    @JsonIgnore
    @ManyToOne
    private Job job;

    public GeneratedString(String data, Job job) {
        this.data = data;
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GeneratedString that = (GeneratedString) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
