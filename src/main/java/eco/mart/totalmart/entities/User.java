package eco.mart.totalmart.entities;

import eco.mart.totalmart.auth.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.tuple.GenerationTiming;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;


@Getter
@Setter
@Entity
@Table(name = "tblUser", schema = "dbo")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Nationalized
    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Nationalized
    @Column(name = "fullname", nullable = false, length = 100)
    private String fullname;

    @Nationalized
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Nationalized
    @Column(name = "avatar", length = 1000)
    private String avatar;

    @Nationalized
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "resetPasswordToken")
    private Long resetPasswordToken;

    @Column(name = "accessToken")
    private Long accessToken;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive = false;

    @Column(name = "createdDate", nullable = false)
    @CurrentTimestamp(timing = GenerationTiming.INSERT)
    private Timestamp createdDate;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

//    @OneToMany(mappedBy = "customer")
//    private Set<Order> orders = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "user")
//    private Set<Address> addresses = new LinkedHashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}