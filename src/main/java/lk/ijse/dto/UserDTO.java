package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int userId;
    private String userName;
    private String password;
    private String role;

    // Custom constructor for convenience (without userId)
    public UserDTO(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    // Custom getter for name
    public String getName() {
        return this.userName;
    }
}
