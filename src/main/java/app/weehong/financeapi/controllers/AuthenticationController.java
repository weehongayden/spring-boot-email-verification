package app.weehong.financeapi.controllers;

import app.weehong.financeapi.dtos.requests.UserRequestDto;
import app.weehong.financeapi.events.RegistrationCompleteEvent;
import app.weehong.financeapi.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, ApplicationEventPublisher applicationEventPublisher) {
        this.authenticationService = authenticationService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping(value = "register")
    public ResponseEntity<Void> register(@RequestBody UserRequestDto userRequestDto, final HttpServletRequest httpServletRequest) {
        log.info("register() called");
        authenticationService.register(userRequestDto);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(userRequestDto, applicationUrl(httpServletRequest)));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    public String verify(@RequestParam("token") String token) {
        log.info("verify() called");
        boolean res = authenticationService.verify(token);
        return "Your account has been activated successfully";
    }

    public String applicationUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/v1" + request.getContextPath();
    }
}
