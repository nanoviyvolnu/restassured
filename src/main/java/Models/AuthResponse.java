package Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    public String userId;
    public String username;
    public ArrayList<Books> books;
    public String code;
    public String message;
}
