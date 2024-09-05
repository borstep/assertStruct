package example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    ContactType type;
    String value;
}
