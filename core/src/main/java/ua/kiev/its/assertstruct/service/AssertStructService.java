package ua.kiev.its.assertstruct.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ua.kiev.its.assertstruct.AssertionStructFailedError;
import ua.kiev.its.assertstruct.Res;
import ua.kiev.its.assertstruct.impl.opt.SubtreeOptions;
import ua.kiev.its.assertstruct.result.Json5Printer;
import ua.kiev.its.assertstruct.result.MatchResult;
import ua.kiev.its.assertstruct.result.RootResult;
import ua.kiev.its.assertstruct.template.Template;
import ua.kiev.its.assertstruct.utils.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import static ua.kiev.its.assertstruct.utils.ResourceUtils.*;

@Getter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssertStructService implements ResourceSourceLocator {

    Config config;
    SortedSet<KeyParser> keyParsers;
    SortedSet<NodeParser> nodeParsers;
    SubtreeOptions subtreeOptions;

    public AssertStructService(Config config) {
        this.config = config;
        SortedSet<NodeParser> nodeParsers = new TreeSet<>(ParsingFactoryComparator.INSTANCE);
        SortedSet<KeyParser> keyParsers = new TreeSet<>(ParsingFactoryComparator.INSTANCE);
        for (ParserFactory parserFactory : config.getParserFactories()) {
            Parser parsers = parserFactory.buildParser(this);
            for (Parser parser : (parsers instanceof ParserContainer)? ((ParserContainer) parsers).getParsers() : Collections.singletonList(parsers)) {
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
        this.subtreeOptions = new SubtreeOptions(
                config.isDefaultOrderedDicts(),
                config.isDefaultOrderedLists(),
                config.isDefaultIgnoreUnknown()
        );
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
            throw new RuntimeException(e);
        }
    }


}
