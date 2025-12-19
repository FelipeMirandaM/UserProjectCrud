package cl.admin.usercrud.services;

import cl.admin.usercrud.dtos.PhoneDTO;
import cl.admin.usercrud.dtos.PhoneResponseDTO;
import cl.admin.usercrud.exception.NotFoundException;
import cl.admin.usercrud.mapper.PhoneMapper;
import cl.admin.usercrud.models.PhoneEntity;
import cl.admin.usercrud.repository.PhoneRepository;
import cl.admin.usercrud.services.impl.PhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneServiceImplTest {

    @Mock
    private PhoneRepository repository;

    @Mock
    private PhoneMapper mapper;

    private PhoneServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PhoneServiceImpl(repository, mapper);
    }

    @Test
    void getAllPhones_returnsMappedList() {
        PhoneEntity entity = mock(PhoneEntity.class);
        PhoneResponseDTO dto = mock(PhoneResponseDTO.class);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponseDTO(List.of(entity))).thenReturn(List.of(dto));

        ResponseEntity<List<PhoneResponseDTO>> resp = service.getAllPhones();


        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertSame(dto, resp.getBody().get(0));
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toResponseDTO(List.of(entity));
    }

    @Test
    void getPhoneById_existing_returnsDto() {
        UUID id = UUID.randomUUID();
        PhoneEntity entity = mock(PhoneEntity.class);
        PhoneResponseDTO dto = mock(PhoneResponseDTO.class);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(dto);

        ResponseEntity<PhoneResponseDTO> resp = service.getPhoneById(id);


        assertSame(dto, resp.getBody());
        verify(repository).findById(id);
        verify(mapper).toResponseDTO(entity);
    }

    @Test
    void getPhoneById_missing_throwsUserNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getPhoneById(id));
        verify(repository).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    void createPhone_savesAndReturnsMappedDto() {
        PhoneDTO dtoIn = mock(PhoneDTO.class);
        PhoneEntity toSave = mock(PhoneEntity.class);
        PhoneEntity saved = mock(PhoneEntity.class);
        PhoneResponseDTO dtoOut = mock(PhoneResponseDTO.class);

        when(mapper.toEntity(dtoIn)).thenReturn(toSave);
        when(repository.save(toSave)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(dtoOut);

        ResponseEntity<PhoneResponseDTO> resp = service.createPhone(dtoIn);


        assertSame(dtoOut, resp.getBody());
        verify(mapper).toEntity(dtoIn);
        verify(repository).save(toSave);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void deletePhone_existing_deletes() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        service.deletePhone(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deletePhone_missing_throwsUserNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deletePhone(id));
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void updatePhone_existing_updatesAndReturnsDto() {
        UUID id = UUID.randomUUID();
        PhoneDTO dtoIn = mock(PhoneDTO.class);
        PhoneEntity entityFromDto = mock(PhoneEntity.class);
        PhoneEntity saved = mock(PhoneEntity.class);
        PhoneResponseDTO dtoOut = mock(PhoneResponseDTO.class);

        when(repository.existsById(id)).thenReturn(true);
        when(mapper.toEntity(dtoIn)).thenReturn(entityFromDto);
        // verify setPhoneId is called by ensuring repository.save receives an entity with id set
        // since entityFromDto is a mock, setPhoneId will be invoked on it directly
        when(repository.save(entityFromDto)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(dtoOut);

        ResponseEntity<PhoneResponseDTO> resp = service.updatePhone(id, dtoIn);


        assertSame(dtoOut, resp.getBody());
        verify(repository).existsById(id);
        verify(mapper).toEntity(dtoIn);
        verify(entityFromDto).setPhoneId(id);
        verify(repository).save(entityFromDto);
        verify(mapper).toResponseDTO(saved);
    }

    @Test
    void updatePhone_missing_throwsUserNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.updatePhone(id, mock(PhoneDTO.class)));
        verify(repository).existsById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

}
