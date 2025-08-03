package com.algostyle.backend.model.dto.userprofile;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // Méthodes utilitaires pour faciliter la création
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, "Opération réussie",data);
    }
    public static <T> ApiResponse<T> success(String msg, T data){
        return new ApiResponse<>(true, msg,data);
    }
    public static <T> ApiResponse<T> error(String msg){
        return new ApiResponse<>(false, msg, null);
    }

}
