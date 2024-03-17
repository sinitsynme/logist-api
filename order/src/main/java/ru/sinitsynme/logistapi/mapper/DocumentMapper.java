package ru.sinitsynme.logistapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sinitsynme.logistapi.entity.Document;
import ru.sinitsynme.logistapi.rest.dto.document.DocumentResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {
    DocumentResponseDto toResponseDto(Document document);
}
