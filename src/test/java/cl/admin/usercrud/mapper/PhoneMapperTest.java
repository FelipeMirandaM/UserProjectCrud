package cl.admin.usercrud.mapper;

import cl.admin.usercrud.dtos.PhoneDTO;
import cl.admin.usercrud.dtos.PhoneResponseDTO;
import cl.admin.usercrud.models.PhoneEntity;
import cl.admin.usercrud.models.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PhoneMapperTest {

    private PhoneMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PhoneMapper();
    }

    @Test
    void toResponse_mapsPhoneEntityList_toPhoneDTOList() {
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder().userId(userId).build();

        PhoneEntity entity = PhoneEntity.builder()
                .number("1234")
                .cityCode("02")
                .countryCode("56")
                .user(user)
                .build();

        List<PhoneDTO> result = mapper.toResponse(List.of(entity));

        assertNotNull(result);
        assertEquals(1, result.size());
        PhoneDTO dto = result.get(0);
        assertEquals("1234", dto.getNumber());
        assertEquals("02", dto.getCityCode());
        assertEquals("56", dto.getCountryCode());
        assertEquals(userId, dto.getUserId());
    }

    @Test
    void toEntity_list_mapsPhoneDTOList_and_attachesProvidedUser() {
        UserEntity user = UserEntity.builder().userId(UUID.randomUUID()).build();

        PhoneDTO dto = PhoneDTO.builder()
                .number("555")
                .cityCode("01")
                .countryCode("7")
                .build();

        List<PhoneEntity> entities = mapper.toEntity(List.of(dto), user);

        assertNotNull(entities);
        assertEquals(1, entities.size());
        PhoneEntity e = entities.get(0);
        assertEquals("555", e.getNumber());
        assertEquals("01", e.getCityCode());
        assertEquals("7", e.getCountryCode());
        assertSame(user, e.getUser());
    }

    @Test
    void toEntity_single_mapsPhoneDTO_and_setsUserFromUserId() {
        UUID userId = UUID.randomUUID();
        PhoneDTO dto = PhoneDTO.builder()
                .number("9999")
                .cityCode("03")
                .countryCode("99")
                .userId(userId)
                .build();

        PhoneEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("9999", entity.getNumber());
        assertEquals("03", entity.getCityCode());
        assertEquals("99", entity.getCountryCode());
        assertNotNull(entity.getUser());
        assertEquals(userId, entity.getUser().getUserId());
    }

    @Test
    void toResponseDTO_single_and_list_includePhoneId_andUserIdAsString() {
        UUID userId = UUID.randomUUID();
        UUID phoneId = UUID.randomUUID();
        UserEntity user = UserEntity.builder().userId(userId).build();

        PhoneEntity entity = PhoneEntity.builder()
                .phoneId(phoneId)
                .number("777")
                .cityCode("04")
                .countryCode("11")
                .user(user)
                .build();

        PhoneResponseDTO single = mapper.toResponseDTO(entity);
        assertEquals("777", single.getNumber());
        assertEquals("04", single.getCityCode());
        assertEquals("11", single.getCountryCode());
        assertEquals(phoneId, single.getPhoneId());
        assertEquals(userId.toString(), single.getUserId());

        List<PhoneResponseDTO> list = mapper.toResponseDTO(List.of(entity));
        assertNotNull(list);
        assertEquals(1, list.size());
        PhoneResponseDTO dtoFromList = list.get(0);
        assertEquals(single.getPhoneId(), dtoFromList.getPhoneId());
        assertEquals(single.getUserId(), dtoFromList.getUserId());
    }


}
