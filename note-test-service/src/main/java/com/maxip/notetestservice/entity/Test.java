package com.maxip.notetestservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Test
{
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Question> questions;
    private Integer numberOfQuestions;
}
