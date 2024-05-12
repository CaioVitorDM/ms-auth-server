package ufrn.imd.br.msauthserver.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.br.msauthserver.dto.ApiResponseDTO;
import ufrn.imd.br.msauthserver.dto.EntityDTO;
import ufrn.imd.br.msauthserver.dto.UserDTO;
import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController extends GenericController<User, UserDTO, UserService> {

    protected UserController(UserService userService) {
        super(userService);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> createMedic(
            @Valid @RequestBody UserDTO dto
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseDTO<>(
                        true,
                        "Sucess: Medic User created successfully.",
                        service.createMedicUser(dto).toResponse(),
                        null
                )
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> createPatient(
            @Valid @RequestBody UserDTO dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseDTO<>(
                        true,
                        "Sucess: Patient User created successfully.",
                        service.createPatientUser(dto).toResponse(),
                        null
                )
        );
    }

    @GetMapping("/find-medic/{id}")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> findUserByMedicId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDTO<>(
                        true,
                        "Sucess: Medic retrieved successfully.",
                        service.getUserByDoctorId(id),
                        null
                )
        );
    }
}
