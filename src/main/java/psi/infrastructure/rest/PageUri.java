package psi.infrastructure.rest;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@UtilityClass
public class PageUri {

    public static URI generatePageUri(UriComponentsBuilder baseUri, Pageable pageable) {
        return generatePageUri(baseUri, pageable, new LinkedMultiValueMap<>());
    }

    public static URI generatePageUri(UriComponentsBuilder baseUri, Pageable pageable, MultiValueMap<String, String> requestParamValuesByNames) {
        requestParamValuesByNames.forEach(baseUri::queryParam);
        baseUri.queryParam("page", pageable.getPageNumber()).queryParam("size", pageable.getPageSize());
        pageable.getSort().forEach(order -> baseUri.queryParam("sort", order.getProperty() + "," + order.getDirection().name()));
        return baseUri.build().toUri();
    }

    public static Pageable getLastPageable(Page<?> page) {
        int maxPageNumber = Integer.max(page.getTotalPages() - 1, 0);
        return PageRequest.of(maxPageNumber, page.getPageable().getPageSize(), page.getSort());
    }

}
