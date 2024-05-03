package ufrn.imd.br.msauthserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Where;
import ufrn.imd.br.msauthserver.model.builder.DoctorBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "doctors")
@Where(clause = "active = true")
public class Doctor extends BaseEntity{
    @Column(nullable = false, unique = true)
    private String crm;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate birthDate;

    public static DoctorBuilder builder(){
        return new DoctorBuilder();
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Doctor that = (Doctor) o;
        return Objects.equals(crm, that.crm) && Objects.equals(name, that.name)
                && Objects.equals(birthDate, that.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crm, name, birthDate);
    }

}
