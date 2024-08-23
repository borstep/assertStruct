package org.assertstruct.service;

import com.fasterxml.jackson.core.JsonFactory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import org.assertstruct.converter.JsonConverter;
import org.assertstruct.impl.converter.jackson.JacksonConverter;
import org.assertstruct.impl.factories.variable.DefaultConstantService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertstruct.service.ConfigDefaults.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder(toBuilder = true, buildMethodName = "internalBuildConfig", builderMethodName = "")
public class Config {

    @Builder.Default
    ConstantService constantService = new DefaultConstantService();
    @Singular
    List<String> dateFormats;
    @Singular
    List<String> dateTimeFormats;
    @Singular
    List<String> timeFormats;
    @Builder.Default
    boolean defaultIgnoreUnknown = false;
    @Builder.Default
    String defaultMessage = "Actual object doesn't match provided template,";
    @Builder.Default
    boolean defaultOrderedDicts = false;
    @Builder.Default
    boolean defaultOrderedLists = true;
    @Singular
    List<String> defaultPackages;
    @Builder.Default
    boolean emptyDictOnSameLine = true;
    @Builder.Default
    boolean emptyListOnSameLine = true;
    @Singular(value = "ext")
    Map<String, String> ext;
    @Builder.Default
    boolean forceKeyQuoting = false;
    @Builder.Default
    boolean generateDiff = true;
    @Builder.Default
    String generatePath = "assert-struct";
    @Builder.Default
    boolean generateValueFile = true;
    @Builder.Default
    int indent = 2;
    @Builder.Default
    JsonFactory json5Factory = buildDefaultJson5Factory();
    @Builder.Default
    JsonConverter jsonConverter = new JacksonConverter();
    @Singular
    List<ParserFactory> parserFactories;
    @Builder.Default
    char quote = '\'';
    @Singular
    List<String> srcPaths; // defaults in builder
    @Builder.Default
    String target = "target";
    @Singular
    List<String> targetPaths; // defaults in builder
    @Builder.Default
    private boolean nowStrictCheck=true;

    /**
     * $NOW precision in seconds
     */
    @Builder.Default
    private int nowPrecision = 60;

    public static class ConfigBuilder {
        ConfigBuilder() {
            // Set some defaults
            this
                    .defaultPackages(DEFAULT_PACKAGES)
                    .targetPaths(DEFAULT_TARGET_PATHS)
                    .srcPaths(DEFAULT_SRC_PATHS)
                    .parserFactories(DEFAULT_PARSER_FACTORIES);
        }

        public ConfigBuilder parserFactoryClass(String className) {
            try {
                return parserFactory((ParserFactory) Class.forName(className).newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public ConfigBuilder dateFormatList(String dateFormats) {
            this.clearDateFormats();
            return this.dateFormats(Arrays.asList(dateFormats.split(",")));
        }

        public ConfigBuilder dateTimeFormatList(String dateTimeFormats) {
            this.clearDateTimeFormats();
            return this.dateTimeFormats(Arrays.asList(dateTimeFormats.split(",")));
        }

        public ConfigBuilder timeFormatList(String timeFormats) {
            this.clearTimeFormats();
            return this.timeFormats(Arrays.asList(timeFormats.split(",")));
        }

        public ConfigBuilder srcPathList(String srcPaths) {
            this.clearSrcPaths();
            return this.srcPaths(Arrays.asList(srcPaths.split(",")));
        }

        public ConfigBuilder targetPathList(String targetPaths) {
            this.clearTargetPaths();
            return this.targetPaths(Arrays.asList(targetPaths.split(",")));
        }

        public ConfigBuilder parserFactoryClasses(String classNames) {
            String[] classes = classNames.split(",");
            for (String className : classes) {
                parserFactoryClass(className);
            }
            return this;
        }

        public AssertStructService configure() {
            return build();
        }

        public AssertStructService build() {
            return new AssertStructService(this.internalBuildConfig());
        }
    }

}
