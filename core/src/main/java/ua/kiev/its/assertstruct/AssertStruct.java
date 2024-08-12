package ua.kiev.its.assertstruct;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ua.kiev.its.assertstruct.config.*;
import ua.kiev.its.assertstruct.converter.JsonConverter;
import ua.kiev.its.assertstruct.impl.config.ConfigParsingFactory;
import ua.kiev.its.assertstruct.impl.config.SubtreeConfig;
import ua.kiev.its.assertstruct.impl.converter.jackson.JacksonConverter;
import ua.kiev.its.assertstruct.impl.factories.any.ShortAnyFactory;
import ua.kiev.its.assertstruct.impl.factories.array.RepeaterFactory;
import ua.kiev.its.assertstruct.impl.factories.regexp.RegexpFactory;
import ua.kiev.its.assertstruct.impl.factories.spel.SpelFactoryRegistrar;
import ua.kiev.its.assertstruct.impl.factories.variable.ConstantFactory;
import ua.kiev.its.assertstruct.impl.factories.variable.DefaultConstantService;
import ua.kiev.its.assertstruct.result.Json5Printer;
import ua.kiev.its.assertstruct.result.MatchResult;
import ua.kiev.its.assertstruct.result.RootResult;
import ua.kiev.its.assertstruct.template.Template;
import ua.kiev.its.assertstruct.utils.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.*;

