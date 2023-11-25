package ru.sinitsynme.logistapi.exception.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Ответ с исключением")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ExceptionResponse {

    @Schema(description = "Время перехвата исключения")
    private final LocalDateTime occurrenceTime;

    @Schema(description = "Сообщение исключения")
    private final String message;

    @Schema(description = "Код ошибки")
    private final String code;

    @Schema(description = "Ошибки валидации")
    private Map<String, String> validationErrors;
}
