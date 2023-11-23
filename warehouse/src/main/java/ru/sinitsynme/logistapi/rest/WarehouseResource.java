package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.rest.dto.WarehouseRequestDto;
import ru.sinitsynme.logistapi.rest.dto.WarehouseResponseDto;

import java.util.List;

@Tag(name = "Управление складами")
@RestController
@RequestMapping("/warehouse")
public class WarehouseResource {

    @Operation(summary = "Создать склад")
    @PostMapping
    public ResponseEntity<WarehouseResponseDto> createWarehouse(
            @RequestBody WarehouseRequestDto warehouseRequestDto) {
        return ResponseEntity.ok(new WarehouseResponseDto(10L, "test", null, null, null, null));
    }

    @Operation(summary = "Редактировать склад")

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> editWarehouse(@RequestBody WarehouseRequestDto warehouseRequestDto,
                                                              @PathVariable Long id) {
        return ResponseEntity.ok(new WarehouseResponseDto(10L, "test-edited", null, null, null, null));
    }

    @Operation(summary = "Получить склад")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok(new WarehouseResponseDto(10L, "test", null, null, null, null));
    }

    @Operation(summary = "Получить список складов")
    @GetMapping
    public ResponseEntity<List<WarehouseResponseDto>> getWarehouse(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(List.of(
                new WarehouseResponseDto(10L, "test", null, null, null, null),
                new WarehouseResponseDto(10L, "test", null, null, null, null)
        ));
    }

    @Operation(summary = "Удалить склад")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
