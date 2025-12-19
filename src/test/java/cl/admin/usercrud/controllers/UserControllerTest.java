package cl.admin.usercrud.controllers;

import cl.admin.usercrud.dtos.UserDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import cl.admin.usercrud.services.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;




@ExtendWith(MockitoExtension.class)
public class UserControllerTest {


    @Mock
    private IUserService service;

    @InjectMocks
    private UserController controller;



    @Test
    void getAllUsers_returnsServiceResponse() {
        UserResponseDTO user = mock(UserResponseDTO.class);
        when(service.getAllUsers()).thenReturn(ResponseEntity.ok(List.of(user)));

        ResponseEntity<List<UserResponseDTO>> resp = controller.getAllUsers();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void getUser_invalidUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> controller.getUser("not-a-uuid"));
    }

    @Test
    void getUser_validId_returnsServiceResponse() {
        String id = UUID.randomUUID().toString();
        UserResponseDTO user = mock(UserResponseDTO.class);
        when(service.getUserById(UUID.fromString(id))).thenReturn(ResponseEntity.ok(user));

        ResponseEntity<UserResponseDTO> resp = controller.getUser(id);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(user, resp.getBody());
    }

    @Test
    void createUser_delegatesToService_andReturnsResponse() {
        UserDTO dto = mock(UserDTO.class);
        UserResponseDTO created = mock(UserResponseDTO.class);
        when(service.createUser(dto)).thenReturn(ResponseEntity.ok(created));

        ResponseEntity<UserResponseDTO> resp = controller.createUser(dto);

        verify(service, times(1)).createUser(dto);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(created, resp.getBody());
    }

    @Test
    void deleteUser_invalidUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> controller.deleteUser("bad-id"));
    }

    @Test
    void deleteUser_validId_callsService_andReturnsAccepted() {
        String id = UUID.randomUUID().toString();
        doNothing().when(service).deleteUser(UUID.fromString(id));

        ResponseEntity<Void> resp = controller.deleteUser(id);

        verify(service, times(1)).deleteUser(UUID.fromString(id));
        assertEquals(HttpStatus.ACCEPTED, resp.getStatusCode());
    }

    @Test
    void updateUser_invalidUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> controller.updateUser("xxx", mock(UserDTO.class)));
    }

    @Test
    void updateUser_validId_returnsServiceResponse() {
        String id = UUID.randomUUID().toString();
        UserDTO dto = mock(UserDTO.class);
        UserResponseDTO updated = mock(UserResponseDTO.class);
        when(service.updateUser(UUID.fromString(id), dto)).thenReturn(ResponseEntity.ok(updated));

        ResponseEntity<UserResponseDTO> resp = controller.updateUser(id, dto);

        verify(service, times(1)).updateUser(UUID.fromString(id), dto);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(updated, resp.getBody());
    }


}
