package io.github.alberes.register.manager.frontend.controllers;

import feign.Response;
import io.github.alberes.register.manager.frontend.constants.MessageConstants;
import io.github.alberes.register.manager.frontend.controllers.dto.*;
import io.github.alberes.register.manager.frontend.controllers.dto.page.PageReport;
import io.github.alberes.register.manager.frontend.services.LoginService;
import io.github.alberes.register.manager.frontend.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@SessionAttributes(MessageConstants.USER_SESSION)
public class UserAccountRegisterManagerController extends GenericController{

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/")
    public String index(Model model){
        return MessageConstants.LOGIN;
    }

    @GetMapping("/login")
    public String home(Model model){
        return MessageConstants.LOGIN;
    }

    @PostMapping("/login-user")
    public String login(@ModelAttribute LoginDto loginDto, Model model){
        Response response = this.loginService.login(loginDto);
        if(response.status() == HttpStatus.OK.value()) {
            try {
                TokenDto token = null;
                token = this.objectMapper.readValue(response.body().asInputStream(), TokenDto.class);
                UserAccountProfileDto userAccountProfileDto = this.userAccountService.authenticated("Bearer " + token.token());
                UserSessionDto userSessionDto = new UserSessionDto(token, userAccountProfileDto);
                boolean admin = userSessionDto.isAdmin();
                model.addAttribute(MessageConstants.USER_SESSION, userSessionDto);
                return users(0, 24, MessageConstants.NAME, MessageConstants.ASC, model);
            } catch (IOException e) {
                e.printStackTrace();
                return home(model);
            }
        }else{
            model.addAttribute(MessageConstants.ERRORMESSAGE, this.getMessageSource(MessageConstants.INVALID_LOGIN));
            return home(model);
        }
    }

    @GetMapping("/new-user")
    public String newUser(Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        Object user = model.getAttribute(MessageConstants.USER);
        if(user == null) {
            model.addAttribute(MessageConstants.USER, new UserAccountDto("", "", "", "", ""));
        }
        return MessageConstants.NEW_USER;
    }

    @PostMapping("/save-user")
    public String saveUser(UserAccountDto dto, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        UserSessionDto userSessionDto = (UserSessionDto)model.getAttribute(MessageConstants.USER_SESSION);
        Response response = this.userAccountService.save(this.createBearerToken(model), dto);
        if(response.status() == HttpStatus.CREATED.value()) {
            String id = this.extractId(response);
            return this.editUser(id, model);
        }else{
            if(response.status() == HttpStatus.CONFLICT.value()){
                model.addAttribute(MessageConstants.USEREMAILCONFLIT, this.getMessageSource(MessageConstants.USER_EMAIL_CONFLIT));
            }else {
                this.createMessages(model, response);
            }
            model.addAttribute(MessageConstants.USER_SESSION, dto);
            return this.newUser(model);
        }
    }

    @GetMapping("/users")
    public String users(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                        @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        UserSessionDto userSessionDto = (UserSessionDto)model.getAttribute(MessageConstants.USER_SESSION);
        PageReport<UserAccountReportDto> users = this.userAccountService.allUsers(
                this.createBearerToken(model),
                userSessionDto.getUserAccountProfileDto().getId(), page, linesPerPage, orderBy, direction);
        model.addAttribute(MessageConstants.USERS, users);
        return MessageConstants.LIST_USER;
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable String id, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        Response response = this.userAccountService.delete(this.createBearerToken(model), id);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            UserSessionDto userSessionDto = (UserSessionDto)model.getAttribute(MessageConstants.USER_SESSION);
            //Owner user deleted hisself
            if(id.equals(userSessionDto.getUserAccountProfileDto().getId())){
                return this.home(model);
            }
            return users(0, 24, MessageConstants.NAME, MessageConstants.ASC, model);
        }else{
            model.addAttribute(MessageConstants.ERRORMESSAGE, this.getMessageSource(MessageConstants.CONTACT_ADMINISTRATOR));
            return MessageConstants.ERROR;
        }
    }

    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable String id, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        UserAccountDto userAccountDto = this.userAccountService.find(this.createBearerToken(model), id);
        model.addAttribute(MessageConstants.USER, userAccountDto);
        return MessageConstants.EDIT_USER;
    }

    @PostMapping("/update-user/{id}")
    public String updateUser(@PathVariable String id, UserAccountUpdateDto dto, Model model){
        if(this.isInvalidSession(model)){
            return home(model);
        }
        Response response = this.userAccountService.update(this.createBearerToken(model), id, dto);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            model.addAttribute(MessageConstants.MESSAGE, this.getMessageSource(MessageConstants.SUCCESS_MESSAGE));
            return this.editUser(id, model);
        }else if(response.status() == HttpStatus.BAD_REQUEST.value()){
            model.addAttribute(MessageConstants.ERRORMESSAGE, this.getMessageSource(MessageConstants.ERROR_MESSAGE));
            this.createMessages(model, response);
            return this.editUser(id, model);
        }else{
            model.addAttribute(MessageConstants.ERRORMESSAGE, this.getMessageSource(MessageConstants.CONTACT_ADMINISTRATOR));
            return MessageConstants.ERROR;
        }
    }
}