package cl.admin.usercrud.controllers;

import cl.admin.usercrud.dtos.UserDTO;
import cl.admin.usercrud.dtos.UserResponseDTO;
import cl.admin.usercrud.exception.ErrorDetails;
import cl.admin.usercrud.services.IUserService;
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
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController{

    private final IUserService service;



    private final String UUID_REGEX = "^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$";

    @Operation(summary = "Get all users", description = "get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retriver successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping(path = "")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){

        return service.getAllUsers();

    }

    @Operation(summary = "Get a user by id", description = "Get a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retriver successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String id){

        if (!id.matches(UUID_REGEX)) {
            throw new IllegalArgumentException("User not found");
        }

        return service.getUserById(UUID.fromString(id));

    }

    @Operation(summary = "Create a user", description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping(path = "")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO){

        return service.createUser(userDTO);

    }

    @Operation(summary = "disable a user by id", description = "disable a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User disabled successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        if (!id.matches(UUID_REGEX)) {
            throw new IllegalArgumentException("User not found");
        }

        service.deleteUser(UUID.fromString(id));

        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }


    @Operation(summary = "Update a user by id", description = "Update a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDTO){
        if (!id.matches(UUID_REGEX)) {
            throw new IllegalArgumentException("User not found");
        }

        return service.updateUser(UUID.fromString(id), userDTO);

    }
}
