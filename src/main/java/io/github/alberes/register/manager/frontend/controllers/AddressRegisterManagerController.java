package io.github.alberes.register.manager.frontend.controllers;

import feign.Response;
import io.github.alberes.register.manager.frontend.controllers.dto.AddressDto;
import io.github.alberes.register.manager.frontend.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.frontend.controllers.dto.page.PageReport;
import io.github.alberes.register.manager.frontend.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@SessionAttributes("userSession")
public class AddressRegisterManagerController extends GenericController{

    @Autowired
    private AddressService addressService;

    @GetMapping("/addresses/{userId}")
    public String addresses(
            @PathVariable String userId,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                        @RequestParam(value = "orderBy", defaultValue = "publicArea") String orderBy,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        Model model){
        PageReport<AddressReportDto> addresses = this.addressService.addresses(this.createBearerToken(model), userId, page, linesPerPage, orderBy, direction);
        if(addresses == null){
            addresses = new PageReport<AddressReportDto>();
            addresses.setContent(new ArrayList<AddressReportDto>());
        }
        model.addAttribute("addresses", addresses);
        model.addAttribute("userId", userId);
        return "list-address";
    }

    @GetMapping("new-address/{userId}")
    public String newAddress(@PathVariable String userId, Model model){
        Object address = model.getAttribute("address");
        if(address == null) {
            AddressDto dto = new AddressDto();
            dto.setUserId(userId);
            dto.setNewRegister(true);
            model.addAttribute("address", dto);        }
        return "new-address";
    }

    @PostMapping("save-address/{userId}")
    public String saveAddress(@PathVariable String userId, @RequestParam boolean isNewRegister, AddressDto dto, Model model){
        if(isNewRegister){
            AddressDto addressDto = this.addressService.searchZipcode(this.createBearerToken(model), userId, dto.getZipCode());
            addressDto.setZipCode(dto.getZipCode());
            if(addressDto.getPublicArea() == null){
                model.addAttribute("errorMessage", "Endereço não encontrado!");
            }else {
                addressDto.setNewRegister(false);
                addressDto.setUserId(userId);
                model.addAttribute("address", addressDto);
            }
            return newAddress(userId, model);
        }else{
            Response response = this.addressService.save(this.createBearerToken(model), userId,  dto);
            if(response.status() == HttpStatus.CREATED.value()){
                String id = this.extractId(response);
                dto.setId(id);
                model.addAttribute("message", "Cadastro criado com sucess!");
                model.addAttribute("address", dto);
                return "edit-address";
            }else{
                this.createMessages(model, response);
                model.addAttribute("address", dto);
                return newAddress(userId, model);
            }
        }
    }

    @PostMapping("update-address/{userId}/{addressId}")
    public String updateAddress(@PathVariable String userId, @PathVariable String addressId,
                                AddressDto dto, Model model){
        Response response = this.addressService.update(this.createBearerToken(model), userId, addressId, dto);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            model.addAttribute("message", "Atualizado com sucesso.");
        }else {
            this.createMessages(model, response);

        }
        model.addAttribute("address", dto);
        return "edit-address";

    }

    @GetMapping("edit-address/{userId}/{addressId}")
    public String editAddress(@PathVariable String userId, @PathVariable String addressId, Model model){
        AddressDto addressDto = this.addressService.find(this.createBearerToken(model), userId, addressId);
        addressDto.setUserId(userId);
        model.addAttribute("address", addressDto);
        return "edit-address";
    }

    @GetMapping("delete-address/{userId}/{addressId}")
    public String deleteAddress(@PathVariable String userId, @PathVariable String addressId, Model model){
        Response response = this.addressService.delete(this.createBearerToken(model), userId, addressId);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            return this.addresses(userId, 0, 24, "publicArea", "ASC", model);
        }else{
            model.addAttribute("message", "Entre em contato com administrador.");
            return "error";
        }
    }
}
