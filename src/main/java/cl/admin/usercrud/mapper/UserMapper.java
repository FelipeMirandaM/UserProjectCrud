package cl.admin.usercrud.mapper;

import cl.admin.usercrud.dtos.UserDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import cl.admin.usercrud.models.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PhoneMapper phoneMapper;

    private final PasswordEncoder passwordEncoder;


    public List<UserResponseDTO> toResponseDTO(List<UserEntity> userEntityList) {

        return userEntityList.stream()
                .map(userEntity -> UserResponseDTO.builder()
                        .userId(userEntity.getUserId())
                        .email(userEntity.getEmail())
                        .name(userEntity.getName())
                        .created(userEntity.getCreated())
                        .modified(userEntity.getModified())
                        .isActive(userEntity.getIsActive())
                        .lastLogin(userEntity.getLastLogin())
                        .phones(phoneMapper.toResponse(userEntity.getPhones()))
                        .build())
                .collect(Collectors.toList());
    }
    public UserResponseDTO toResponseDTO(UserEntity userEntity) {

        return UserResponseDTO.builder()
                        .userId(userEntity.getUserId())
                        .email(userEntity.getEmail())
                        .name(userEntity.getName())
                        .modified(userEntity.getModified())
                        .created(userEntity.getCreated())
                        .isActive(userEntity.getIsActive())
                        .lastLogin(userEntity.getLastLogin())
                        .phones(phoneMapper.toResponse(userEntity.getPhones()))
                        .build();
    }

    public UserEntity toEntity(UserDTO userDTO) {

        UserEntity user = new UserEntity();
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setIsActive(true);

            if(userDTO.getPhones() != null){
                user.setPhones(phoneMapper.toEntity(userDTO.getPhones(), user));
            }


        return user;
    }


}
