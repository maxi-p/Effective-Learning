package com.maxip.filestore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchHint
{
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String value;
}
