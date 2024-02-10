package dto;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {

    @Min(0)
    private int page;
    @Min(1)
    private int size;
    private String[] sortByFields;

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.by(sortByFields));
    }

}
