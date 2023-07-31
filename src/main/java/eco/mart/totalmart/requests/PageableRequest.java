package eco.mart.totalmart.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
public class PageableRequest {

    String sort = "id";
    Integer page = 0;
    Integer size = 10;
    String key = "";

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageableRequest{");
        sb.append("sort='").append(sort).append('\'');
        sb.append(", page=").append(page);
        sb.append(", size=").append(size);
        sb.append(", key='").append(key).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Pageable toPageable() {
        String sortString = this.getSort();
        Sort sort;
        if (sortString != null) {
            if (sortString.startsWith("!")) {
                sort = Sort.by(sortString.substring(1)).descending();
            } else {
                sort = Sort.by(sortString).ascending();
            }
        } else {
            sort = Sort.by("id").ascending();
        }

        Integer page = this.getPage();
        Integer size = this.getSize();

        System.out.println("page: " + page);
        System.out.println("size: " + size);
        System.out.println("sort: " + sort);


        return PageRequest.of(
                page == null ? 0 : page,
                size == null ? 10 : size,
                sort);

    }

}
