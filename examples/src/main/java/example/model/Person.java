package example.model;

import lombok.*;

import java.util.List;

@SuppressWarnings("unused")
@Data
@Builder
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
