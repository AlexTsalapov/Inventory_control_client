package com.app.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private int id;
    private String note;
    private String name;
    private double price;
    private int amount;
    private String dateOfManufacture;
    private String dateOfExpiration;
    private String category;
    private StorageDTO storage;
}
