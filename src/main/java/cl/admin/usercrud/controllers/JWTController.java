package cl.admin.usercrud.controllers;

import cl.admin.usercrud.dtos.TokenResponseDTO;
import cl.admin.usercrud.services.IJWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JWTController {

    private final IJWTService jwtService;

    @GetMapping("/token")
    private ResponseEntity<TokenResponseDTO> getToken(){
        return jwtService.generateToken("f9389390-cf4e-4f79-b028-95c178bfff6a");
    }

}
