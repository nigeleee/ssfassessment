package vttp2023.batch3.ssf.frontcontroller.model;

import java.io.Serializable;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Login implements Serializable{
    
    @NotNull (message="Field cannot be empty")
    @NotBlank (message="Enter Username")
    @Size (min = 2, message = "Username must be at least 2 characters")
    private String username;

    @NotNull (message="Field cannot be empty")
    @NotBlank (message="Enter Password")
    @Size (min = 2, message = "Password must be at least 2 characters")
    private String password;

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

    public static Login create(String json) {
        JsonObject o = Json.createReader(new StringReader(json)).readObject();
        Login login = new Login();
        login.setUsername(o.getString("username"));
        login.setPassword(o.getString("password"));
        return login;
    }
    public JsonObject toJSON() {
        return Json.createObjectBuilder()
        .add("username", this.getUsername())
        .add("password", this.getPassword())
        .build();
    } 
}
