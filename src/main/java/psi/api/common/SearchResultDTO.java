package psi.api.common;

import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
@Builder
public class SearchResultDTO<T> {

    private List<T> results;
    private Long totalSize;
    private Integer pageSize;
    private Integer pageNumber;
    private URI nextPage;
    private URI previousPage;
    private URI firstPage;
    private URI lastPage;

}
