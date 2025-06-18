package io.github.alberes.register.manager.frontend.controllers;

import feign.Response;
import io.github.alberes.register.manager.frontend.constants.Constants;
import io.github.alberes.register.manager.frontend.controllers.dto.AddressDto;
import io.github.alberes.register.manager.frontend.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.frontend.controllers.dto.UserSessionDto;
import io.github.alberes.register.manager.frontend.controllers.dto.page.PageReport;
import io.github.alberes.register.manager.frontend.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Controller
@SessionAttributes(Constants.USER_SESSION)
public class AddressRegisterManagerController extends GenericController{

    @Autowired
    private AddressService addressService;

    @GetMapping("/login-address")
    public String home(Model model){
        return Constants.LOGIN;
    }

    @GetMapping("/addresses/{userId}")
    public String addresses(
            @PathVariable String userId,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                        @RequestParam(value = "orderBy", defaultValue = "publicArea") String orderBy,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        Model model){
        if(this.isInvalidSession(model)){
            return this.home(model);
        }
        PageReport<AddressReportDto> addresses = this.addressService.addresses(this.createBearerToken(model), userId, page, linesPerPage, orderBy, direction);
        if(addresses == null){
            addresses = new PageReport<AddressReportDto>();
            addresses.setContent(new ArrayList<AddressReportDto>());
        }
        model.addAttribute(Constants.ADDRESSES, addresses);
        model.addAttribute(Constants.USERID, userId);
        return Constants.LIST_ADDRESS;
    }

    @GetMapping("new-address/{userId}")
    public String newAddress(@PathVariable String userId, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        Object address = model.getAttribute(Constants.ADDRESS);
        if(address == null) {
            AddressDto dto = new AddressDto();
            dto.setUserId(userId);
            dto.setNewRegister(true);
            model.addAttribute(Constants.ADDRESS, dto);        }
        return Constants.NEW_ADDRESS;
    }

    @PostMapping("save-address/{userId}")
    public String saveAddress(@PathVariable String userId, @RequestParam boolean isNewRegister, AddressDto dto, Model model){
        AddressDto addressDto = null;
        boolean successSearchAddress = true;
        if(this.isInvalidSession(model)){
            return home(model);
        }
        UserSessionDto userSession = (UserSessionDto)model.getAttribute(Constants.USER_SESSION);
        if(isNewRegister){
            try {
                addressDto = this.addressService.searchZipcode(this.createBearerToken(model), userId, dto.getZipCode());
            }catch (ResponseStatusException responseStatusException){
                successSearchAddress = false;
                responseStatusException.printStackTrace();
            }
            if(successSearchAddress){
                addressDto.setNewRegister(false);
                addressDto.setUserId(userId);
                addressDto.setZipCode(dto.getZipCode());
                userSession.getCache().put(Constants.ZIP_CODE, addressDto.getZipCode());
                model.addAttribute(Constants.ADDRESS, addressDto);
            }else {
                addressDto = new AddressDto();
                addressDto.setNewRegister(true);
                addressDto.setZipCode(dto.getZipCode());
                successSearchAddress = false;
                model.addAttribute(Constants.ADDRESS, addressDto);
                model.addAttribute(Constants.ERRORMESSAGE, this.getMessageSource(Constants.ADDRESS_NOT_FIND));
            }
            return newAddress(userId, model);
        }else{
            String zipCode = (String) userSession.getCache().get(Constants.ZIP_CODE);
            if(!dto.getZipCode().equals(zipCode)){
                model.addAttribute(Constants.ERRORMESSAGE, this.getMessageSource(Constants.ZIPCODE_CHANGED));
                return newAddress(userId, model);
            }
            Response response = this.addressService.save(this.createBearerToken(model), userId,  dto);
            if(response.status() == HttpStatus.CREATED.value()){
                String id = this.extractId(response);
                dto.setId(id);
                model.addAttribute(Constants.MESSAGE, this.getMessageSource(Constants.SUCCESS_MESSAGE));
                model.addAttribute(Constants.ADDRESS, dto);
                userSession.getCache().remove(Constants.ZIP_CODE);
                return Constants.EDIT_ADDRESS;
            }else{
                this.createMessages(model, response);
                model.addAttribute(Constants.ADDRESS, dto);
                return newAddress(userId, model);
            }
        }
    }

    @PostMapping("update-address/{userId}/{addressId}")
    public String updateAddress(@PathVariable String userId, @PathVariable String addressId,
                                AddressDto dto, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        dto.setId(addressId);
        Response response = this.addressService.update(this.createBearerToken(model), userId, addressId, dto);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            model.addAttribute(Constants.MESSAGE, this.getMessageSource(Constants.SUCCESS_MESSAGE));
        }else {
            this.createMessages(model, response);

        }
        model.addAttribute(Constants.ADDRESS, dto);
        return Constants.EDIT_ADDRESS;

    }

    @GetMapping("edit-address/{userId}/{addressId}")
    public String editAddress(@PathVariable String userId, @PathVariable String addressId, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        AddressDto addressDto = this.addressService.find(this.createBearerToken(model), userId, addressId);
        addressDto.setUserId(userId);
        model.addAttribute(Constants.ADDRESS, addressDto);
        return Constants.EDIT_ADDRESS;
    }

    @GetMapping("delete-address/{userId}/{addressId}")
    public String deleteAddress(@PathVariable String userId, @PathVariable String addressId, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        Response response = this.addressService.delete(this.createBearerToken(model), userId, addressId);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            return this.addresses(userId, 0, 24, Constants.PUBLICAREA, Constants.ASC, model);
        }else{
            model.addAttribute(Constants.ERRORMESSAGE, this.getMessageSource(Constants.CONTACT_ADMINISTRATOR));
            return Constants.ERROR;
        }
    }
}
