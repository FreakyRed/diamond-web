package sk.tuke.gamestudio.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="users")
@NamedQuery(name = "getLogin",
query = "SELECT u FROM User u WHERE u.username=:username AND u.password=:password")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true)
    private String username;

    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = new BCryptPasswordEncoder(12).encode(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
