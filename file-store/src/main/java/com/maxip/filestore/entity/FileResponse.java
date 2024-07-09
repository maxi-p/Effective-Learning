package com.maxip.filestore.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileResponse
{
    private Long id;
    private String name;
    private Integer numberOfPages;
    private Integer resultPage;
    private Long moduleId;
    private Long subjectId;
}
