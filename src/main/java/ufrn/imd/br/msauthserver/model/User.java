package ufrn.imd.br.msauthserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ufrn.imd.br.msauthserver.model.builder.UserBuilder;
import ufrn.imd.br.msauthserver.model.enums.Role;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Where(clause = "active = true")
public class User extends BaseEntity implements UserDetails {

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @NotNull
    @Column(nullable = false, unique = true)
    private String login;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false, name = "phone", unique = true)
    private String phoneNumber;

    @Column(name = "img_id")
    private Long imgId;

    public static UserBuilder builder(){
        return new UserBuilder();
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        if(!super.equals(o))
            return false;
        User user = (User) o;
        return Objects.equals(patientId, user.patientId) && Objects.equals(doctorId, user.doctorId)
                && Objects.equals(login, user.login) && Objects.equals(password, user.password)
                && role == user.role && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), patientId, doctorId, login, password, role, email, phoneNumber);
    }
}
