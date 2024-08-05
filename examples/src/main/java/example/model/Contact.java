package example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
//@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    ContactType type;
    String value;
}
