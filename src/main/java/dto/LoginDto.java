package dto;


public class LoginDto {
    private String email;
    private String password;

    public LoginDto(String email, String password) {                                                                    //Конструктор для логина
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
