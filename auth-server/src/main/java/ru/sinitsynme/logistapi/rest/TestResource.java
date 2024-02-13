package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.logistapi.config.annotations.AdminAccess;
import ru.sinitsynme.logistapi.config.tasks.ClearRefreshTokenRepositoryTask;

@Tag(name = "Таск-адаптеры")
@RestController
@RequestMapping("/test")
@SecurityRequirement(name = "Bearer Authentication")
@Profile("dev")
public class TestResource {

    private final ClearRefreshTokenRepositoryTask task;

    public TestResource(ClearRefreshTokenRepositoryTask task) {
        this.task = task;
    }

    @Operation(summary = "Удалить просроченные refresh-токены")
    @GetMapping("/clearRefreshTokens")
    @AdminAccess
    public ResponseEntity<?> clearRefreshTokens() {
        task.clearRefreshTokenRepository();
        return ResponseEntity.ok().build();
    }
}