package ufrn.imd.br.msauthserver.model.builder;


import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.model.enums.Role;

public class UserBuilder {
    private long id;
    private Long patientId;
    private Long doctorId;
    private String login;
    private String password;
    private Role role;
    private String email;
    private String phoneNumber;
    private Long imgId;

    public UserBuilder id(long id){
        this.id = id;
        return this;
    }

    public UserBuilder patientId(Long patientId){
        this.patientId = patientId;
        return this;
    }

    public UserBuilder doctorId(Long doctorId){
        this.doctorId = doctorId;
        return this;
    }

    public UserBuilder login(String login){
        this.login = login;
        return this;
    }

    public UserBuilder password(String password){
        this.password = password;
        return this;
    }

    public UserBuilder role(Role role){
        this.role = role;
        return this;
    }

    public UserBuilder email(String email){
        this.email = email;
        return this;
    }

    public UserBuilder phoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserBuilder imgId(Long imgId){
        this.imgId = imgId;
        return this;
    }

    public User build(){
        User user = new User();
        user.setId(id);
        user.setPatientId(patientId);
        user.setDoctorId(doctorId);
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setImgId(imgId);
        return user;
    }
}
