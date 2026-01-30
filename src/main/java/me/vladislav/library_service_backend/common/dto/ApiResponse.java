package me.vladislav.library_service_backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private Status status;
    private String message;
    private Object data;
    private List<String> errors;

    public enum Status {
        SUCCESS,
        ERROR
    }

    public static ApiResponse success(String message, Object data) {
        ApiResponse response = new ApiResponse();
        response.status = Status.SUCCESS;
        response.message = message;
        response.data = data;
        return response;
    }

    public static ApiResponse error(String message, List<String> errors) {
        ApiResponse response = new ApiResponse();
        response.status = Status.ERROR;
        response.message = message;
        response.errors = errors;
        return response;
    }

}
