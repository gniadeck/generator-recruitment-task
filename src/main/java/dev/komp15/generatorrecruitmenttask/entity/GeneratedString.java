package dev.komp15.generatorrecruitmenttask.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class GeneratedString {
    @Id
    @GeneratedValue
    private Long id;
    private String data;

    public GeneratedString(String data) {
        this.data = data;
    }
}
