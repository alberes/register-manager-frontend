package io.github.alberes.register.manager.frontend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import io.github.alberes.register.manager.frontend.constants.MessageConstants;
import io.github.alberes.register.manager.frontend.controllers.dto.UserSessionDto;
import io.github.alberes.register.manager.frontend.controllers.exceptions.FieldErroDto;
import io.github.alberes.register.manager.frontend.controllers.exceptions.StandardErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

public abstract class GenericController {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MessageSource messageSource;


    public boolean isInvalidSession(Model model){
        model.addAttribute(MessageConstants.SESSIONTIMEOUT, this.getMessageSource(MessageConstants.SESSION_TIMEOUT));
        return model.getAttribute(MessageConstants.USER_SESSION) == null;
    }

    public String createBearerToken(Model model){
        UserSessionDto userSessionDto = (UserSessionDto)model.getAttribute(MessageConstants.USER_SESSION);
        return MessageConstants.BEARER + userSessionDto.getToken().token();
    }

    public URI createURI(Response response){
        URI uri = null;
        Collection<String> header = response.headers().get(MessageConstants.LOCATION);
        String location = "";
        for(String l : header){
            location = l;
        }
        try {
            uri = new URI(location);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }

    public StandardErrorDto toStandardErrorDto(Response response){
        try {
            StandardErrorDto standardErrorDto = this.objectMapper.readValue(response.body().asInputStream(), StandardErrorDto.class);
            return standardErrorDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMessages(Model model, Response response){
        StandardErrorDto standardErrorDto = this.toStandardErrorDto(response);
        model.addAttribute(MessageConstants.ERROR, standardErrorDto);
        if (!standardErrorDto.getFields().isEmpty()) {
            for (FieldErroDto f : standardErrorDto.getFields()) {
                model.addAttribute(f.field(), f.message());
            }
        }
    }

    public String extractId(Response response){
        URI uri = this.createURI(response);
        String[] path = uri.getPath().split(MessageConstants.SLASH);
        String id = path[path.length - 1];
        return id;
    }

    public String getMessageSource(String messageId){
        return this.messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale());
    }
}
