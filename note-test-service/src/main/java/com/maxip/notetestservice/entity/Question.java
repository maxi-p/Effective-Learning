package com.maxip.notetestservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String question;
    private String answer;
}
