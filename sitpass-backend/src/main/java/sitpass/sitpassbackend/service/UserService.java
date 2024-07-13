package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.UserDTO;
import sitpass.sitpassbackend.model.User;

public interface UserService {

    User getModel(String email);
    User getModel(Long id);
    UserDTO create(UserDTO userDTO);
    UserDTO update(UserDTO userDTO, String imageUrl);
    UserDTO update(Long id, String password);
    Boolean validateCurrentPassword(Long id, String typedCurrentPassword);

}
