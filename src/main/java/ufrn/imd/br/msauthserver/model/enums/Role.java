package ufrn.imd.br.msauthserver.model.enums;

public enum Role {
    MEDIC("Médico"),
    PATIENT("Paciente");

    private final String description;

    Role(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }
}
