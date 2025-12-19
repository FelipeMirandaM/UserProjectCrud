package cl.admin.usercrud.mapper;


import cl.admin.usercrud.dtos.PhoneDTO;
import cl.admin.usercrud.dtos.PhoneResponseDTO;
import cl.admin.usercrud.models.PhoneEntity;
import cl.admin.usercrud.models.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoneMapper {


    public List<PhoneDTO> toResponse(List<PhoneEntity> phoneEntityList) {

        return phoneEntityList.stream().map(
                phoneEntity -> PhoneDTO.builder()
                        .number(phoneEntity.getNumber())
                        .cityCode(phoneEntity.getCityCode())
                        .countryCode(phoneEntity.getCountryCode())
                        .userId(phoneEntity.getUser().getUserId())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<PhoneEntity> toEntity(List<PhoneDTO> phoneDTOList, UserEntity entity) {

        return phoneDTOList.stream().map(
                phoneEntity -> PhoneEntity.builder()
                        .number(phoneEntity.getNumber())
                        .cityCode(phoneEntity.getCityCode())
                        .countryCode(phoneEntity.getCountryCode())
                        .user(entity)
                        .build()
        ).collect(Collectors.toList());
    }

    public PhoneEntity toEntity(PhoneDTO phoneDTO) {

        return PhoneEntity.builder()
                        .number(phoneDTO.getNumber())
                        .cityCode(phoneDTO.getCityCode())
                        .countryCode(phoneDTO.getCountryCode())
                        .user(UserEntity.builder().userId(phoneDTO.getUserId()).build())
                        .build();
    }

    public List<PhoneResponseDTO> toResponseDTO(List<PhoneEntity> phoneEntityList) {

        return phoneEntityList.stream().map(
                phoneEntity -> PhoneResponseDTO.builder()
                        .number(phoneEntity.getNumber())
                        .cityCode(phoneEntity.getCityCode())
                        .countryCode(phoneEntity.getCountryCode())
                        .phoneId(phoneEntity.getPhoneId())
                        .userId(phoneEntity.getUser().getUserId().toString())
                        .build()
        ).collect(Collectors.toList());
    }

    public PhoneResponseDTO toResponseDTO(PhoneEntity phoneEntity) {

        return PhoneResponseDTO.builder()
                        .number(phoneEntity.getNumber())
                        .cityCode(phoneEntity.getCityCode())
                        .countryCode(phoneEntity.getCountryCode())
                        .phoneId(phoneEntity.getPhoneId())
                        .userId(phoneEntity.getUser().getUserId().toString())
                        .build();
    }

}
