package com.maxip.notetestservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result
{
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String testName;
    private String encodedResult;
    private Integer questionNumber;
    private Integer correctQuestionNumber;
    private Long testId;
}
