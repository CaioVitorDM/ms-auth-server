package ufrn.imd.br.msauthserver.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.dto.DoctorDTO;
import ufrn.imd.br.msauthserver.dto.PatientDTO;
import ufrn.imd.br.msauthserver.dto.UserDTO;
import ufrn.imd.br.msauthserver.mappers.DtoMapper;
import ufrn.imd.br.msauthserver.mappers.UserMapper;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.model.enums.Role;
import ufrn.imd.br.msauthserver.repository.GenericRepository;
import ufrn.imd.br.msauthserver.repository.UserRepository;
import ufrn.imd.br.msauthserver.utils.exception.BusinessException;
import ufrn.imd.br.msauthserver.utils.exception.ResourceNotFoundException;

@Transactional
@Service
public class UserService implements GenericService<User, UserDTO> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       DoctorService doctorService,
                       PatientService patientService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Override
    public GenericRepository<User> getRepository() {
        return this.userRepository;
    }

    @Override
    public DtoMapper<User, UserDTO> getDtoMapper() {
        return this.userMapper;
    }

    @Override
    public void validateBeforeSave(User entity){
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        GenericService.super.validateBeforeSave(entity);
        validateLogin(entity.getLogin());
        validateEmail(entity.getEmail());
        validatePhoneNumber(entity.getPhoneNumber());
    }

    public UserDTO createMedicUser(UserDTO dto){
        DoctorDTO doctor = doctorService.createDoctor(dto.doctor());

        User entity = getDtoMapper().toEntity(dto);
        entity.setDoctorId(doctor.id());
        entity.setRole(Role.MEDIC);
        validateBeforeSave(entity);

        return getDtoMapper().toDto(getRepository().save(entity));

    }

    public UserDTO createPatientUser(UserDTO dto){
        PatientDTO patient = patientService.createPatient(dto.patient());

        User entity = getDtoMapper().toEntity(dto);
        entity.setPatientId(patient.id());
        entity.setRole(Role.PATIENT);
        validateBeforeSave(entity);

        return getDtoMapper().toDto(getRepository().save(entity));
    }

    public UserDTO getUserByDoctorId(Long id){
        User user = this.userRepository.findByDoctorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário médico não encontrado com o id: " + id));

        return this.getDtoMapper().toDto(user);
    }

    private void validateLogin(String login){
        if(userRepository.existsByLoginIgnoreCase(login)){
            throw new BusinessException(
                    "Login inválido: " + login + ". Já existe um usuário cadastrado com esse login.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateEmail(String email){
        if(userRepository.existsByEmailIgnoreCase(email)){
            throw new BusinessException(
                    "Email inválido: " + email + ". Já existe um usuário cadastrado com esse email.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePhoneNumber(String phone){
        if(userRepository.existsByPhoneNumberIgnoreCase(phone)){
            throw new BusinessException(
                    "Telefone inválido: " + phone + ". Já existe um usuário cadastrado com esse número de telefone.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
