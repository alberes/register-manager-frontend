package io.github.alberes.register.manager.frontend.controllers.exceptions;

import java.io.Serializable;
import java.util.List;

public class StandardErrorDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long timestamp;

    private Integer status;

    private String error;

    private String message;

    private String path;

    private List<FieldErroDto> fields;

    public StandardErrorDto() {
    }

    public StandardErrorDto(Long timestamp, Integer status, String error, String message, String path, List<FieldErroDto> fields) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fields = fields;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FieldErroDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldErroDto> fields) {
        this.fields = fields;
    }
}