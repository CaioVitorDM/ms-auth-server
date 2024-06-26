package ufrn.imd.br.msauthserver.controller;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.br.msauthserver.dto.ApiResponseDTO;
import ufrn.imd.br.msauthserver.dto.EntityDTO;
import ufrn.imd.br.msauthserver.dto.PatientDTO;
import ufrn.imd.br.msauthserver.dto.UserDTO;
import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController extends GenericController<User, UserDTO, UserService> {

    protected UserController(UserService userService) {
        super(userService);
    }

    @GetMapping("/patients/doctor")
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> findPatientsByDoctorId(@RequestParam(required = false) String doctorId,
                                                                                @RequestParam(required = false) String login,
                                                                                @RequestParam(required = false) String name) {
        List<UserDTO> patients = service.findPatientsByDoctorId(doctorId, login, name);
        ApiResponseDTO<List<UserDTO>> response = new ApiResponseDTO<>(
                true,
                "Success: Patients retrieved successfully.",
                patients,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
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

    @GetMapping("/find-patients")
    public ResponseEntity<ApiResponseDTO<Page<UserDTO>>> findPatientsFromMedic(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String doctorId)
    {
            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    "Sucess: patients retrieved sucessfully",
                    service.findByFilters(name, email, phoneNumber, doctorId, pageable),
                    null
            ));
    }


    @PutMapping("/edit-patient")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updatePatientUser
            (@Valid @RequestBody UserDTO dto){

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDTO<>(
                        true,
                        "Sucesso: Patient user edited.",
                        service.updatePatientUser(dto),
                        null
                )
        );
    }

    @PutMapping("/edit-doctor")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateMedicUser
            (@Valid @RequestBody UserDTO dto){

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDTO<>(
                        true,
                        "Sucesso: Patient user edited.",
                        service.updateMedicUser(dto),
                        null
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UserDTO>> delete(Long id) {
        service.deleteUserById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Sucess: Entity has been successfully removed.",
                null,
                null));
    }
}
