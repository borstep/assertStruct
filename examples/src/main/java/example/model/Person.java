package example.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
//@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    String name;
    int age;
    Address address;
    @Singular
    List<Contact> contacts;
    @Singular
    List<String> skills;
}
