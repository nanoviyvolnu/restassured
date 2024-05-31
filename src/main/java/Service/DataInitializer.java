package Service;

import Models.CreateUserRequest;
import Utils.GenerateValidPassword;
import com.github.javafaker.Faker;
import lombok.Data;

@Data
public class DataInitializer {
    private Faker faker;
    private GenerateValidPassword generateValidPassword;
    private CreateUserRequest createUserRequest;

    public void initialize() {
        faker = new Faker();
        generateValidPassword = new GenerateValidPassword();
        createUserRequest = new CreateUserRequest();
        createUserRequest.setUserName(faker.name().username());
        createUserRequest.setPassword(generateValidPassword.generatePassword(faker));
    }

    public void initializeDefaultData(){
        createUserRequest = new CreateUserRequest();
        createUserRequest.setUserName("myriam.marvin");
        createUserRequest.setPassword("Vz7<#Q<Y");
    }
}
