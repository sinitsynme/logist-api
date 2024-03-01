package ru.sinitsynme.logistapi.rest;

import dto.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.mapper.ManufacturerMapper;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerRequestDto;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerResponseDto;
import ru.sinitsynme.logistapi.service.ManufacturerService;
import security.annotations.AdminAccess;

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
    @Operation(summary = "Получить страницу производителей")
    public ResponseEntity<Page<ManufacturerResponseDto>> findPageOfManufacturer(
            @Valid PageRequestDto pageRequestDto) {

        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);

        Page<Manufacturer> manufacturersList = manufacturerService.getManufacturerPage(pageRequestDto.toPageable());
        return ResponseEntity.ok(
                manufacturersList
                        .map(manufacturerMapper::toResponseDto)
        );
    }

    @PostMapping
    @Operation(summary = "Создать производителя")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ManufacturerResponseDto> createManufacturer(
            @RequestBody @Valid ManufacturerRequestDto requestDto) {
        Manufacturer manufacturer = manufacturerService.saveManufacturer(requestDto);
        logger.info("Manufactuter {} created", manufacturer);
        return ResponseEntity.ok(manufacturerMapper.toResponseDto(manufacturer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать производителя по ID")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ManufacturerResponseDto> editManufacturerById(
            @RequestBody @Valid ManufacturerRequestDto requestDto,
            @PathVariable("id") Long id) {
        Manufacturer manufacturer = manufacturerService.editManufacturer(requestDto, id);
        logger.info("Manufactuter {} edited", manufacturer);
        return ResponseEntity.ok(manufacturerMapper.toResponseDto(manufacturer));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить производителя")
    @SecurityRequirement(name = "Bearer Authentication")
    @AdminAccess
    public ResponseEntity<?> deleteManufacturerById(
            @PathVariable("id") long manufacturerId
    ) {
        manufacturerService.deleteManufacturer(manufacturerId);
        logger.info("Manufactuter with ID = {} deleted", manufacturerId);
        return ResponseEntity.ok().build();
    }

    private void updatePageRequestDtoIfSortIsEmpty(PageRequestDto pageRequestDto) {
        if (pageRequestDto.getSortByFields() == null || pageRequestDto.getSortByFields().length == 0) {
            pageRequestDto.setSortByFields(new String[]{"name"});
        }
    }
}
