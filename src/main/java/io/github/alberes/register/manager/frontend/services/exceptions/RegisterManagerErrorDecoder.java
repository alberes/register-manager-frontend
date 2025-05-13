package io.github.alberes.register.manager.frontend.services.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class RegisterManagerErrorDecoder implements ErrorDecoder{

    private final ErrorDecoder errorDecoder = new ErrorDecoder.Default();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseStatusException decode(String methodKey, Response response) {
        String body = null;

        try{
            if(response.body() != null){
                body = objectMapper.readTree(response.body().asInputStream()).toString();
            }
        }catch (IOException e) {
            body = e.getMessage();
        }

        return new ResponseStatusException(
                HttpStatus.valueOf(response.status()),
                body);
    }
}
