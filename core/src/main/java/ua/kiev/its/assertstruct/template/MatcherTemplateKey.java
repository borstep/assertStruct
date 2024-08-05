package ua.kiev.its.assertstruct.template;

import ua.kiev.its.assertstruct.matcher.Matcher;

public interface MatcherTemplateKey {
    boolean match(String key, Matcher context);

}
