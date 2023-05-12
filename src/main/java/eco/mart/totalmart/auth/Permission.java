package eco.mart.totalmart.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READONLY("ADMIN_READONLY"),
    ADMIN_READWRITE("ADMIN_READWRITE"),
    USER_READONLY("USER_READONLY"),
    USER_READWRITE("USER_READWRITE");





    @Getter
  private  final String value;


}
