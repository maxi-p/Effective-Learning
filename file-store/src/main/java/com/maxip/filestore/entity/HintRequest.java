package com.maxip.filestore.entity;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HintRequest
{
    private String fileName;
    private String moduleId;
    private String subjectId;
    private Integer pageNumber;
    private List<String> hints;
}
