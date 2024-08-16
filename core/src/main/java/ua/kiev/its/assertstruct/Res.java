package ua.kiev.its.assertstruct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ua.kiev.its.assertstruct.impl.parser.JSon5Parser;
import ua.kiev.its.assertstruct.service.AssertStructService;
import ua.kiev.its.assertstruct.template.Template;
import ua.kiev.its.assertstruct.template.TemplateParseException;
import ua.kiev.its.assertstruct.template.TemplateParser;
import ua.kiev.its.assertstruct.utils.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static ua.kiev.its.assertstruct.utils.ResourceUtils.codeLocator;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = {"resource"})
public class Res {

    static final String SIBLING_PREFIX = "$$/";
    static final String METHOD_PREFIX = "$/$";
    static final String CLASS_PREFIX = "$/";
    @Getter
    URL resource;
    @Getter
    ResourceLocation location;
    String content;
    AssertStructService env;

    @Getter(lazy = true)
    final ResourceLocation sourceLocation = calcSourceLocation();

    /**
     * Create resource by autodetect type of resource
     *
     * @param res the path to the resource or resource content
     * @return resource
     */
    public static Res res(@NonNull String res) {
        return res(res, codeLocator(), AssertStruct.getDefault());
    }

    /**
     * Creates a new Res object from the given resource path.
     * The path can be absolute or relative to the class executed from method
     * Resource existence will be checked during creating, however loading will be delayed until accessed.
     *
     * @param resPath the path to the resource.
     * @return a new Res object representing the resource.
     * @throws IllegalArgumentException if the resource cannot be found
     */
    public static Res from(String resPath) {
        return from(resPath, codeLocator(), AssertStruct.getDefault());
    }

    /**
     * Creates a new Res object with the given content.
     *
     * @param content the content for the Res object
     * @return a new Res object with the specified content
     */
    public static Res of(String content) {
        return of(content, codeLocator(), AssertStruct.getDefault());
    }

    public static Res res(@NonNull String res, StackTraceElement traceElement, AssertStructService env) {
        boolean isFileResource = false;
        if (!res.isEmpty()) {
            char c = res.charAt(0);
            if (c == '{' || c == '[' || c == '\'' || c == '"' || c == ' ' || c == '\t' || c == '\n') { // JSON-like first char
            } else if ((c == 't' && res.equals("true")) || (c == 'f' && res.equals("false"))) { // JSON-like boolean
            } else if (c == '+' || c == '-' || c == '.' || c == 'E' || c == 'e' || c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') { // look like number
                try {
                    Float.parseFloat(res);
                } catch (NumberFormatException e) {
                    isFileResource = true;
                }
            } else {
                isFileResource = (res.indexOf('\n') == -1); // File must be single line
            }
        }
        if (isFileResource) {
            return Res.from(res, traceElement, env);
        } else {
            return Res.of(res, traceElement, env);
        }
    }

    static Res from(String resPath, StackTraceElement traceElement, AssertStructService locator) {
        try {
            URL url;
            if (resPath.startsWith(Res.SIBLING_PREFIX)) {
                traceElement = traceElement == null ? codeLocator() : traceElement;
                Class<?> executorClass = Res.class.getClassLoader().loadClass(traceElement.getClassName());
                url = executorClass.getResource(resPath.substring(3));
            } else if (resPath.startsWith(Res.METHOD_PREFIX)) {
                traceElement = traceElement == null ? codeLocator() : traceElement;
                Class<?> executorClass = Res.class.getClassLoader().loadClass(traceElement.getClassName());
                url = executorClass.getResource(executorClass.getSimpleName() + "/" + traceElement.getMethodName() + resPath.substring(3));
            } else if (resPath.startsWith(Res.CLASS_PREFIX)) {
                traceElement = traceElement == null ? codeLocator() : traceElement;
                Class<?> executorClass = Res.class.getClassLoader().loadClass(traceElement.getClassName());
                url = executorClass.getResource(executorClass.getSimpleName() + "/" + resPath.substring(2));
            } else {
                url = Res.class.getClassLoader().getResource(resPath);
            }
            if (url == null) {
                throw new IllegalArgumentException("Can't find resource: " + resPath);
            }
            return new Res(url, ResourceLocation.fromURL(url), locator);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can't find resource: " + resPath, e);
        }
    }


    static Res of(String content, StackTraceElement traceElement, AssertStructService env) {
        return new Res(content,
                ResourceLocation.fromStackTrace(traceElement), env);
    }

    Res(URL resource, ResourceLocation location, AssertStructService env) {
        this.resource = resource;
        this.location = location;
        this.env = env;
    }

    Res(String content, ResourceLocation location, AssertStructService env) {
        this.content = content;
        this.resource = location.getUrl();
        this.location = location;
        this.env = env;
    }

    public String asString() {
        checkRead();
        return content;
    }

    public char[] asChars() {
        checkRead();
        return content.toCharArray();
    }

    Template _template = null;

    private ResourceLocation calcSourceLocation() {
        return env == null ? location : env.lookupSourceLocation(location);
    }

    public Template asTemplate() {
        checkRead();
        if (_template == null) {
            try {
                TemplateParser parser = new TemplateParser(env);
                _template = parser.parse(new JSon5Parser(asChars()));
            } catch (IOException e) {
                throw new TemplateParseException(e);
            }
        }
        return _template;
    }

    private void checkRead() {
        if (content == null) {
            try (BufferedReader inp = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
                content = inp.lines().collect(Collectors.joining("\n"));
                ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
