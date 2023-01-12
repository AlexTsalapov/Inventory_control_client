package com.app.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private RequestType requestType;
    private String requestMessage;

    public enum RequestType {
        Authorization,
        Registration,
        AddOrder,

        CreateStorage,
        GetProducts,
        GetCategory,
        CreateOrder,

        UpdateStorage,
        DeleteStorage,
        CreateProduct,
        DeleteProduct,
        UpdateProduct,
        CreateCategory,
        DeleteCategory,
        UpdateCategory,
        Exit
    }
}
