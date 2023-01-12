package com.app.DTO;



import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    protected int id;
    private String login;
    private String password;
    private List<StorageDTO> storages=new ArrayList<>();
    public void addStorage(StorageDTO storageDTO)
    {
        storages.add(storageDTO);
    }
}
