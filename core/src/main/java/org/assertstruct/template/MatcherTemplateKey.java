package org.assertstruct.template;

import org.assertstruct.matcher.Matcher;

public interface MatcherTemplateKey {
    boolean match(String key, Matcher context);

}
