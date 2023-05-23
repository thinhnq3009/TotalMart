package eco.mart.totalmart.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseObject {
    String message = "Successfully !!!!";
    String status = "success";

    String action = "none";
    Object data;

    public ResponseObject(String message, String status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public ResponseObject message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResponseEntity<ResponseObject> toResponseEntity(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(this);
    }

    public ResponseEntity<ResponseObject> toResponseEntity() {
        return ResponseEntity.status(HttpStatus.OK).body(this);
    }


}
