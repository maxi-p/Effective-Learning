package com.maxip.filestore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ModuleResponse
{
    private Long id;
    private String name;
    private List<File> files;
}
