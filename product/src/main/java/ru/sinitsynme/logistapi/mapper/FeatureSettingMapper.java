package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import ru.sinitsynme.logistapi.entity.FeatureSetting;
import ru.sinitsynme.logistapi.rest.dto.FeatureSettingDto;

@Mapper(componentModel = "spring")
public interface FeatureSettingMapper {

    FeatureSetting fromDto(FeatureSettingDto featureSettingDto);

}