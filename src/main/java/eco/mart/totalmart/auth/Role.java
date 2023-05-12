package eco.mart.totalmart.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permission.ADMIN_READONLY,
                    Permission.ADMIN_READWRITE,
                    Permission.USER_READONLY,
                    Permission.USER_READWRITE
            )
    ),

    CUSTOMER(
            Set.of(
                    Permission.USER_READONLY,
                    Permission.USER_READWRITE
            )
    ),
    GUEST(
            Collections.emptySet()
    );

    @Getter
    private final Set<Permission> value;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getValue().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }


}
