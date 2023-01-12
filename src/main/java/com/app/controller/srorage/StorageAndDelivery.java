package com.app.controller.srorage;

import com.app.DTO.StorageDTO;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public abstract class StorageAndDelivery {

    Stage stage;
    StorageDTO storageDTO;
    ArrayList<String> list;
    ArrayList<StorageDTO> listStorage;
    String choose;

    private ComboBox<String> boxItems;

    public void setList(ArrayList<StorageDTO> list) {
        this.listStorage=new ArrayList<>();
        this.list=new ArrayList<>();
        listStorage=list;
        for (int i = 0; i <list.size() ; i++) {
            this.list.add(list.get(i).getName());
        }
    }
    public StorageDTO findStorage()
    {
        for (int i = 0; i < listStorage.size(); i++) {
            if(listStorage.get(i).getName().equals(choose))
            {
                return listStorage.get(i);
            }

        }
        return null;
    }
    public void filingBox()
    {
        boxItems.getItems().addAll(list);
    }

    public StorageDTO getStorageDTO()
    {
        return  storageDTO;
    }

    public void setStage(Stage stage)
    {
        this.stage=stage;
    }





}
