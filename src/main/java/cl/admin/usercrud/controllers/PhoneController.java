package cl.admin.usercrud.controllers;

import cl.admin.usercrud.dtos.PhoneDTO;
import cl.admin.usercrud.dtos.PhoneResponseDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import cl.admin.usercrud.exception.ErrorDetails;
import cl.admin.usercrud.services.IPhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/phone")
@RequiredArgsConstructor
@Tag(name = "Phone Management", description = "APIs for managing phones")
public class PhoneController {

    private final IPhoneService service;



    private final String UUID_REGEX = "^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$";

    @Operation(summary = "Get all phones", description = "get all phones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phones retriver successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping(path = "")
    public ResponseEntity<List<PhoneResponseDTO>> getAllPhones(){

        return service.getAllPhones();

    }

    @Operation(summary = "Get a phone by id", description = "get a phone by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phones retriver successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<PhoneResponseDTO> getPhone(@PathVariable String id){

        if (!id.matches(UUID_REGEX)) {
            throw new IllegalArgumentException("Phone not found");
        }

        return service.getPhoneById(UUID.fromString(id));

    }

    @Operation(summary = "Create a phone", description = "create a phones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phones retriver successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping(path = "")
    public ResponseEntity<PhoneResponseDTO> createPhone(@Valid @RequestBody PhoneDTO phoneDTO){

        return service.createPhone(phoneDTO);

    }

    @Operation(summary = "Delete a phones by id", description = "delete a phones by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phones retriver successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable String id){
        if (!id.matches(UUID_REGEX)) {
            throw new IllegalArgumentException("Phone not found");
        }

        service.deletePhone(UUID.fromString(id));

        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    @Operation(summary = "Update a phones by id", description = "update a phones by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phones retriver successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PatchMapping(path = "/{id}")
    public ResponseEntity<PhoneResponseDTO> updatePhone(@PathVariable String id, @Valid @RequestBody PhoneDTO phoneDTO){
        if (!id.matches(UUID_REGEX)) {
            throw new IllegalArgumentException("Phone not found");
        }

        return service.updatePhone(UUID.fromString(id), phoneDTO);

    }


}
