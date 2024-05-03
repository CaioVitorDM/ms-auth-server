package ufrn.imd.br.msauthserver.model.builder;

import ufrn.imd.br.msauthserver.model.Patient;

import java.time.LocalDate;

public class PatientBuilder {
    private long id;
    private Long doctorId;
    private String cpf;
    private LocalDate birthDate;
    private String name;

    public PatientBuilder id(long id){
        this.id = id;
        return this;
    }

    public PatientBuilder doctorId(Long doctorId){
        this.doctorId = doctorId;
        return this;
    }

    public PatientBuilder cpf(String cpf){
        this.cpf = cpf;
        return this;
    }

    public PatientBuilder birthDate(LocalDate birthDate){
        this.birthDate = birthDate;
        return this;
    }

    public PatientBuilder name(String name){
        this.name = name;
        return this;
    }

    public Patient build(){
        Patient patient = new Patient();
        patient.setId(id);
        patient.setDoctorId(doctorId);
        patient.setCpf(cpf);
        patient.setBirthDate(birthDate);
        patient.setName(name);
        return patient;
    }
}
