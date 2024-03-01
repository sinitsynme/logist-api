package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.FeatureSetting;
import ru.sinitsynme.logistapi.mapper.FeatureSettingMapper;
import ru.sinitsynme.logistapi.repository.FeatureSettingRepository;
import ru.sinitsynme.logistapi.rest.dto.FeatureSettingDto;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.FEATURE_SETTING_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCode.MANUFACTURER_NOT_FOUND_CODE;

@Service
public class FeatureSettingService {

    private final FeatureSettingRepository featureSettingRepository;
    private final FeatureSettingMapper featureSettingMapper;
    private final Logger logger = LoggerFactory.getLogger(FeatureSettingService.class);

    @Autowired
    public FeatureSettingService(FeatureSettingRepository featureSettingRepository,
                                 FeatureSettingMapper featureSettingMapper) {
        this.featureSettingRepository = featureSettingRepository;
        this.featureSettingMapper = featureSettingMapper;
    }

    public List<FeatureSetting> getFeatureSettings() {
        return featureSettingRepository.findAll();
    }

    public FeatureSetting saveFeatureSetting(FeatureSettingDto featureSettingDto) {
        FeatureSetting featureSetting = featureSettingMapper.fromDto(featureSettingDto);
        if (featureSettingRepository.existsById(featureSettingDto.getName())) {
            throw new BadRequestException(
                    String.format("Feature setting with name = %s already exists", featureSettingDto.getName()),
                    null,
                    BAD_REQUEST,
                    MANUFACTURER_NOT_FOUND_CODE,
                    ExceptionSeverity.WARN);
        }
        featureSettingRepository.save(featureSetting);

        logger.info("Feature setting with name {} is saved with value {}",
                featureSetting.getName(), featureSetting.isEnabled()
        );
        return featureSetting;
    }

    public FeatureSetting updateFeatureSetting(FeatureSettingDto featureSettingDto, String name) {
        FeatureSetting featureSetting = featureSettingMapper.fromDto(featureSettingDto);

        FeatureSetting featureSettingFromDb = featureSettingRepository.findById(name)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Feature setting with name = %s doesn't exist", name),
                        null,
                        NOT_FOUND,
                        FEATURE_SETTING_NOT_FOUND_CODE,
                        ExceptionSeverity.WARN));

        featureSettingFromDb.setDescription(featureSetting.getDescription());
        featureSettingFromDb.setEnabled(featureSetting.isEnabled());

        featureSettingRepository.save(featureSettingFromDb);

        logger.info("Feature setting with name {} is updated with value {}",
                featureSettingFromDb.getName(), featureSettingFromDb.isEnabled()
        );

        return featureSettingFromDb;
    }
}
