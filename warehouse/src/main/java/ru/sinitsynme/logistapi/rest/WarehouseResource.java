package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.Warehouse;
import ru.sinitsynme.logistapi.mapper.WarehouseMapper;
import ru.sinitsynme.logistapi.rest.dto.AddressResponseDto;
import ru.sinitsynme.logistapi.rest.dto.OrganizationResponseDto;
import ru.sinitsynme.logistapi.rest.dto.WarehouseRequestDto;
import ru.sinitsynme.logistapi.rest.dto.WarehouseResponseDto;
import ru.sinitsynme.logistapi.service.WarehouseService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Управление складами")
@RestController
@RequestMapping("/warehouse")
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
    public ResponseEntity<WarehouseResponseDto> createWarehouse(
            @RequestBody @Valid WarehouseRequestDto warehouseRequestDto) {
        Warehouse warehouse = warehouseService.createWarehouse(warehouseRequestDto);
        logger.info("Created warehouse with ID {}", warehouse.getId());

        WarehouseResponseDto warehouseResponseDto = warehouseMapper.warehouseToResponseDto(warehouse);
        return ResponseEntity.ok(warehouseResponseDto);
    }

    @Operation(summary = "Редактировать склад")

    @PutMapping("/{id}")
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

    @Operation(summary = "Получить список складов")
    @GetMapping
    public ResponseEntity<List<WarehouseResponseDto>> getWarehouse(@RequestParam @Min(value = 0) int page, @RequestParam @Min(value = 1) int size) {
        List<WarehouseResponseDto> organizationList = warehouseService
                .getPageOfWarehouse(page, size)
                .stream()
                .map(warehouseMapper::warehouseToResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(organizationList);
    }

    @Operation(summary = "Удалить склад")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);

        logger.info("Deleted warehouse with ID {}", id);
        return ResponseEntity.ok().build();
    }
}
