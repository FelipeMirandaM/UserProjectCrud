package cl.admin.usercrud.services.impl;

import cl.admin.usercrud.dtos.UserDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import cl.admin.usercrud.exception.NotFoundException;
import cl.admin.usercrud.mapper.UserMapper;
import cl.admin.usercrud.models.UserEntity;
import cl.admin.usercrud.repository.UserRepository;
import cl.admin.usercrud.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    @Override
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserEntity> userEntityList =  repository.findAll();


        return new ResponseEntity<>(mapper.toResponseDTO(userEntityList), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserById(UUID id) {

        UserEntity user =  repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        return new ResponseEntity<>(mapper.toResponseDTO(user), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserDTO userDTO) {

        if(repository.existsByEmail(userDTO.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }

        UserEntity userEntity= repository.save(mapper.toEntity(userDTO));

        return new ResponseEntity<>(mapper.toResponseDTO(userEntity), HttpStatus.CREATED);
    }

    @Override
    public void deleteUser(UUID id) {

        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setIsActive(false);

        repository.save(user);

    }

    @Override
    public ResponseEntity<UserResponseDTO> updateUser(UUID id, UserDTO userDTO) {

        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!userDTO.getEmail().equals(user.getEmail())
                && repository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserEntity userEntity = mapper.toEntity(userDTO);

        userEntity.setUserId(id);

        UserEntity userSave = repository.save(userEntity);

        return ResponseEntity.ok(mapper.toResponseDTO(userSave));
    }

    @Bean
    public UserDetailsService userDetailsService() throws UsernameNotFoundException{

        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return repository.findById(UUID.fromString(username)).orElseThrow(
                        ()-> new UsernameNotFoundException("User not found"));
            }

        };
    }

}
