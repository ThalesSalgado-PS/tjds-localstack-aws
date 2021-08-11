package com.tjds.localstackaws.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonSqsDTO {

    private String document;
    private String name;
    private String occupation;
    private BigDecimal salary;

}
