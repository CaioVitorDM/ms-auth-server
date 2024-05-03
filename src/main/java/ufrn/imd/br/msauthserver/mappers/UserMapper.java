package ufrn.imd.br.msauthserver.mappers;

import org.springframework.stereotype.Component;
import ufrn.imd.br.msauthserver.dto.UserDTO;
import ufrn.imd.br.msauthserver.model.Doctor;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.repository.DoctorRepository;
import ufrn.imd.br.msauthserver.repository.PatientRepository;
import ufrn.imd.br.msauthserver.repository.UserRepository;
import ufrn.imd.br.msauthserver.utils.exception.ResourceNotFoundException;


@Component
public class UserMapper implements DtoMapper<User, UserDTO>{


    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;

    public UserMapper(PatientRepository patientRepository,
                      DoctorRepository doctorRepository,
                      PatientMapper patientMapper,
                      DoctorMapper doctorMapper) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.patientMapper = patientMapper;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public UserDTO toDto(User entity){
        Patient patient = patientRepository.getPatientById(entity.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o id: " + entity.getPatientId()));

        Doctor doctor = doctorRepository.getDoctorById(entity.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com o id: " + entity.getDoctorId()));


        return new UserDTO(
                entity.getId(),
                patientMapper.toDto(patient),
                doctorMapper.toDto(doctor),
                entity.getLogin(),
                entity.getPassword(),
                entity.getRole(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getImgId()
        );
    }

    @Override
    public User toEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.id())
                .patientId(userDTO.patient().id())
                .doctorId(userDTO.doctor().id())
                .login(userDTO.login())
                .password(userDTO.password())
                .role(userDTO.role())
                .email(userDTO.email())
                .phoneNumber(userDTO.phoneNumber())
                .imgId(userDTO.imgId())
                .build();
    }
}
