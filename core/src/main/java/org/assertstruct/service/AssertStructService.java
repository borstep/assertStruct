package org.assertstruct.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.assertstruct.AssertionStructFailedError;
import org.assertstruct.Res;
import org.assertstruct.converter.JsonConverter;
import org.assertstruct.converter.JsonConverterFactory;
import org.assertstruct.impl.converter.dummy.DummyConverter;
import org.assertstruct.impl.opt.SubtreeOptions;
import org.assertstruct.impl.parser.JSon5Parser;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.result.Json5Printer;
import org.assertstruct.result.MatchResult;
import org.assertstruct.result.RootResult;
import org.assertstruct.service.exceptions.MatchingFailure;
import org.assertstruct.template.Template;
import org.assertstruct.template.TemplateParseException;
import org.assertstruct.template.TemplateParser;
import org.assertstruct.utils.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.assertstruct.utils.ResourceUtils.*;

@Getter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssertStructService implements ResourceSourceLocator {

    Config config;
    JsonConverter jsonConverter;
    SortedSet<KeyParser> keyParsers;
    SortedSet<NodeParser> nodeParsers;
    SubtreeOptions subtreeOptions;

    public AssertStructService(Config config) {
        this.subtreeOptions = new SubtreeOptions(
                config.isDefaultOrderedDicts(),
                config.isDefaultOrderedLists(),
                config.isDefaultIgnoreUnknown()
        );
        if (config.getJsonConverter() != null) {
            this.jsonConverter = config.getJsonConverter();
        } else {
            this.jsonConverter = loadJsonConverter(config.getJsonConverterFactories(), config);
            config = config.toBuilder().jsonConverter(jsonConverter).internalBuildConfig(); // Hack to share potentially expensive converter
        }
        this.config = config;
        SortedSet<NodeParser> nodeParsers = new TreeSet<>(ParsingFactoryComparator.INSTANCE);
        SortedSet<KeyParser> keyParsers = new TreeSet<>(ParsingFactoryComparator.INSTANCE);
        for (ParserFactory parserFactory : config.getParserFactories()) {
            Parser parsers = parserFactory.buildParser(this);
            for (Parser parser : (parsers instanceof ParserContainer) ? ((ParserContainer) parsers).getParsers() : Collections.singletonList(parsers)) {
                if (parser instanceof NodeParser) {
                    nodeParsers.add((NodeParser) parser);
                }
                if (parser instanceof KeyParser) {
                    keyParsers.add((KeyParser) parser);
                }
            }
        }
        this.nodeParsers = Collections.unmodifiableSortedSet(nodeParsers);
        this.keyParsers = Collections.unmodifiableSortedSet(keyParsers);
    }

    private static JsonConverter loadJsonConverter(List<String> jsonConverterFactories, Config config) {
        for (String jsonConverterClass : jsonConverterFactories) {
            try {
                return ((JsonConverterFactory) AssertStructService.class.getClassLoader().loadClass(jsonConverterClass).newInstance())
                        .build(config);
            } catch (ClassNotFoundException ignore) {
            } catch (Exception e) {
                log.warn("Can't instantiate JSON converter {}.", jsonConverterClass, e);
            }
        }
        log.warn("No JSON converter found use default");
        return new DummyConverter();
    }

    public Config.ConfigBuilder with() {
        return config.toBuilder();
    }


    @Override
    public ResourceLocation lookupSourceLocation(ResourceLocation location) {
        try {
            File file = location.toFile();
            String uri = file.getAbsoluteFile().toURI().getPath();
            String rootPart = null;
            String suffixPath = null;
            for (String path : config.getTargetPaths()) {
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
            for (String path : config.getSrcPaths()) {
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

    public void assertStruct(@NonNull Res expected, Object actualValue) {
        StackTraceElement el = codeLocator();
        assertStruct(expected, actualValue, null, el);
    }

    public void assertStruct(@NonNull String expected, Object actualValue) {
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, this);
        assertStruct(res, actualValue, null, el);
    }

    public void assertStruct(@NonNull String expected, Object actualValue, String message) {
        StackTraceElement el = codeLocator();
        Res res = Res.res(expected, el, this);
        assertStruct(res, actualValue, message, el);
    }

    public void assertStruct(@NonNull Res expected, Object pojoActualValue, String message, StackTraceElement el) {
        RootResult match = match(expected, pojoActualValue);
        if (match.hasDifference()) {
            if (el == null) {
                el = codeLocator();
            }
            ResourceLocation testLocation = ResourceLocation.fromStackTrace(el);
            File targetDir = new File(config.getTarget());
            File generateDir = new File(targetDir, config.getGeneratePath());
            if (!generateDir.exists() && (config.isGenerateDiff() || config.isGenerateValueFile())) {
                if (!generateDir.mkdirs()) {
                    log.warn("Can't generate error data because directory {} doesn't exist", generateDir.getAbsolutePath());
                }
            }
            StringBuilder errMessage = new StringBuilder(message == null ? config.getDefaultMessage() : message)
                    .append("\n");
            if (config.isGenerateDiff() && generateDir.exists()) {
                File matchFile = new File(generateDir, testLocation.getClassName().replace(".", "/")
                        + "_" + testLocation.getMethodName() + "_" + testLocation.getLineNumber() + ".json5");
                File parentFile = matchFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                save(match, matchFile);
                ResourceLocation matchLocation = ResourceLocation.fromFile(matchFile);
                errMessage.append("Diff: ").append(expected.getSourceLocation().diff(matchLocation));
            }
            if (config.isGenerateValueFile() && generateDir.exists()) {
                File valueFile = new File(generateDir, testLocation.getClassName().replace(".", "/")
                        + "_" + testLocation.getMethodName() + "_" + testLocation.getLineNumber() + ".value.json5");
                ResourceLocation valueLocation = ResourceLocation.fromFile(valueFile);
                try {
                    try (PrintWriter out = new PrintWriter(valueFile)) {
                        out.print(jsonify(match.getMatchedValue()));
                    }
                    errMessage.append("\nActual value: ").append(valueLocation.fileURI());
                } catch (IOException e) {
                    log.warn("Failed to print actual value", e);
                }
            }
            String expectedString = expected.asString();
            String actualString = jsonify(match.getDelegate());

            errMessage.append("\nexpected: ").append(expectedString).append(" but was: ").append(actualString);
            throw new AssertionStructFailedError(errMessage.toString(), expectedString, actualString, match.getMatchedValue());
        }
    }

    public RootResult match(Res res, Object pojoActualValue) {
        return match(res.asTemplate(), pojoActualValue);
    }

    public RootResult match(Template template, Object pojoActualValue) {
        Matcher matcher = new Matcher(this, template);
        return matcher.match(pojoActualValue);
    }

    public String jsonify(Object value) {
        Json5Printer printer = new Json5Printer(this);
        printer.print(value);
        return printer.toString();
    }

    public void save(MatchResult match, File matchFile) {
        try {
            try (PrintWriter out = new PrintWriter(matchFile)) {
                out.print(new Json5Printer(this).print(match));
            }
        } catch (IOException e) {
            throw new MatchingFailure("Can't save matching result into file " + matchFile, e);
        }
    }


    public Template parse(Res res) throws TemplateParseException {
        try {
            TemplateParser parser = new TemplateParser(this);
            return parser.parse(new JSon5Parser(res.asChars(), this));
        } catch (IOException e) {
            throw new TemplateParseException(e);
        }
    }
}
