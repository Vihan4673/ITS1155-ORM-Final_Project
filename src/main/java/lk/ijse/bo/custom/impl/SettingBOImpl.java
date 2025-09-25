package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.SettingBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.User;
import lk.ijse.util.PasswordStorage;

import java.util.ArrayList;
import java.util.List;

public class SettingBOImpl implements SettingBO {

    private final UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOFactory.DAOType.USER);

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> allUsers = userDAO.getAllUsers();
        for (User user : allUsers) {
            userDTOS.add(new UserDTO(user.getUserId(), user.getUserName(), user.getPassword(), user.getRole()));
        }
        return userDTOS;
    }

    @Override
    public void deleteUser(UserDTO userDTO) {
        User user = new User(userDTO.getUserId(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getRole());
        userDAO.delete(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        User user = new User(userDTO.getUserId(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getRole());
        userDAO.update(user);
    }

    // --- New Methods for password update ---

    @Override
    public String getUserPasswordByUsername(String username) {
        User user = userDAO.getUser(username);
        return user != null ? user.getPassword() : null;
    }

    @Override
    public void updateUserPassword(String username, String newHashedPassword) {
        User user = userDAO.getUser(username);
        if (user != null) {
            user.setPassword(newHashedPassword);
            userDAO.update(user);
        }
    }
}
