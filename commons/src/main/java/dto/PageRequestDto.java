package dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {

    @Min(0)
    private int page;
    @Min(1)
    private int size;
    private String[] sortByFields;
    private boolean sortFromMaxToMin = false;

    public Pageable toPageable() {
        Sort.Direction direction = sortFromMaxToMin ? Sort.Direction.DESC : Sort.Direction.ASC;

        return  PageRequest.of(page, size, Sort.by(direction, sortByFields));
    }

    // TODO migrate to this way of setting default sorting column
    public void updatePageRequestDtoIfSortIsEmpty(String defaultSortColumn) {
        if (sortByFields == null || sortByFields.length == 0) {
            sortByFields = new String[]{defaultSortColumn};
        }
    }

}
