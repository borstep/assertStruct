package ua.kiev.its.assertstruct.impl.config;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.config.ConfigBuilder;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@AllArgsConstructor
public class SubtreeConfig {
    boolean orderedDicts;
    boolean orderedLists;
    boolean ignoreUnknown;

    public static SubtreeConfigBuilder builder() {
        return new SubtreeConfigBuilder();
    }

    @Data
    public static class SubtreeConfigBuilder implements ConfigBuilder {
        Boolean orderedDicts;
        Boolean orderedLists;
        Boolean ignoreUnknown;

        @Override
        public NodeConfig.NodeConfigBuilder set(String name, Object value) {
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

        public SubtreeConfig build(SubtreeConfig parent) {
            return new SubtreeConfig(
                    orderedDicts == null ? parent.orderedDicts : orderedDicts,
                    orderedLists == null ? parent.orderedLists : orderedLists,
                    ignoreUnknown == null ? parent.ignoreUnknown : ignoreUnknown);
        }
    }
}
