package com.maxip.notetestservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoteDto
{
    private Long id;
    private String key;
    private String value;
    private Long userId;
    private CategoryDto categoryDto;
}
