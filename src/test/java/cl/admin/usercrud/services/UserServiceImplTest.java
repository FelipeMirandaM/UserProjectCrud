package cl.admin.usercrud.services;

import cl.admin.usercrud.dtos.UserDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import cl.admin.usercrud.exception.NotFoundException;
import cl.admin.usercrud.mapper.UserMapper;
import cl.admin.usercrud.models.UserEntity;
import cl.admin.usercrud.repository.UserRepository;
import cl.admin.usercrud.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository, mapper);
    }

    @Test
    void getAllUsers_returnsMappedList() {
        UserEntity entity = mock(UserEntity.class);
        UserResponseDTO dto = mock(UserResponseDTO.class);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponseDTO(List.of(entity))).thenReturn(List.of(dto));

        ResponseEntity<List<UserResponseDTO>> resp = service.getAllUsers();

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertSame(dto, resp.getBody().get(0));
        verify(repository).findAll();
        verify(mapper).toResponseDTO(List.of(entity));
    }

    @Test
    void getUserById_existing_returnsDto() {
        UUID id = UUID.randomUUID();
        UserEntity entity = mock(UserEntity.class);
        UserResponseDTO dto = mock(UserResponseDTO.class);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(dto);

        ResponseEntity<UserResponseDTO> resp = service.getUserById(id);

        assertEquals(200, resp.getStatusCode().value());
        assertSame(dto, resp.getBody());
        verify(repository).findById(id);
        verify(mapper).toResponseDTO(entity);
    }

    @Test
    void getUserById_missing_throwsUserNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getUserById(id));
        verify(repository).findById(id);
    }

    @Test
    void createUser_savesAndReturnsDto() {
        UserDTO dtoIn = mock(UserDTO.class);
        String email = "a@b.com";
        when(dtoIn.getEmail()).thenReturn(email);

        UserEntity toSave = mock(UserEntity.class);
        UserEntity saved = mock(UserEntity.class);
        UserResponseDTO dtoOut = mock(UserResponseDTO.class);

        when(repository.existsByEmail(email)).thenReturn(false);
        when(mapper.toEntity(dtoIn)).thenReturn(toSave);
        when(repository.save(toSave)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(dtoOut);

        ResponseEntity<UserResponseDTO> resp = service.createUser(dtoIn);

        assertEquals(200, resp.getStatusCode().value());
        assertSame(dtoOut, resp.getBody());
        verify(repository).existsByEmail(email);
        verify(mapper).toEntity(dtoIn);
        verify(repository).save(toSave);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void createUser_emailInUse_throwsIllegalArgumentException() {
        UserDTO dtoIn = mock(UserDTO.class);
        when(dtoIn.getEmail()).thenReturn("inuse@example.com");
        when(repository.existsByEmail("inuse@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createUser(dtoIn));
        verify(repository).existsByEmail("inuse@example.com");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void deleteUser_existing_setsInactiveAndSaves() {
        UUID id = UUID.randomUUID();
        UserEntity entity = mock(UserEntity.class);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.deleteUser(id);

        verify(repository).findById(id);
        verify(entity).setIsActive(false);
        verify(repository).save(entity);
    }

    @Test
    void deleteUser_missing_throwsUserNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteUser(id));
        verify(repository).findById(id);
    }

    @Test
    void updateUser_existing_updatesAndReturnsDto() {
        UUID id = UUID.randomUUID();
        UserEntity existing = mock(UserEntity.class);
        UserDTO dtoIn = mock(UserDTO.class);
        when(dtoIn.getEmail()).thenReturn("new@example.com");
        when(existing.getEmail()).thenReturn("old@example.com");

        UserEntity entityFromDto = mock(UserEntity.class);
        UserEntity saved = mock(UserEntity.class);
        UserResponseDTO dtoOut = mock(UserResponseDTO.class);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail("new@example.com")).thenReturn(false);
        when(mapper.toEntity(dtoIn)).thenReturn(entityFromDto);
        when(repository.save(entityFromDto)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(dtoOut);

        ResponseEntity<UserResponseDTO> resp = service.updateUser(id, dtoIn);

        assertEquals(200, resp.getStatusCode().value());
        assertSame(dtoOut, resp.getBody());
        verify(repository).findById(id);
        verify(repository).existsByEmail("new@example.com");
        verify(mapper).toEntity(dtoIn);
        verify(entityFromDto).setUserId(id);
        verify(repository).save(entityFromDto);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void updateUser_emailConflict_throwsIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        UserEntity existing = mock(UserEntity.class);
        UserDTO dtoIn = mock(UserDTO.class);
        when(dtoIn.getEmail()).thenReturn("taken@example.com");
        when(existing.getEmail()).thenReturn("old@example.com");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.updateUser(id, dtoIn));
        verify(repository).findById(id);
        verify(repository).existsByEmail("taken@example.com");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void userDetailsService_loadUserByUsername_found() {
        UUID id = UUID.randomUUID();
        UserEntity entity = mock(UserEntity.class);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        UserDetailsService uds = service.userDetailsService();
        assertSame(entity, uds.loadUserByUsername(id.toString()));
        verify(repository).findById(id);
    }

    @Test
    void userDetailsService_loadUserByUsername_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        UserDetailsService uds = service.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> uds.loadUserByUsername(id.toString()));
        verify(repository).findById(id);
    }


}
