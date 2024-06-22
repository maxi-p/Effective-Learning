package com.maxip.noteservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoteCategory
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
