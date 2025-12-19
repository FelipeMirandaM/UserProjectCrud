package cl.admin.usercrud.services.impl;

import cl.admin.usercrud.dtos.PhoneDTO;
import cl.admin.usercrud.dtos.PhoneResponseDTO;
import cl.admin.usercrud.exception.NotFoundException;
import cl.admin.usercrud.mapper.PhoneMapper;
import cl.admin.usercrud.models.PhoneEntity;
import cl.admin.usercrud.repository.PhoneRepository;
import cl.admin.usercrud.services.IPhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements IPhoneService {

    private final PhoneRepository repository;

    private final PhoneMapper mapper;

    @Override
    public ResponseEntity<List<PhoneResponseDTO>> getAllPhones() {

        List<PhoneEntity> phoneEntityList =  repository.findAll();


        return new ResponseEntity<>(mapper.toResponseDTO(phoneEntityList), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PhoneResponseDTO> getPhoneById(UUID id) {

        PhoneEntity phone =  repository.findById(id).orElseThrow(() -> new NotFoundException("Phone not found"));

        return new ResponseEntity<>(mapper.toResponseDTO(phone), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PhoneResponseDTO> createPhone(PhoneDTO phoneDTO) {

        PhoneEntity userEntity= repository.save(mapper.toEntity(phoneDTO));

        return new ResponseEntity<>(mapper.toResponseDTO(userEntity), HttpStatus.CREATED);
    }

    @Override
    public void deletePhone(UUID id) {

        if(!repository.existsById(id)){
            throw new NotFoundException("phone not found");
        }

        repository.deleteById(id);
    }

    @Override
    public ResponseEntity<PhoneResponseDTO> updatePhone(UUID id, PhoneDTO phoneDTO) {

        if(!repository.existsById(id)){
            throw new NotFoundException("phone not found");
        }

        PhoneEntity entity = mapper.toEntity(phoneDTO);

        entity.setPhoneId(id);

        return new ResponseEntity<>(mapper.toResponseDTO(repository.save(entity)), HttpStatus.OK);
    }
}
