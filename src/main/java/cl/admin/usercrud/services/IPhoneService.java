package cl.admin.usercrud.services;

import cl.admin.usercrud.dtos.PhoneDTO;
import cl.admin.usercrud.dtos.PhoneResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IPhoneService {

    ResponseEntity<List<PhoneResponseDTO>> getAllPhones();
    ResponseEntity<PhoneResponseDTO> getPhoneById(UUID id);
    ResponseEntity<PhoneResponseDTO> createPhone(PhoneDTO phoneDTO);
    void deletePhone(UUID id);
    ResponseEntity<PhoneResponseDTO> updatePhone(UUID id,PhoneDTO phoneDTO);

}
