package io.github.alberes.register.manager.frontend.controllers;

import feign.Response;
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
@SessionAttributes("userSession")
public class UserAccountRegisterManagerController extends GenericController{

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/")
    public String index(Model model){
        return "login";
    }

    @GetMapping("/login")
    public String home(Model model){
        return "login";
    }

    @PostMapping("/login-user")
    public String login(@ModelAttribute LoginDto loginDto, Model model){
        Response response = this.loginService.login(loginDto);
        if(response.status() == HttpStatus.OK.value()) {
            try {
                TokenDto token = null;
                token = this.objectMapper.readValue(response.body().asInputStream(), TokenDto.class);
                UserSessionDto userSessionDto = new UserSessionDto(token);
                model.addAttribute("userSession", userSessionDto);
                return users(0, 24, "name", "ASC", model);
            } catch (IOException e) {
                e.printStackTrace();
                return home(model);
            }
        }else{
            return home(model);
        }
    }

    @GetMapping("/new-user")
    public String newUser(Model model){
        Object user = model.getAttribute("user");
        if(user == null) {
            model.addAttribute("user", new UserAccountDto("", "", "", "", ""));
        }
        return "new-user";
    }

    @PostMapping("/save-user")
    public String saveUser(UserAccountDto dto, Model model){
        UserSessionDto userSessionDto = (UserSessionDto)model.getAttribute("userSession");
        Response response = this.userAccountService.save(this.createBearerToken(model), dto);
        if(response.status() == HttpStatus.CREATED.value()) {
            String id = this.extractId(response);
            return this.editUser(id, model);
        }else{
            if(response.status() == HttpStatus.CONFLICT.value()){
                model.addAttribute("email", "Email já cadastrado!");
            }else {
                this.createMessages(model, response);
            }
            model.addAttribute("user", dto);
            return this.newUser(model);
        }
    }

    @GetMapping("/users")
    public String users(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                        @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        Model model){
        UserSessionDto userSessionDto = (UserSessionDto)model.getAttribute("userSession");
        PageReport<UserAccountReportDto> users = this.userAccountService.allUsers(
                this.createBearerToken(model),
                userSessionDto.getToken().id(), page, linesPerPage, orderBy, direction);
        model.addAttribute("users", users);
        return "list-user";
    }


    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable String id, Model model){
        Response response = this.userAccountService.delete(this.createBearerToken(model), id);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            return users(0, 24, "name", "ASC", model);
        }else{
            model.addAttribute("message", "Entre em contato com administrador.");
            return "error";
        }
    }

    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable String id, Model model){
        UserAccountDto userAccountDto = this.userAccountService.find(this.createBearerToken(model), id);
        model.addAttribute("user", userAccountDto);
        return "edit-user";
    }

    @PostMapping("/update-user/{id}")
    public String updateUser(@PathVariable String id, UserAccountUpdateDto dto, Model model){
        Response response = this.userAccountService.update(this.createBearerToken(model), id, dto);
        if(response.status() == HttpStatus.NO_CONTENT.value()){
            model.addAttribute("message", "Atualizado com sucesso.");
            return this.editUser(id, model);
        }else{
            model.addAttribute("message", "Entre em contato com administrador.");
            return "error";
        }
    }
}