package com.maxip.notetestservice.entity;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestOrder
{
    private Long userId;
    private Integer questionNumber;
    private List<Long> categories;
}
