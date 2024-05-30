package Utils;

import com.github.javafaker.Faker;

public class GenerateValidPassword {
    public String generatePassword(Faker faker) {
        String upperCase = faker.regexify("[A-Z]{1}");
        String lowerCase = faker.regexify("[a-z]{1}");
        String digit = faker.regexify("[0-9]{1}");
        String specialChar = faker.regexify("[!@#$%^&*()_+<>?]{1}");
        String otherChars = faker.regexify("[A-Za-z0-9!@#$%^&*()_+<>?]{4}");

        return upperCase + lowerCase + digit + specialChar + otherChars;
    }
}
