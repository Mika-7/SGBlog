package com.cdtu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCategory {
    private String name;
    private String status;
    private String description;
    private Long id;
}
