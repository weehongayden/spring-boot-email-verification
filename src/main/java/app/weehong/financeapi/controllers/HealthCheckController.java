package app.weehong.financeapi.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/health-check")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<Void> healthCheck() {
        log.info("healthCheck() called");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
