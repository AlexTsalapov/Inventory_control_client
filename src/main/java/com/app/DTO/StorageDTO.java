package com.app.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
@Data
@AllArgsConstructor
@NoArgsConstructor

public class StorageDTO {

    private int id;
    private String name;

    private boolean type;
    private UserDTO user;

    @JsonIgnore
    private List<ProductDTO> products=new ArrayList<>();

    public void addProduct(ProductDTO productDTO)
    {
        products.add(productDTO);
    }


}
