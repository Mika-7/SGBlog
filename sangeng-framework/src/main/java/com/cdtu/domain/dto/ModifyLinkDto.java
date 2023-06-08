package com.cdtu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyLinkDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String logo;
    private String status;
}
