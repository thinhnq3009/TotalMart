package eco.mart.totalmart.dto;

import eco.mart.totalmart.auth.Role;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.User} entity
 */
public record UserDto(Long id, String username, String password, String fullname, String email, String avatar,
                      String phone, Long resetPasswordToken, Long accessToken, Boolean isActive, Timestamp createdDate,
                      Role role) implements Serializable {
}