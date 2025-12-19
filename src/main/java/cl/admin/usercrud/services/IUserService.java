package cl.admin.usercrud.services;

import cl.admin.usercrud.dtos.UserDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    ResponseEntity<List<UserResponseDTO>> getAllUsers();
    ResponseEntity<UserResponseDTO> getUserById(UUID id);
    ResponseEntity<UserResponseDTO> createUser(UserDTO personDTO);
    void deleteUser(UUID id);
    ResponseEntity<UserResponseDTO> updateUser(UUID id,UserDTO personDTO);

    UserDetailsService userDetailsService();


}
