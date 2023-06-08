package com.cdtu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserDto {
    private String userName;
    private String phonenumber;
    private String status;
}
