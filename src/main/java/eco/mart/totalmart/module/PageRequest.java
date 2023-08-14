package eco.mart.totalmart.module;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public class PageRequest {
    Integer page = 0;
    Integer size = 10;
    String sortBy = "id";


    public Pageable getPageable() {
        return org.springframework.data.domain.PageRequest.of(page, size);
    }

}
