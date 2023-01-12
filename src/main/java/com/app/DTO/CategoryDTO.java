package com.app.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private int id;
    private String name;
    private int storageId;
}
