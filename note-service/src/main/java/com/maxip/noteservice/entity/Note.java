package com.maxip.noteservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Note
{
    @Id
    @GeneratedValue
    private Long id;
    private String key;
    private String value;
    private Long userId;
    @ManyToOne(fetch = FetchType.EAGER)
    private NoteCategory noteCategory;
}
