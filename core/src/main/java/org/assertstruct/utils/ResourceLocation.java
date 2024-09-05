package org.assertstruct.utils;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Value
@Builder(toBuilder = true)
@With
public class ResourceLocation {
    String className;
    String fileName;
    String methodName;
    URL url;
    int lineNumber;

    public static ResourceLocation fromStackTrace(StackTraceElement el) {
        ResourceLocationBuilder builder = builder()
                .className(el.getClassName())
                .fileName(el.getFileName())
                .methodName(el.getMethodName())
                .lineNumber(el.getLineNumber());

        try {
            URL url = ResourceLocation.class.getClassLoader().loadClass(el.getClassName()).getResource(el.getFileName().replace(".java", ".class"));
            builder.url(url);
        } catch (ClassNotFoundException e) { // must never happen
            throw new RuntimeException(e);
        }
        return builder.build();
    }

    public static ResourceLocation fromURL(URL url) {
        ResourceLocationBuilder builder = builder();
        builder.url(url);
        try {
            URI uri = url.toURI();
            File file = new File(uri);
            builder
                    .fileName(file.getName());
        } catch (URISyntaxException e) { // ignore if URL can't be converted into file
        }
        return builder.build();
    }

    public static ResourceLocation fromFile(File matchFile) {
        try {
            return new ResourceLocation(null, matchFile.getName(), null, matchFile.toURI().toURL(), 0);
        } catch (MalformedURLException e) { // will never happen
            throw new RuntimeException(e);
        }
    }

    public boolean isClass() {
        return className != null;
    }

    public String fileURI() {
        if (url != null) {
            File file = toFile();
            StringBuilder stringBuilder = new StringBuilder("file://");
            stringBuilder.append(file.getAbsolutePath());
            if (lineNumber > 0) {
                stringBuilder.append(":");
                stringBuilder.append(lineNumber);
            }
            return stringBuilder.toString();
        }
        return null;
    }

    public File toFile() {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) { // usually will not happen
            throw new RuntimeException("Can't convert resource locator to file:" + getUrl(), e);
        }
    }

    private String filePathForDiff() {
        try {
            String absolutePath = new File(url.toURI()).getAbsolutePath();
            return absolutePath.startsWith("/") ? absolutePath.substring(1) : absolutePath;
        } catch (URISyntaxException e) { // usually will not happen
            throw new RuntimeException("Can't convert resource locator to file:" + getUrl(), e);
        }
    }

    public String at() {
        StringBuilder buff = new StringBuilder();
        buff.append("at ");
        if (className != null) {
            buff.append(className);
            if (fileName != null) {
                buff.append("(").append(fileName).append(":").append(lineNumber).append(")");
            }
        } else {
            String file = url.getFile();
            buff.append(file).
                    append(" (").append(fileName).append(":").append(lineNumber).append(")");
        }
        return buff.toString();
    }

    public String diff(ResourceLocation to) {
        StringBuilder buff = new StringBuilder("diff:///[")
                .append(filePathForDiff())
                .append(":").append(getLineNumber())
                .append("]?/[")
                .append(to.filePathForDiff())
                .append(":").append(to.getLineNumber())
                .append("]&");
        return buff.toString();
    }
}
