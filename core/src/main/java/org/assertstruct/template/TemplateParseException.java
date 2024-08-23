package org.assertstruct.template;

import com.fasterxml.jackson.core.JsonLocation;

public class TemplateParseException extends RuntimeException {
    JsonLocation location;

    public TemplateParseException(String message) {
        this(message, null, null);
    }

    public TemplateParseException(String message, JsonLocation location) {
        this(message, null, location);
    }

    public TemplateParseException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public TemplateParseException(Throwable cause) {
        this(null, cause, null);
    }

    public TemplateParseException(Throwable cause, JsonLocation location) {
        this(null, cause, location);
    }

    public TemplateParseException(String message, Throwable cause, JsonLocation location) {
        super(location == null ? message : (message == null ? "Cannot parse template" : message) + " at " + location.offsetDescription(), cause);
        this.location = location;
    }
}
