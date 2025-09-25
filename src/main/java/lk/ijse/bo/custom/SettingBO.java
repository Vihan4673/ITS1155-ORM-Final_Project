package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.UserDTO;

import java.util.List;

public interface SettingBO extends SuperBO {

    List<UserDTO> getAllUsers();               // Get all users for TableView
    void deleteUser(UserDTO userDTO);          // Delete selected user
    void updateUser(UserDTO userDTO);          // Update user details (username/password)

    // New methods to support password update separately
    String getUserPasswordByUsername(String username);  // Fetch hashed password
    void updateUserPassword(String username, String newHashedPassword); // Update password
}