import static ua.kiev.its.assertstruct.utils.ResourceUtils.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssertStruct implements ResourceSourceLocator {

    private static final ParsingFactory[] defaultFactories = new ParsingFactory[]{
            RepeaterFactory.INSTANCE,
            ConfigParsingFactory.INSTANCE,
            ShortAnyFactory.INSTANCE,
            RegexpFactory.INSTANCE,
    };

    private static final ParsingFactoryRegistrar[] defaultRegistrar = new ParsingFactoryRegistrar[]{
            SpelFactoryRegistrar.INSTANCE
    };
    private static final AssertStruct defaultInstance = buildDefault();

    @Getter
    private List<String> defaultPackages = Arrays.asList("java.lang", "java.util");

    static AssertStruct buildDefault() {
        AssertStruct conf = new AssertStruct();
        for (ParsingFactoryRegistrar registrar : defaultRegistrar) {
            registrar.registerFactory(conf);
        }
        for (ParsingFactory factory : defaultFactories) {
            conf.registerParsingFactory(factory);
        }
        conf.registerParsingFactory(new ConstantFactory(conf));
        return conf;
    }

    @Getter
    JsonFactory json5Factory = buildJson5Factory();

    @Getter
    SortedSet<NodeFactory> nodeMatchingFactories = new TreeSet<>(ParsingFactoryComparator.INSTANCE);
    @Getter
    SortedSet<KeyFactory> keyMatchingFactories = new TreeSet<>(ParsingFactoryComparator.INSTANCE);
    private int indent = 2;
    private char quote = '\'';
    private boolean forceKeyQuoting = false;
    @Getter
    private boolean emptyListOnSameLine = true;
    @Getter
    private boolean emptyDictOnSameLine = true;
    private String defaultMessage = "Actual object doesn't match provided template,";
    private List<String> targetPaths = Arrays.asList(
            "/target/classes/",
            "/target/test-classes/",
            "/out/classes/",
            "/out/test-classes/"
    );
    private List<String> srcPaths = Arrays.asList(
            "/src/main/java/",
            "/src/main/resources/",
            "/src/test/java/",
            "/src/test/resources/"
    );
    @Getter
    private String targetPath = "target";

    @Getter
    private ConstantService constantService = new DefaultConstantService();

    @Getter
    private String generatePath = "assert-struct";
    private boolean generate = true;
    @Getter
    private JsonConverter jsonConverter = new JacksonConverter();


    private List<ResourceLocation> targetResources = new ArrayList<>();

    @Getter
    SubtreeConfig subtreeConfig = new SubtreeConfig(
            false,
            true,
            false
    );

    private JsonFactory buildJson5Factory() {
        return JsonFactory.builder()
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS)
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
    }

    public static AssertStruct getDefaultInstance() {
        return defaultInstance;
    }

    public void registerParsingFactory(ParsingFactory factory) {
        if (factory instanceof KeyFactory) {
            keyMatchingFactories.add((KeyFactory) factory);
        }
        if (factory instanceof NodeFactory) {
            nodeMatchingFactories.add((NodeFactory) factory);
        }
    }


    public int getIndent() {
        return indent;
    }


    public char getQuote() {
        return quote;
    }

    public boolean getForceKeyQuoting() {
        return forceKeyQuoting;
    }

    @Override
    public ResourceLocation lookupSourceLocation(ResourceLocation location) {
        try {
            File file = location.toFile();
            String uri = file.getAbsoluteFile().toURI().getPath();
            String rootPart = null;
            String suffixPath = null;
            for (String path : targetPaths) {
                if (uri.contains(path)) {
                    rootPart = uri.substring(0, uri.indexOf(path));
                    suffixPath = uri.substring(uri.indexOf(path) + path.length());
                    break;
                }
            }
            if (rootPart == null) {
                return location;
            }
            File root = new File(rootPart);
            for (String path : srcPaths) {
                File srcFolder = new File(root, path);
                if (srcFolder.exists()) {
                    File srcFile = new File(srcFolder, suffixPath);
                    if (location.isClass()) {
                        srcFile = new File(srcFile.getParentFile(), location.getFileName());
                    }
                    if (srcFile.exists() && srcFile.isFile()) {
                        return location.withUrl(srcFile.toURI().toURL());
                    }
                }
            }
        } catch (MalformedURLException e) {// ignore if URL can't be converted into file, usually will not happen
        }
        return location;
    }

    public void match(@NonNull Res expected, Object actualValue) {
        StackTraceElement el = codeLocator();
        match(expected, actualValue, null, el);
    }

    public void match(@NonNull String expected, Object actualValue) {
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, this);
        match(res, actualValue, null, el);
    }

    public void match(@NonNull String expected, Object actualValue, String message) {
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, this);
        match(res, actualValue, message, el);
    }

    public void match(@NonNull Res expected, Object pojoActualValue, String message, StackTraceElement el) {
        Template template = expected.asTemplate();
        RootResult match = template.match(pojoActualValue);
        if (match.hasDifference()) {
            if (el == null) {
                el = codeLocator();
            }
            ResourceLocation testLocation = ResourceLocation.fromStackTrace(el);
            File targetDir = new File(targetPath);
            File generateDir = new File(targetDir, generatePath);
            if (!generateDir.exists()) {

            }
            File matchFile = new File(generateDir, testLocation.getClassName().replace(".", "/")
                    + "_" + testLocation.getMethodName() + "_" + testLocation.getLineNumber() + ".json5");
            File valueFile = new File(generateDir, testLocation.getClassName().replace(".", "/")
                    + "_" + testLocation.getMethodName() + "_" + testLocation.getLineNumber() + ".value.json5");
            File parentFile = matchFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            save(match, matchFile);
            ResourceLocation matchLocation = ResourceLocation.fromFile(matchFile);
            StringBuilder errMessage = new StringBuilder(message == null ? defaultMessage : message)
                    .append("\n");
            ResourceLocation valueLocation = ResourceLocation.fromFile(valueFile);
            errMessage.append("Diff: ").append(expected.getSourceLocation().diff(matchLocation));
            try {
                Json5Printer printer = new Json5Printer(this);
                printer.print(match.getMatchedValue());
                try (PrintWriter out = new PrintWriter(valueFile)) {
                    out.print(printer.toString());
                }
                errMessage.append("\nActual value: ").append(valueLocation.fileURI());
            } catch (IOException e) {
                log.warn("Failed to print actual value", e);
            }
            String expectedString = expected.asString();
            String actualString = match.asString();

            errMessage.append("\nexpected: ").append(expectedString).append(" but was: ").append(actualString);
            throw new AssertionStructFailedError(errMessage.toString(), expectedString, actualString, match.getMatchedValue());
        }
    }

    public void save(MatchResult match, File matchFile) {
        try {
            try (PrintWriter out = new PrintWriter(matchFile)) {
                out.print(new Json5Printer(this).print(match));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
