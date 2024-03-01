package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Manufacturer;
import ru.sinitsynme.logistapi.mapper.ManufacturerMapper;
import ru.sinitsynme.logistapi.repository.ManufacturerRepository;
import ru.sinitsynme.logistapi.rest.dto.ManufacturerRequestDto;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.MANUFACTURER_NOT_FOUND_CODE;

@Service
public class ManufacturerService {

    private final ManufacturerMapper manufacturerMapper;
    private final ManufacturerRepository manufacturerRepository;
    private final Logger logger = LoggerFactory.getLogger(ManufacturerService.class);

    @Autowired
    public ManufacturerService(ManufacturerMapper manufacturerMapper, ManufacturerRepository manufacturerRepository) {
        this.manufacturerMapper = manufacturerMapper;
        this.manufacturerRepository = manufacturerRepository;
    }

    public Manufacturer saveManufacturer(ManufacturerRequestDto manufacturerRequestDto) {
        Manufacturer manufacturer = manufacturerMapper.fromRequestDto(manufacturerRequestDto);
        return manufacturerRepository.save(manufacturer);
    }

    public Manufacturer editManufacturer(ManufacturerRequestDto manufacturerRequestDto, Long manufacturerId) {
        Manufacturer manufacturerFromDb = getManufacturerById(manufacturerId);
        manufacturerFromDb.setName(manufacturerRequestDto.getName());
        manufacturerFromDb.setContactNumber(manufacturerRequestDto.getContactNumber());
        return manufacturerRepository.save(manufacturerFromDb);
    }

    public void deleteManufacturer(Long manufacturerId) {
        if (!manufacturerRepository.existsById(manufacturerId)) {
            throw notFoundException(manufacturerId);
        }
        manufacturerRepository.deleteById(manufacturerId);
    }

    public Manufacturer getManufacturerById(Long manufacturerId) {
        return manufacturerRepository
                .findById(manufacturerId)
                .orElseThrow(() -> notFoundException(manufacturerId));
    }

    public Page<Manufacturer> getManufacturerPage(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    private NotFoundException notFoundException(Long manufacturerId) {
        logger.warn("Manufacturer with id = {} not found", manufacturerId);
        return new NotFoundException(
                String.format("Manufacturer with id = %d not found", manufacturerId),
                null,
                NOT_FOUND,
                MANUFACTURER_NOT_FOUND_CODE,
                ExceptionSeverity.WARN);
    }

}
