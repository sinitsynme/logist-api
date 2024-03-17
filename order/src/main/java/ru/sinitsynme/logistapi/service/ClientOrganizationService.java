package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Address;
import ru.sinitsynme.logistapi.entity.ClientOrganization;
import ru.sinitsynme.logistapi.entity.enums.OrganizationStatus;
import ru.sinitsynme.logistapi.mapper.AddressMapper;
import ru.sinitsynme.logistapi.mapper.ClientOrganizationMapper;
import ru.sinitsynme.logistapi.repository.ClientOrganizationRepository;
import ru.sinitsynme.logistapi.rest.dto.address.AddressRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationEditRequestDto;
import ru.sinitsynme.logistapi.rest.dto.clientOrganization.ClientOrganizationRequestDto;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.*;

@Service
public class ClientOrganizationService {

    private final AddressMapper addressMapper;
    private final ClientOrganizationMapper clientOrganizationMapper;
    private final ClientOrganizationRepository clientOrganizationRepository;
    private final Logger logger = LoggerFactory.getLogger(ClientOrganizationService.class);

    @Autowired
    public ClientOrganizationService(
            AddressMapper addressMapper,
            ClientOrganizationMapper clientOrganizationMapper,
            ClientOrganizationRepository clientOrganizationRepository) {
        this.addressMapper = addressMapper;
        this.clientOrganizationMapper = clientOrganizationMapper;
        this.clientOrganizationRepository = clientOrganizationRepository;
    }

    public ClientOrganization getClientOrganization(String inn) {
        return clientOrganizationRepository.findById(inn).orElseThrow(
                () -> notFoundException(inn));
    }

    public Page<ClientOrganization> getClientOrganizations(Pageable pageable) {
        return clientOrganizationRepository.findAll(pageable);
    }

    public Page<ClientOrganization> getClientOrganizationsByClientId(UUID clientId, Pageable pageable) {
        return clientOrganizationRepository.findAllByClientId(clientId, pageable);
    }

    public ClientOrganization saveClientOrganization(ClientOrganizationRequestDto requestDto) {
        ClientOrganization clientOrganization = clientOrganizationMapper.fromRequestDto(requestDto);
        clientOrganization.setOrganizationStatus(OrganizationStatus.NEW);

        checkIfClientOrganizationExistsByInn(requestDto.getInn());

        clientOrganization = clientOrganizationRepository.save(clientOrganization);
        logger.info("Created client organization with inn = {}", requestDto.getInn());

        return clientOrganization;
    }

    public ClientOrganization addAddressToClientOrganization(AddressRequestDto requestDto, String inn) {
        ClientOrganization clientOrganizationFromDb = getClientOrganization(inn);

        Address newAddress =  addressMapper.requestDtoToAddress(requestDto);
        newAddress.setClientOrganization(clientOrganizationFromDb);
        clientOrganizationFromDb.getAddressList().add(newAddress);

        clientOrganizationFromDb = clientOrganizationRepository.save(clientOrganizationFromDb);

        logger.info("Added new address to client organization with inn = {}", inn);

        return clientOrganizationFromDb;
    }


    public ClientOrganization editClientOrganization(ClientOrganizationEditRequestDto requestDto, String inn) {
        ClientOrganization clientOrganizationFromDb = getClientOrganization(inn);

        clientOrganizationFromDb.setClientAccount(requestDto.getClientAccount());
        clientOrganizationFromDb.setName(requestDto.getName());
        clientOrganizationFromDb.setBik(requestDto.getBik());
        clientOrganizationFromDb.setBankName(requestDto.getBankName());
        clientOrganizationFromDb.setCorrespondentAccount(requestDto.getCorrespondentAccount());
        clientOrganizationFromDb.setClientId(requestDto.getClientId());

        clientOrganizationFromDb = clientOrganizationRepository.save(clientOrganizationFromDb);

        logger.info("Edited client organization with inn = {}", inn);

        return clientOrganizationFromDb;
    }

    public void changeOrganizationStatus(String inn, String newStatus) {
        ClientOrganization clientOrganization = getClientOrganization(inn);

        try {
            clientOrganization.setOrganizationStatus(OrganizationStatus.valueOf(newStatus));
        } catch (IllegalArgumentException e) {
            logger.warn("Incorrect status {} for product with inn {}", newStatus, inn);
            throw new BadRequestException(
                    String.format("Incorrect status %s for product with inn %s", newStatus, inn),
                    null,
                    BAD_REQUEST,
                    CLIENT_ORGANIZATION_INCORRECT_STATUS,
                    ExceptionSeverity.WARN);

        }

        clientOrganizationRepository.save(clientOrganization);
    }


    private NotFoundException notFoundException(String inn) {
        logger.warn("Client organization with inn = {} not found", inn);
        return new NotFoundException(
                String.format("Client organization client with inn = %s not found", inn),
                null,
                NOT_FOUND,
                CLIENT_ORGANIZATION_NOT_FOUND,
                ExceptionSeverity.WARN);
    }

    private void checkIfClientOrganizationExistsByInn(String inn) {
        if (clientOrganizationRepository.findById(inn).isPresent()) {
            throw new BadRequestException(
                    String.format("Client organization with inn = %s exists", inn),
                    null,
                    BAD_REQUEST,
                    CLIENT_ORGANIZATION_EXISTS_BY_INN,
                    ExceptionSeverity.WARN);
        }
    }
}
