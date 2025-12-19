package cl.admin.usercrud.services;

import cl.admin.usercrud.dtos.TokenResponseDTO;
import cl.admin.usercrud.models.UserEntity;
import cl.admin.usercrud.services.impl.JWTServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JWTServicesImplTest {

    private JWTServicesImpl jwtService;

    @Mock
    private UserEntity user;

    @BeforeEach
    void setUp() {
        jwtService = new JWTServicesImpl();
    }

    @Test
    void generateToken_and_extracUUID_shouldMatch() {
        String uuid = UUID.randomUUID().toString();

        ResponseEntity<TokenResponseDTO> resp = jwtService.generateToken(uuid);

        assertNotNull(resp);
        assertNotNull(resp.getBody());
        String token = resp.getBody().getToken();
        assertNotNull(token);

        String extracted = jwtService.extracUUID(token);
        assertEquals(uuid, extracted);
    }

    @Test
    void isTokenValid_returnsFalse_forDifferentUser() {
        String uuid = UUID.randomUUID().toString();
        String token = jwtService.generateToken(uuid).getBody().getToken();

        when(user.getUserId()).thenReturn(UUID.randomUUID());

        assertFalse(jwtService.isTokenValid(token, user));
    }


}
