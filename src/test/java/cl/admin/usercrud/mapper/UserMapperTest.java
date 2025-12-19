package cl.admin.usercrud.mapper;

import cl.admin.usercrud.dtos.UserDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import cl.admin.usercrud.models.PhoneEntity;
import cl.admin.usercrud.models.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private PhoneMapper phoneMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper(phoneMapper, passwordEncoder);
    }

    @Test
    void toResponseDTO_list_mapsFieldsAndPhones() {
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UserEntity entity = UserEntity.builder()
                .userId(userId)
                .email("user@example.com")
                .name("Usuario")
                .created(now)
                .modified(now)
                .lastLogin(now)
                .isActive(true)
                .phones(List.of()) // phones will be replaced by phoneMapper result
                .build();

        when(phoneMapper.toResponse(entity.getPhones())).thenReturn(List.of());

        List<UserResponseDTO> result = mapper.toResponseDTO(List.of(entity));

        assertNotNull(result);
        assertEquals(1, result.size());
        UserResponseDTO dto = result.get(0);
        assertEquals(userId, dto.getUserId());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("Usuario", dto.getName());
        assertEquals(now, dto.getCreated());
        assertEquals(now, dto.getModified());
        assertEquals(now, dto.getLastLogin());
        assertTrue(dto.getIsActive());
        assertNotNull(dto.getPhones());
        verify(phoneMapper).toResponse(entity.getPhones());
    }

    @Test
    void toResponseDTO_single_mapsFieldsAndPhones() {
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UserEntity entity = UserEntity.builder()
                .userId(userId)
                .email("single@example.com")
                .name("Single")
                .created(now)
                .modified(now)
                .lastLogin(now)
                .isActive(false)
                .phones(List.of())
                .build();

        when(phoneMapper.toResponse(entity.getPhones())).thenReturn(List.of());

        UserResponseDTO dto = mapper.toResponseDTO(entity);

        assertEquals(userId, dto.getUserId());
        assertEquals("single@example.com", dto.getEmail());
        assertEquals("Single", dto.getName());
        assertEquals(now, dto.getCreated());
        assertEquals(now, dto.getModified());
        assertEquals(now, dto.getLastLogin());
        assertFalse(dto.getIsActive());
        assertNotNull(dto.getPhones());
        verify(phoneMapper).toResponse(entity.getPhones());
    }

    @Test
    void toEntity_encodesPassword_setsActive_and_mapsPhones_attachingUser() {
        // Preparar DTO de entrada
        UserDTO dto = UserDTO.builder()
                .email("nuevo@example.com")
                .name("Nuevo")
                .password("plain")
                .phones(List.of()) // contenido irrelevante, phoneMapper mock lo manejará
                .build();

        // Simular comportamiento del encoder y del phoneMapper
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        List<PhoneEntity> mappedPhones = List.of(mock(PhoneEntity.class));
        // Capturaremos el UserEntity pasado al phoneMapper para comprobar que es el mismo objeto
        when(phoneMapper.toEntity(eq(dto.getPhones()), any(UserEntity.class))).thenReturn(mappedPhones);

        UserEntity result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals("nuevo@example.com", result.getEmail());
        assertEquals("Nuevo", result.getName());
        assertEquals("encoded", result.getPassword());
        assertTrue(result.getIsActive());
        assertSame(mappedPhones, result.getPhones());

        verify(passwordEncoder).encode("plain");

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(phoneMapper).toEntity(eq(dto.getPhones()), userCaptor.capture());
        // El UserEntity pasado al phoneMapper debe ser el mismo objeto que devolvimos
        assertSame(result, userCaptor.getValue());
    }

}
