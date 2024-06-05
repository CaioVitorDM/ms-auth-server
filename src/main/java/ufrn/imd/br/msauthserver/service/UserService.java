package ufrn.imd.br.msauthserver.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.dto.DoctorDTO;
import ufrn.imd.br.msauthserver.dto.PatientDTO;
import ufrn.imd.br.msauthserver.dto.UserDTO;
import ufrn.imd.br.msauthserver.mappers.DtoMapper;
import ufrn.imd.br.msauthserver.mappers.UserMapper;
import ufrn.imd.br.msauthserver.model.Doctor;
import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.model.enums.Role;
import ufrn.imd.br.msauthserver.repository.GenericRepository;
import ufrn.imd.br.msauthserver.repository.UserRepository;
import ufrn.imd.br.msauthserver.utils.exception.BusinessException;
import ufrn.imd.br.msauthserver.utils.exception.ResourceNotFoundException;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public List<UserDTO> findPatientsByDoctorId(String doctorId, String login, String name) {
        List<User> patients = userRepository.searchByFilters(doctorId, login, name);
        return getDtoMapper().toDto(patients);
    }
    @Override
    public void validateBeforeSave(User entity){
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        GenericService.super.validateBeforeSave(entity);
        validateLogin(entity.getLogin(), entity.getId());
        validateEmail(entity.getEmail(), entity.getId());
        validatePhoneNumber(entity.getPhoneNumber(), entity.getId());
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

    private void validateLogin(String login, Long id){
        if(login != null){
            Optional<User> user = userRepository.findByLoginIgnoreCase(login);
            if (user.isPresent() && (id == null || user.get().getId() != id)){
                throw new BusinessException(
                        "Login inválido: " + login + ". Já existe um usuário cadastrado com esse login.",
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        else{
            throw new BusinessException("Error: O login não pode ser nulo.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmail(String email, Long id){
        if(email != null){
            Optional<User> user = userRepository.findByEmailIgnoreCase(email);
            if(user.isPresent() && (id == null || user.get().getId() != id)){
                throw new BusinessException(
                        "Email inválido: " + email + ". Já existe um usuário cadastrado com esse email.",
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        else{
            throw new BusinessException("Error: O email não pode ser nulo.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validatePhoneNumber(String phone, Long id){
        if(phone != null){
            Optional<User> user = userRepository.findByPhoneNumberIgnoreCase(phone);
            if(user.isPresent() && (id == null || user.get().getId() != id)){
                throw new BusinessException(
                        "Telefone inválido: " + phone + ". Já existe um usuário cadastrado com esse número de telefone.",
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        else{
            throw new BusinessException("Error: O número de telefone não pode ser nulo.", HttpStatus.BAD_REQUEST);
        }
    }

    public Page<UserDTO> findByFilters(String name, String email, String phoneNumber, String doctorId, Pageable pageable) {
        return userRepository.searchByFilters(name, email, phoneNumber, doctorId, pageable).map(userMapper::toDto);
    }

    public UserDTO updatePatientUser(UserDTO dto) {
        User updatedEntity = userMapper.toEntity(dto);
        Long userId = dto.id();

        User bdEntity = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
                "Error: User not found with id [" + userId + "]", HttpStatus.NOT_FOUND
        ));

        PatientDTO patient = patientService.updatePatient(dto.patient());

        BeanUtils.copyProperties(updatedEntity, bdEntity, getNullPropertyNames(updatedEntity));

        bdEntity.setPatientId(patient.id());
        bdEntity.setRole(Role.PATIENT);
        validateBeforeSave(bdEntity);

        return getDtoMapper().toDto(getRepository().save(bdEntity));
    }

    public UserDTO updateMedicUser(UserDTO dto){
        User updatedEntity = userMapper.toEntity(dto);
        Long userId = dto.id();

        User bdEntity = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
                "Error: User not found with id [" + userId + "]", HttpStatus.NOT_FOUND
        ));

        DoctorDTO doctor = doctorService.updateDoctor(dto.doctor());

        BeanUtils.copyProperties(updatedEntity, bdEntity, getNullPropertyNames(updatedEntity));

        bdEntity.setPatientId(doctor.id());
        bdEntity.setRole(Role.MEDIC);
        validateBeforeSave(bdEntity);

        return getDtoMapper().toDto(getRepository().save(bdEntity));
    }

    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            if(user.get().getRole().equals(Role.MEDIC)){
                this.doctorService.deleteById(user.get().getDoctorId());
            }
            else{
                this.patientService.deleteById(user.get().getPatientId());
            }
        }
        GenericService.super.deleteById(id);
    }

    /**
     * Retrieves the names of properties with null values from the given object.
     *
     * @param source The object from which to extract property names.
     * @return An array of property names with null values.
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
