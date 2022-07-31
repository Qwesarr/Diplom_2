package dto;

public class UserDto {
    private String name;
    private String email;
    private String password;

    public UserDto() {
    }

    public UserDto(String name, String email, String password) {                                                        //Конструктор полный
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public UserDto(String email, String password) {                                                                     //Конструктор для логина
        this.email = email;
        this.password = password;
    }
    public String getName() {
        return name;
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
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
