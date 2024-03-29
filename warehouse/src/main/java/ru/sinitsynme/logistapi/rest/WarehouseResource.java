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
import ru.sinitsynme.logistapi.entity.Warehouse;
import ru.sinitsynme.logistapi.mapper.WarehouseMapper;
import ru.sinitsynme.logistapi.rest.dto.WarehouseRequestDto;
import ru.sinitsynme.logistapi.rest.dto.WarehouseResponseDto;
import ru.sinitsynme.logistapi.service.WarehouseService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Управление складами")
@RestController
@RequestMapping("/rest/api/v1/warehouse")
public class WarehouseResource {

    private final Logger logger = LoggerFactory.getLogger(WarehouseResource.class);
    private final WarehouseMapper warehouseMapper;
    private final WarehouseService warehouseService;

    public WarehouseResource(WarehouseMapper warehouseMapper, WarehouseService warehouseService) {
        this.warehouseMapper = warehouseMapper;
        this.warehouseService = warehouseService;
    }

    @Operation(summary = "Создать склад")
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<WarehouseResponseDto> createWarehouse(
            @RequestBody @Valid WarehouseRequestDto warehouseRequestDto) {
        Warehouse warehouse = warehouseService.createWarehouse(warehouseRequestDto);
        logger.info("Created warehouse with ID {}", warehouse.getId());

        WarehouseResponseDto warehouseResponseDto = warehouseMapper.warehouseToResponseDto(warehouse);
        return ResponseEntity.ok(warehouseResponseDto);
    }

    @Operation(summary = "Редактировать склад")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<WarehouseResponseDto> editWarehouse(@RequestBody @Valid WarehouseRequestDto warehouseRequestDto,
                                                              @PathVariable Long id) {
        Warehouse editedWarehouse = warehouseService.editWarehouse(warehouseRequestDto, id);
        logger.info("Edited warehouse with ID {}", editedWarehouse.getId());

        WarehouseResponseDto warehouseResponseDto = warehouseMapper.warehouseToResponseDto(editedWarehouse);
        return ResponseEntity.ok(warehouseResponseDto);
    }

    @Operation(summary = "Получить склад")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getWarehouse(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.getWarehouse(id);
        WarehouseResponseDto responseDto = warehouseMapper.warehouseToResponseDto(warehouse);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Получить страницу складов")
    @GetMapping
    public ResponseEntity<Page<WarehouseResponseDto>> getWarehouse(
            @Valid PageRequestDto pageRequestDto) {
        updatePageRequestDtoIfSortIsEmpty(pageRequestDto);

        Page<WarehouseResponseDto> organizationList = warehouseService
                .getPageOfWarehouse(pageRequestDto.toPageable())
                .map(warehouseMapper::warehouseToResponseDto);

        return ResponseEntity.ok(organizationList);
    }

    @Operation(summary = "Удалить склад")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);

        logger.info("Deleted warehouse with ID {}", id);
        return ResponseEntity.ok().build();
    }

    private void updatePageRequestDtoIfSortIsEmpty(PageRequestDto pageRequestDto) {
        if (pageRequestDto.getSortByFields() == null || pageRequestDto.getSortByFields().length == 0) {
            pageRequestDto.setSortByFields(new String[]{"name"});
        }
    }
}
