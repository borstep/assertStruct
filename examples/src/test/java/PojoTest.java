import example.model.Address;
import example.model.Contact;
import example.model.Person;
import org.junit.jupiter.api.Test;
import ua.kiev.its.assertstruct.Res;

import java.net.URL;

import static example.model.ContactType.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.kiev.its.assertstruct.AssertStructUtils.assertStruct;


public class PojoTest {
    public static final Person ALEX = Person.builder()
            .name("Alex")
            .age(25)
            .contact(Contact.builder().type(MAIL).value("alex@example.com").build())
            .contact(Contact.builder().type(PHONE).value("+380123456789").build())
            .address(Address.builder().city("Brussels").street("Rue").build())
            .skill("Java")
            .skill("Spring")
            .build();
    public static final Person BOB = Person.builder()
            .name("Bob")
            .age(20)
            .contact(Contact.builder().type(MAIL).value("bob@example.com").build())
            .contact(Contact.builder().type(PHONE).value("+440123456789").build())
            .address(Address.builder().city("London").street("Baker").build())
            .skill("C")
            .skill("Windows")
            .skill("Linux")
            .build();

    @Test
    public void inlineTest() {
        assertStruct("[1,5,3]", new int[]{1,2,3});
    }

    @Test
    public void arrayTest() {
        assertStruct("./example/array.json5", new int[]{1,2,3});
    }
    @Test
    public void arrayTest1() {
        assertArrayEquals ( new int[]{1,2,4}, new int[]{1,2,3});
    }

    @Test
    public void pojoTest() {
        assertStruct("./example/pojo.json5", ALEX);
    }

    @Test
    public void deepFieldTest() {
        assertStruct("./example/deepField.json5", ALEX);
    }

    @Test
    public void schemeTest() {
        assertStruct("./example/scheme.json5", ALEX);
    }
}
