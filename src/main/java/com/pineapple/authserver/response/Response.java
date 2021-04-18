package com.pineapple.authserver.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
    private int count;
    private T data;
}
