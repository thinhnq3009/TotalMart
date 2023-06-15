package eco.mart.totalmart.module;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class MyPage<T> extends PageImpl<T> {

    private int next;
    private int prev;
    private int firstIndex;
    private int lastIndex;


    public MyPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public MyPage(List<T> content) {
        super(content);
    }

    public static <T> MyPage<T> of(List<T> content, Pageable pageable, long total) {
        return new MyPage<>(content, pageable, total);
    }

    public static <T> MyPage<T> of(List<T> content) {
        return new MyPage<>(content);
    }

    public static <T> MyPage<T> of(Page<T> page) {
        return new MyPage<>(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public int getPrev() {
        return  getNumber() - 1;
    }

    public int getNext() {
        return getNumber() + 1;
    }

    public int getFirstIndex() {
        return getNumber() * getSize();
    }

    public int getLastIndex() {
        return getNumber() * getSize() + getNumberOfElements() - 1;
    }
}
