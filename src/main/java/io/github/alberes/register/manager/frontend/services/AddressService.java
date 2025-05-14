package io.github.alberes.register.manager.frontend.services;

import feign.Response;
import io.github.alberes.register.manager.frontend.controllers.dto.AddressDto;
import io.github.alberes.register.manager.frontend.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.frontend.controllers.dto.page.PageReport;
import io.github.alberes.register.manager.frontend.services.exceptions.RegisterManagerErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "addressregistermanagerapi", url = "${registermanager.url}", configuration = RegisterManagerErrorDecoder.class)
public interface AddressService {

    @GetMapping("users/{userId}/addresses/zipcode/{zipCode}")
    public AddressDto searchZipcode(@RequestHeader("Authorization") String token, @PathVariable String userId, @PathVariable String zipCode);

    @PostMapping("users/{userId}/addresses")
    public Response save(@RequestHeader("Authorization") String token, @PathVariable String userId, @RequestBody AddressDto dto);

    @GetMapping("users/{userId}/addresses/{addressId}")
    public AddressDto find(@RequestHeader("Authorization") String token, @PathVariable String userId, @PathVariable String addressId);

    @PutMapping("users/{userId}/addresses/{addressId}")
    public Response update(@RequestHeader("Authorization") String token, @PathVariable String userId, @PathVariable String addressId,
                           @RequestBody AddressDto dto);

    @DeleteMapping("users/{userId}/addresses/{addressId}")
    public Response delete(@RequestHeader("Authorization") String token, @PathVariable String userId, @PathVariable String addressId);

    @GetMapping("users/{userId}/addresses")
    public PageReport<AddressReportDto> addresses(@RequestHeader("Authorization") String token,
                                                  @PathVariable String userId,
                                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                  @RequestParam(value = "orderBy", defaultValue = "publicArea") String orderBy,
                                                  @RequestParam(value = "direction", defaultValue = "ASC") String direction);


}
