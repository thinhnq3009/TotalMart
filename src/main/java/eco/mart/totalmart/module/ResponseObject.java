package eco.mart.totalmart.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject {
    String message;
    String status = "success";

    String action = "none";
    Object data;

    public ResponseObject(String message, String status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }


}
