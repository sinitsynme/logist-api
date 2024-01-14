package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.mapper.ManufacturerMapper;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerResponseDto;
import ru.sinitsynme.logistapi.service.ManufacturerService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Управление производителями")
@RestController
@RequestMapping("/rest/api/v1/manufacturer")
public class ManufacturerResource {

    private final ManufacturerMapper manufacturerMapper;
    private final ManufacturerService manufacturerService;
    private final Logger logger = LoggerFactory.getLogger(ManufacturerResource.class);


    public ManufacturerResource(ManufacturerMapper manufacturerMapper, ManufacturerService manufacturerService) {
        this.manufacturerMapper = manufacturerMapper;
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить производителя по ID")
    public ResponseEntity<ManufacturerResponseDto> findManufacturerById(
            @PathVariable("id") Long manufacturerId) {
        Manufacturer manufacturer = manufacturerService.getManufacturerById(manufacturerId);
        return ResponseEntity.ok(manufacturerMapper.toResponseDto(manufacturer));
    }

    @GetMapping
    @Operation(summary = "Получить список производителей")
    public ResponseEntity<List<ManufacturerResponseDto>> findPageOfManufacturer(
            @RequestParam @Valid @Min(1) int size,
            @RequestParam @Valid @Min(0) int page) {
        List<Manufacturer> manufacturersList = manufacturerService.getManufacturerPage(size, page);
        return ResponseEntity.ok(
                manufacturersList
                        .stream()
                        .map(manufacturerMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    @Operation(summary = "Создать производителя")
    public ResponseEntity<ManufacturerResponseDto> createManufacturer(
            @RequestBody @Valid ManufacturerRequestDto requestDto) {
        Manufacturer manufacturer = manufacturerService.saveManufacturer(requestDto);
        logger.info("Manufactuter {} created", manufacturer);
        return ResponseEntity.ok(manufacturerMapper.toResponseDto(manufacturer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать производителя по ID")
    public ResponseEntity<ManufacturerResponseDto> editManufacturerById(
            @RequestBody @Valid ManufacturerRequestDto requestDto,
            @PathVariable("id") Long id) {
        Manufacturer manufacturer = manufacturerService.editManufacturer(requestDto, id);
        logger.info("Manufactuter {} edited", manufacturer);
        return ResponseEntity.ok(manufacturerMapper.toResponseDto(manufacturer));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить производителя")
    public ResponseEntity<?> deleteManufacturerById(
            @PathVariable("id") long manufacturerId
    ) {
        manufacturerService.deleteManufacturer(manufacturerId);
        logger.info("Manufactuter with ID = {} deleted", manufacturerId);
        return ResponseEntity.ok().build();
    }
}
