package ufrn.imd.br.msauthserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Where;
import ufrn.imd.br.msauthserver.model.builder.PatientBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "patients")
@Where(clause = "active = true")
public class Patient extends BaseEntity{

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private LocalDate birthDate;
    @Column(nullable = false)
    private String name;

    public static PatientBuilder builder(){
        return new PatientBuilder();
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Patient that = (Patient) o;
        return Objects.equals(doctorId, that.doctorId) && Objects.equals(name, that.name)
                && Objects.equals(birthDate, that.birthDate) && Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, cpf, birthDate, name);
    }
}
