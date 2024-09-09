package org.assertstruct.impl.opt;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.assertstruct.opt.OptionsBuilder;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@AllArgsConstructor
public class SubtreeOptions {
    public static final SubtreeOptions INITIAL = new SubtreeOptions(null, null, null);
    Boolean orderedDicts;
    Boolean orderedLists;
    Boolean ignoreUnknown;

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
            //noinspection IfCanBeSwitch
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
