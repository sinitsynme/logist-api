package ru.sinitsynme.logistapi.rest;

import dto.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sinitsynme.logistapi.entity.ClientOrganization;
import ru.sinitsynme.logistapi.mapper.ClientOrganizationMapper;
import ru.sinitsynme.logistapi.rest.dto.address.AddressRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ChangeClientOrganizationStatusRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationEditRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationResponseDto;
import ru.sinitsynme.logistapi.service.ClientOrganizationService;

import java.util.UUID;

@Tag(name = "Управление организациями клиентов")
@RestController
@RequestMapping("/rest/api/v1/clientOrganization")
public class ClientOrganizationResource {

    private final ClientOrganizationService clientOrganizationService;
    private final ClientOrganizationMapper clientOrganizationMapper;

    @Autowired
    public ClientOrganizationResource(ClientOrganizationService clientOrganizationService,
                                      ClientOrganizationMapper clientOrganizationMapper) {
        this.clientOrganizationService = clientOrganizationService;
        this.clientOrganizationMapper = clientOrganizationMapper;
    }

    @GetMapping
    @Operation(summary = "Получить страницу организаций клиентов")
    public ResponseEntity<Page<ClientOrganizationResponseDto>> getClientOrganizations(
            @Valid PageRequestDto pageRequestDto
    ) {
        pageRequestDto.updatePageRequestDtoIfSortIsEmpty("name");

        Page<ClientOrganizationResponseDto> page = clientOrganizationService
                .getClientOrganizations(pageRequestDto.toPageable())
                .map(clientOrganizationMapper::toResponseDto);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/clientId/{clientId}")
    @Operation(summary = "Получить страницу организаций клиента по ID клиента")
    public ResponseEntity<Page<ClientOrganizationResponseDto>> getClientOrganizationsByClientId(
            @Valid PageRequestDto pageRequestDto,
            @PathVariable UUID clientId
    ) {
        pageRequestDto.updatePageRequestDtoIfSortIsEmpty("name");

        Page<ClientOrganizationResponseDto> page = clientOrganizationService
                .getClientOrganizationsByClientId(clientId, pageRequestDto.toPageable())
                .map(clientOrganizationMapper::toResponseDto);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{inn}")
    @Operation(summary = "Получить организацию клиента по ИНН")
    public ResponseEntity<ClientOrganizationResponseDto> getClientOrganizationByInn(
            @PathVariable String inn
    ) {
        ClientOrganization organization = clientOrganizationService.getClientOrganization(inn);
        return ResponseEntity.ok(clientOrganizationMapper.toResponseDto(organization));
    }

    @PostMapping
    @Operation(summary = "Сохранить организацию клиента")
    public ResponseEntity<ClientOrganizationResponseDto> saveClientOrganization(
            @RequestBody ClientOrganizationRequestDto requestDto) {
        ClientOrganization savedOrganization = clientOrganizationService.saveClientOrganization(requestDto);
        return ResponseEntity.ok(clientOrganizationMapper.toResponseDto(savedOrganization));
    }

    @PutMapping("/{inn}")
    @Operation(summary = "Обновить организацию клиента по ИНН")
    public ResponseEntity<ClientOrganizationResponseDto> editClientOrganization(
            @PathVariable String inn,
            @RequestBody ClientOrganizationEditRequestDto requestDto
    ) {
        ClientOrganization editedOrganization = clientOrganizationService.editClientOrganization(
                requestDto,
                inn
        );
        return ResponseEntity.ok(clientOrganizationMapper.toResponseDto(editedOrganization));
    }

    @PatchMapping("/{inn}/address")
    @Operation(summary = "Добавить адрес организации по ИНН")
    public ResponseEntity<ClientOrganizationResponseDto> addOrganizationAddress(
            @PathVariable String inn,
            @RequestBody AddressRequestDto requestDto
    ) {
        ClientOrganization clientOrganization = clientOrganizationService
                .addAddressToClientOrganization(requestDto, inn);

        return ResponseEntity.ok(clientOrganizationMapper.toResponseDto(clientOrganization));
    }

    @PatchMapping("/{inn}/status")
    @Operation(summary = "Обновить статус организации клиента по ИНН")
    public ResponseEntity<?> changeClientOrganizationStatus(
            @PathVariable String inn,
            @RequestBody ChangeClientOrganizationStatusRequestDto requestDto
    ) {
        clientOrganizationService.changeOrganizationStatus(inn, requestDto.getStatus());
        return ResponseEntity.ok().build();
    }
}
