package io.github.alberes.register.manager.frontend.services;

import feign.Response;
import io.github.alberes.register.manager.frontend.controllers.dto.LoginDto;
import io.github.alberes.register.manager.frontend.controllers.dto.TokenDto;
import io.github.alberes.register.manager.frontend.services.exceptions.RegisterManagerErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "loginregistermanagerapi", url = "${registermanager.url}", configuration = RegisterManagerErrorDecoder.class)
public interface LoginService {

    @PostMapping("login")
    public Response login(@RequestBody LoginDto dto);

}
