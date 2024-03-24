package ru.sinitsynme.logistapi.rest.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sinitsynme.logistapi.entity.enums.DocumentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponseDto {

    private DocumentType type;
    private String name;
    private String path;

}
