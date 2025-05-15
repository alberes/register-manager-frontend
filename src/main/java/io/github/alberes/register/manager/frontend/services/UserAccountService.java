package io.github.alberes.register.manager.frontend.services;

import feign.Response;
import io.github.alberes.register.manager.frontend.controllers.dto.UserAccountDto;
import io.github.alberes.register.manager.frontend.controllers.dto.UserAccountProfileDto;
import io.github.alberes.register.manager.frontend.controllers.dto.UserAccountReportDto;
import io.github.alberes.register.manager.frontend.controllers.dto.UserAccountUpdateDto;
import io.github.alberes.register.manager.frontend.controllers.dto.page.PageReport;
import io.github.alberes.register.manager.frontend.services.exceptions.RegisterManagerErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "useraccoutregistermanagerapi", url = "${registermanager.url}", configuration = RegisterManagerErrorDecoder.class)
public interface UserAccountService {

    @PostMapping("users")
    public Response save(@RequestHeader("Authorization") String token, @RequestBody UserAccountDto dto);

    @GetMapping("users/{id}")
    public UserAccountDto find(@RequestHeader("Authorization") String token, @PathVariable String id);

    @GetMapping("users/authenticated")
    public UserAccountProfileDto authenticated(@RequestHeader("Authorization") String token);

    @PutMapping("users/{id}")
    public Response update(@RequestHeader("Authorization") String token,
                           @PathVariable String id, @RequestBody UserAccountUpdateDto dto);

    @DeleteMapping("users/{id}")
    public Response delete(@RequestHeader("Authorization") String token, @PathVariable String id);

    @GetMapping("users/{id}/all")
    public PageReport<UserAccountReportDto> allUsers(@RequestHeader("Authorization") String token,
                                                     @PathVariable String id,
                                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                     @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
                                                     @RequestParam(value = "direction", defaultValue = "ASC") String direction);

}
