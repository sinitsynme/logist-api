package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.FeatureSetting;
import ru.sinitsynme.logistapi.rest.dto.FeatureSettingDto;
import ru.sinitsynme.logistapi.service.FeatureSettingService;

import java.util.List;

@Tag(name = "Управление feature-тогглами")
@RestController
@RequestMapping("/rest/api/v1/feature-setting")
@SecurityRequirement(name = "Bearer Authentication")
public class FeatureSettingResource {

    private final FeatureSettingService featureSettingService;

    @Autowired
    public FeatureSettingResource(FeatureSettingService featureSettingService) {
        this.featureSettingService = featureSettingService;
    }

    @GetMapping
    public ResponseEntity<List<FeatureSetting>> getFeatureSettings() {
        return ResponseEntity.ok(
                featureSettingService.getFeatureSettings()
        );
    }

    @PostMapping
    public ResponseEntity<FeatureSetting> saveFeatureSetting(
            @RequestBody FeatureSettingDto featureSettingDto
    ) {
        return ResponseEntity.ok(
                featureSettingService.saveFeatureSetting(featureSettingDto)
        );
    }

    @PutMapping("/{name}")
    public ResponseEntity<FeatureSetting> updateFeatureSetting(
            @RequestBody FeatureSettingDto featureSettingDto,
            @PathVariable("name") String name
    ) {
        return ResponseEntity.ok(
                featureSettingService.updateFeatureSetting(featureSettingDto, name)
        );
    }

}
