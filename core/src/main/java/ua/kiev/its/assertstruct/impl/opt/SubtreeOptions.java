package ua.kiev.its.assertstruct.impl.opt;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.opt.OptionsBuilder;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@AllArgsConstructor
public class SubtreeOptions {
    boolean orderedDicts;
    boolean orderedLists;
    boolean ignoreUnknown;

    public static SubtreeOptionsBuilder builder() {
        return new SubtreeOptionsBuilder();
    }

    @Data
    public static class SubtreeOptionsBuilder implements OptionsBuilder {
        Boolean orderedDicts;
        Boolean orderedLists;
        Boolean ignoreUnknown;

        @Override
        public NodeOptions.NodeOptionsBuilder set(String name, Object value) {
            if (name.equals("orderedDicts")) {
                orderedDicts = (Boolean) value;
            } else if (name.equals("orderedLists")) {
                orderedLists = (Boolean) value;
            } else if (name.equals("ignoreUnknown")) {
                ignoreUnknown = (Boolean) value;
            } else {
                throw new IllegalArgumentException("Unknown property: " + name + " at SubtreeConfig");
            }
            return null;
        }

        public SubtreeOptions build(SubtreeOptions parent) {
            return new SubtreeOptions(
                    orderedDicts == null ? parent.orderedDicts : orderedDicts,
                    orderedLists == null ? parent.orderedLists : orderedLists,
                    ignoreUnknown == null ? parent.ignoreUnknown : ignoreUnknown);
        }
    }
}