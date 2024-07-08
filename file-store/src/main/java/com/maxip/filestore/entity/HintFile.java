package com.maxip.filestore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"pageNumber", "file_id", "search_hint_id"})})
public class HintFile
{
    @Id
    @GeneratedValue
    private Long Id;

    private Integer pageNumber;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne
    @JoinColumn(name = "search_hint_id")
    private SearchHint searchHint;
}
