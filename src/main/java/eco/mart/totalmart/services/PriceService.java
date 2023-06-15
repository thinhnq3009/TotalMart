package eco.mart.totalmart.services;

import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Numbers;

@Service
public class PriceService {

        private final String currency = " đ";

        private Numbers numbers;
        public String toString (Double price) {
            return numbers.formatDecimal(price, 3, "POINT", 0, "COMMA") + currency;
        }

        public String toString (Integer price) {
            return numbers.formatDecimal(price, 3, "POINT", 0, "COMMA") + currency;
        }

        public String toString (Long price) {
            return numbers.formatDecimal(price, 3, "POINT", 0, "COMMA") + currency;
        }



}
