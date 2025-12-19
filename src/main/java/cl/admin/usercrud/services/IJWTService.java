package cl.admin.usercrud.services;

import cl.admin.usercrud.dtos.TokenResponseDTO;
import cl.admin.usercrud.models.UserEntity;
import org.springframework.http.ResponseEntity;

public interface IJWTService {

    String extracUUID(String token);

    ResponseEntity<TokenResponseDTO> generateToken(String uuid);

    boolean isTokenValid(String token, UserEntity userEntity);

}
