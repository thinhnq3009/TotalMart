package eco.mart.totalmart.module;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class CustomPage<T> extends PageImpl<T> {

    private int next;
    private int prev;

    private int firstIndex;
    private int lastIndex;




    public CustomPage(List<T> content) {
        super(content);
    }

    public CustomPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }


    public boolean hasPrev() {
        return getNumber() > 0;
    }

    public int getPrev() {
        return getNumber() - 1;
    }

    public int getNext() {
        return getNumber() + 1;
    }

    public int getFirstIndex() {
        return getNumber() * getSize();
    }

    public int getLastIndex() {
        if (getTotalElements() == 0)
            return 0;
        return getNumberOfElements()/getSize() ;
    }

    public CustomPage<T> filterAndClone(Predicate<? super T> filter) {
        Objects.requireNonNull(filter, "filter must not be null");
        List<T> ts = getContent()
                .stream()
                .filter(filter)
                .toList();
        return CustomPage.of(ts);
    }




    // Static methods
    public static <T> CustomPage<T> of(List<T> content) {
        return new CustomPage<>(content);
    }

    public static <T> CustomPage<T> of(List<T> content, Pageable pageable) {
        int total = content.size();
        int first = (int)pageable.getOffset();
        int last = Math.min(first + pageable.getPageSize(), total);
        if (first > last) {
            return new CustomPage<>(List.of(), pageable, 0);
        }
        return new CustomPage<>(content.subList(first,last), pageable,  content.size());
    }

}
