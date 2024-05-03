package ufrn.imd.br.msauthserver.model.builder;


import ufrn.imd.br.msauthserver.model.Doctor;

import java.time.LocalDate;

public class DoctorBuilder {
    private long id;
    private String crm;
    private String name;
    private LocalDate birthDate;

    public DoctorBuilder id(long id){
        this.id = id;
        return this;
    }

    public DoctorBuilder crm(String crm){
        this.crm = crm;
        return this;
    }

    public DoctorBuilder name(String name){
        this.name = name;
        return this;
    }

    public DoctorBuilder birthDate(LocalDate birthDate){
        this.birthDate = birthDate;
        return this;
    }

    public Doctor build(){
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setCrm(crm);
        doctor.setName(name);
        doctor.setBirthDate(birthDate);
        return doctor;
    }
}
