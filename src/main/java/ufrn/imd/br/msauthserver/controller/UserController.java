package ufrn.imd.br.msauthserver.controller;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import ufrn.imd.br.msauthserver.service.DoctorService;
import ufrn.imd.br.msauthserver.service.PatientService;
import ufrn.imd.br.msauthserver.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
public class UserController extends GenericController<User, UserDTO, UserService> {

    private final PatientService patientService;

    private final DoctorService doctorService;

    protected UserController(UserService userService, PatientService patientService, DoctorService doctorService) {
        super(userService);
        this.patientService = patientService;
        this.doctorService = doctorService;
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

    @GetMapping("/patient/check/{id}")
    public ResponseEntity<ApiResponseDTO<Boolean>> isValidPatient(@PathVariable Long id) {
        boolean exists = patientService.existsById(id);
        ApiResponseDTO<Boolean> response = new ApiResponseDTO<>(
                true,
                exists ? "Patient exists." : "Patient does not exist.",
                exists,
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medic/check/{id}")
    public ResponseEntity<ApiResponseDTO<Boolean>> isValidDoctor(@PathVariable Long id) {
        boolean exists = doctorService.existsById(id);
        ApiResponseDTO<Boolean> response = new ApiResponseDTO<>(
                true,
                exists ? "Doctor exists." : "Doctor does not exist.",
                exists,
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/patient/check")
    public ResponseEntity<ApiResponseDTO<Map<Long, Boolean>>> checkPatients(@RequestBody List<Long> patientIds) {
        Map<Long, Boolean> results = patientIds.stream()
                .collect(Collectors.toMap(Function.identity(), patientService::existsById));

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Sucess: Checked patient IDs.",
                results,
                null
        ));
    }

    @GetMapping("/patients/recent")
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> findRecentPatientsByDoctor(
            @RequestParam String doctorId,
            @RequestParam String fromDate,
            @RequestParam int limit) {
        List<UserDTO> recentPatients = service.findRecentPatientsByDoctor(doctorId, LocalDate.parse(fromDate), limit);
        ApiResponseDTO<List<UserDTO>> response = new ApiResponseDTO<>(
                true,
                "Success: Recent patients retrieved successfully.",
                recentPatients,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
