package ru.sinitsynme.logistapi.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.rest.dto.WarehouseRequestDto;
import ru.sinitsynme.logistapi.rest.dto.WarehouseResponseDto;

@RestController
@RequestMapping("/warehouse")
public class WarehouseResource {

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> createWarehouse(
            @RequestBody WarehouseRequestDto warehouseRequestDto) {
        return ResponseEntity.ok(new WarehouseResponseDto(10L, "test", null, null, null, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> editWarehouse(@RequestBody WarehouseRequestDto warehouseRequestDto,
                                                              @PathVariable Long id) {
        return ResponseEntity.ok(new WarehouseResponseDto(10L, "test-edited", null, null, null, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok(new WarehouseResponseDto(10L, "test", null, null, null, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
