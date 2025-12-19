package cl.admin.usercrud.controllers;


import cl.admin.usercrud.dtos.PhoneDTO;
import cl.admin.usercrud.dtos.PhoneResponseDTO;
import cl.admin.usercrud.services.IPhoneService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneControllerTest {


    @Mock
    private IPhoneService service;

    @InjectMocks
    private PhoneController controller;

    @Test
    void getAllPhones_returnsServiceResponse() {
        PhoneResponseDTO phone = mock(PhoneResponseDTO.class);
        when(service.getAllPhones()).thenReturn(ResponseEntity.ok(List.of(phone)));

        ResponseEntity<List<PhoneResponseDTO>> resp = controller.getAllPhones();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void getPhone_invalidUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> controller.getPhone("not-a-uuid"));
    }

    @Test
    void getPhone_validId_returnsServiceResponse() {
        String id = UUID.randomUUID().toString();
        PhoneResponseDTO phone = mock(PhoneResponseDTO.class);
        when(service.getPhoneById(UUID.fromString(id))).thenReturn(ResponseEntity.ok(phone));

        ResponseEntity<PhoneResponseDTO> resp = controller.getPhone(id);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(phone, resp.getBody());
    }

    @Test
    void createPhone_delegatesToService_andReturnsResponse() {
        PhoneDTO dto = mock(PhoneDTO.class);
        PhoneResponseDTO created = mock(PhoneResponseDTO.class);
        when(service.createPhone(dto)).thenReturn(ResponseEntity.ok(created));

        ResponseEntity<PhoneResponseDTO> resp = controller.createPhone(dto);

        verify(service, times(1)).createPhone(dto);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(created, resp.getBody());
    }

    @Test
    void deletePhone_invalidUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> controller.deletePhone("bad-id"));
    }

    @Test
    void deletePhone_validId_callsService_andReturnsAccepted() {
        String id = UUID.randomUUID().toString();
        doNothing().when(service).deletePhone(any(UUID.class));

        ResponseEntity<Void> resp = controller.deletePhone(id);

        verify(service, times(1)).deletePhone(UUID.fromString(id));
        assertEquals(HttpStatus.ACCEPTED, resp.getStatusCode());
    }

    @Test
    void updatePhone_invalidUuid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> controller.updatePhone("xxx", mock(PhoneDTO.class)));
    }

    @Test
    void updatePhone_validId_returnsServiceResponse() {
        String id = UUID.randomUUID().toString();
        PhoneDTO dto = mock(PhoneDTO.class);
        PhoneResponseDTO updated = mock(PhoneResponseDTO.class);
        when(service.updatePhone(UUID.fromString(id), dto)).thenReturn(ResponseEntity.ok(updated));

        ResponseEntity<PhoneResponseDTO> resp = controller.updatePhone(id, dto);

        verify(service, times(1)).updatePhone(UUID.fromString(id), dto);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(updated, resp.getBody());

    }

}
