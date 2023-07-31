package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
    private String resetPasswordToken;

    @Column(name = "resetPasswordTokenExpiryDate")
    private Timestamp resetPasswordTokenExpiryDate;

    @Column(name = "accessToken")
    private String accessToken;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive = false;

    @Column(name = "createdDate", nullable = false)
    @CurrentTimestamp(timing = GenerationTiming.INSERT)
    private Timestamp createdDate;

    @Transient
    private int spent;


    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Wishlist> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Cart> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Address> addresses = new ArrayList<>();

    /*
    |||||| Method ||||||||
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!username.equals(user.username)) return false;
        return email.equals(user.email);
    }


    public Integer getSpent() {
        return orders.stream().map(Order::getTotalBill).reduce(0, Integer::sum);
    }

    public String getAvatar() {
        return avatar == null ? "/public/user/images/avatar/default-avatar.png" : avatar;
    }

    public void generateResetPasswordToken() {
        this.resetPasswordToken = java.util.UUID.randomUUID().toString();
        this.resetPasswordTokenExpiryDate = new Timestamp(System.currentTimeMillis() + 1000 * 60 *15);
    }

    public void clearResetPasswordToken() {
        this.resetPasswordToken = null;
        this.resetPasswordTokenExpiryDate = null;
    }

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

    public boolean isAdmin() {
        return getRole() == Role.ADMIN;
    }
}