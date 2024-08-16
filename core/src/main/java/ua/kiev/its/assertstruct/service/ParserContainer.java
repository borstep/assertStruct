package ua.kiev.its.assertstruct.service;

import lombok.Value;

import java.util.Collection;

/**
 * Helper class to return multiple Parser by single factory
 */
@Value
public class ParserContainer implements Parser {
    Collection<Parser> parsers;

    @Override
    public int priority() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPrefix() {
        throw new UnsupportedOperationException();
    }
}
