package cl.admin.usercrud.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PhoneDTO {

    @Size(min = 8, message = "the phone number must have at least 8 numbers")
    private String number;

    @NotBlank(message = "The city code must be provided")
    @JsonProperty("citycode")
    private String cityCode;

    @NotBlank(message = "The country code must be provided")
    @JsonProperty("contrycode")
    private String countryCode;

    private UUID userId;
}
