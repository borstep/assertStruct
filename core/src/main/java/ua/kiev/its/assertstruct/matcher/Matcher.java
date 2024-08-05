package ua.kiev.its.assertstruct.matcher;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ua.kiev.its.assertstruct.AssertStruct;
import ua.kiev.its.assertstruct.config.SharedValidator;
import ua.kiev.its.assertstruct.impl.config.ConfigTemplateKey;
import ua.kiev.its.assertstruct.impl.factories.array.RepeaterTemplateNode;
import ua.kiev.its.assertstruct.result.ErrorList;
import ua.kiev.its.assertstruct.result.ErrorMap;
import ua.kiev.its.assertstruct.result.ErrorValue;
import ua.kiev.its.assertstruct.result.MatchResult;
import ua.kiev.its.assertstruct.template.*;
import ua.kiev.its.assertstruct.template.node.ArrayNode;
import ua.kiev.its.assertstruct.template.node.ObjectNode;

import java.util.*;

import static ua.kiev.its.assertstruct.impl.factories.array.RepeaterTemplateNode.isRepeater;
import static ua.kiev.its.assertstruct.utils.Markers.EOS;

@Data
@RequiredArgsConstructor
public class Matcher {
    @Setter(value = AccessLevel.NONE)
    final AssertStruct env;
    @Setter(value = AccessLevel.NONE)
    final Template template;


    boolean orderedDict = false;


    public MatchResult match(Object value) {
        return match(value, template.getRoot());
    }

    private MatchResult match(Object value, TemplateNode node) {
        if (node.getValidators() != null) {
            for (SharedValidator validator : node.getValidators()) {
                if (!validator.match(value, this)) {
                    return new ErrorValue(value, node);
                }
            }
        }
        if (node instanceof ValueMatcher) {
            if (((ValueMatcher) node).match(value, this)) {
                return node;
            } else {
                return new ErrorValue(value, node);
            }
        } else if (node instanceof ObjectNode) {
            if (value instanceof Map) {
                return matchDict((Map) value, (ObjectNode) node);
            } else {
                return new ErrorValue(value, node);
            }
        } else if (node instanceof ArrayNode) {
            if (value instanceof List) {
                return matchList((List) value, (ArrayNode) node);
            } else if (value instanceof Collection) {
                return matchList(new ArrayList((Collection) value), (ArrayNode) node);
            } else {
                return new ErrorValue(value, node);
            }
        } else {
            throw new IllegalArgumentException("Unsupported node type: " + node.getClass().getName());
        }
    }

    private MatchResult matchList(List actual, ArrayNode template) {
        return matchList(actual, 0, template, 0, false);
    }

    private MatchResult matchList(List actual, int actualFrom, ArrayNode template, int templateFrom, boolean quickFail) {
        boolean ordered = template.isOrdered(); //TODO Implement unordered
        int i = templateFrom;
        ErrorList results = new ErrorList(template);
        boolean hasError = false;
        for (int j = actualFrom; j < actual.size(); j++) {
            Object actualValue = actual.get(j);
            TemplateNode expectedNode = i < template.size() ? template.get(i) : null;
            MatchResult<TemplateNode> match;
            if (expectedNode == null) { // template is shorter than actual list
                match = new ErrorValue(actualValue);
            } else if (expectedNode.isConfig() || expectedNode.getKey() instanceof ConfigTemplateKey) { // Configuration
                results.add(expectedNode); // Just copy config as is
                i++;
                j--; //rerun same actual node
                continue;
            } else if (isRepeater(expectedNode)) {
                if (!results.isEmpty() && results.get(results.size() - 1) != expectedNode)
                    results.add(expectedNode);
                long remainingTemplateLength = template.stream().skip(i).filter(RepeaterTemplateNode::isNotRepeater).count();
                int remainingActualLength = actual.size() - j;
                match = match(actualValue, template.get(i - 1));
                if (!match.hasDifference()
                        && remainingTemplateLength <= remainingActualLength
                        && lookAheadFail(actual, j, template, i + 1)) {
                    continue;
                } else {
                    i++;
                    j--;
                    continue;
                }
            } else {
                match = match(actualValue, template.get(i++));
            }
            hasError = hasError || match.hasDifference();
            if (quickFail && hasError) {
                return results;
            }
            results.add(match);
        }
        if (i < template.size() && !(i + 1 == template.size() && isRepeater(template.get(i)))) { // template is longer than actual list ignoring last repeater
            hasError = true;
        }
        if (hasError) {
            return results;
        } else {
            return template;
        }
    }

    private boolean lookAheadFail(List actual, int actualFrom, ArrayNode template, int templateFrom) {
        return matchList(actual, actualFrom, template, templateFrom, true).hasDifference();
    }
/*
    Iterator<Object> actualValues = actual.iterator();
        Iterator<TemplateNode> expectedNodes = template.iterator();
        TemplateNode expectedNode = nextExpectedNode(expectedNodes);
        ErrorList results = new ErrorList(template);
        boolean hasError = false;
        while (actualValue != EOS || expectedNode != null) {
            if (expectedNode != null && (expectedNode.isConfig() || expectedNode.getKey() instanceof ConfigTemplateKey)) { // Configuration
                results.add(expectedNode); // Just copy config as is
                expectedNode=nextExpectedNode(expectedNodes);
                continue;    // TODO implement
            } else if (actualValue == EOS) { // actual list is shorter then template
                hasError = true;
                break;
            } else if (expectedNode == null) { // actual list is longer -> copy remaining actual values
                results.add(new ErrorValue(actualValue, expectedNode));
                actualValue = nextActual(actualValues);
                hasError = true;
                continue;
            } else {
                MatchResult match = match(actualValue, expectedNode, this);
                results.add(match);
                actualValue = nextActual(actualValues);
                expectedNode = nextExpectedNode(expectedNodes);
                if (match.hasDifference())
                    hasError = true;
                continue;
            }
        }
        if (hasError) {
            return results;
        }
        return template;
    }*/

    private MatchResult matchDict(Map<Object, Object> actual, ObjectNode template) {
        boolean ordered = template.isOrdered();
        boolean ignoreUnknown = template.isIgnoreUnknown();
        HashSet<Object> foundKeys = new HashSet<>();
        ErrorMap results = new ErrorMap(template);
        Iterator<Object> actualKeys = actual.keySet().iterator();
        Iterator<TemplateNode> expectedNodes = template.values().iterator();

        Object actualKey = nextActual(actualKeys);
        TemplateNode expectedNode = nextExpectedNode(expectedNodes);
        boolean forceError = false;
        while (actualKey != EOS || expectedNode != null) {
            if (foundKeys.contains(actualKey)) {
                foundKeys.remove(actualKey);
                actualKey = nextActual(actualKeys);
                continue;
            } else if (expectedNode != null && expectedNode.getKey() instanceof ConfigTemplateKey) { // Configuration
                results.put(expectedNode.getKey(), expectedNode); // Just copy config as is
                expectedNode = nextExpectedNode(expectedNodes);
                continue;
            } else if (expectedNode != null && expectedNode.getKey() instanceof EvaluatorTemplateKey) { // Evaluator key
                results.put(expectedNode.getKey(), match(((EvaluatorTemplateKey) expectedNode.getKey()).evaluate(actual, this), expectedNode));
                expectedNode = nextExpectedNode(expectedNodes);
                continue;
            } else if (expectedNode != null && expectedNode.getKey() instanceof MatcherTemplateKey) { // Matcher Key
                results.put(expectedNode.getKey(), expectedNode); // Add it anyway
                if (actualKey != EOS && ((MatcherTemplateKey) expectedNode.getKey()).match((String) actualKey, this) && !template.containsKey(actualKey)) {
                    MatchResult match = match(actual.get(actualKey), expectedNode);
                    if (match.hasDifference()) {
                        results.put(actualKey, match);
                    } // else do nothing
                    actualKey = nextActual(actualKeys);
                } else { // move to next key
                    expectedNode = nextExpectedNode(expectedNodes);
                }
                continue;
            } else if (expectedNode != null) { // Simple key
                if (Objects.equals(actualKey, expectedNode.getKey().getValue())) { // Simple key found in right order
                    results.put(expectedNode.getKey(), match(actual.get(actualKey), expectedNode));
                    actualKey = nextActual(actualKeys);
                    expectedNode = nextExpectedNode(expectedNodes);
                    continue;
                } else if (actualKey != EOS && template.lookup((String) actualKey, this) == null) { // No key found in template
                    if (!ignoreUnknown) {
                        results.put(actualKey, new ErrorValue(actual.get(actualKey)));
                    }
                    actualKey = nextActual(actualKeys);
                    continue;
                } else if (actual.containsKey(expectedNode.getKey().getValue())) { // Simple key found in wrong order
                    if (!ordered) { // if unordered add result in template order
                        results.put(expectedNode.getKey(), match(actual.get(expectedNode.getKey().getValue()), expectedNode));
                        foundKeys.add(expectedNode.getKey().getValue());
                        expectedNode = nextExpectedNode(expectedNodes);
                        continue;
                    }
                } //TODO @!!!!!!!
            }
            if (actualKey == EOS) { // No actual keys left, move to next expected key
                expectedNode = nextExpectedNode(expectedNodes);
            } else { // current expected key does not match with actual key
                TemplateNode templateNode = template.lookup((String) actualKey, this);
                if (templateNode == null) { // No key found in template at the end of template
                    if (!ignoreUnknown) {
                        results.put(actualKey, new ErrorValue(actual.get(actualKey)));
                    }
                } else {
                    TemplateKey templateKey = templateNode.getKey();
                    if (templateKey.getType() == TemplateKeyType.SIMPLE) {
                        if (ordered) {
                            forceError = true;
                            results.put(templateKey, match(actual.get(actualKey), templateNode));
                        }
                    } else if (templateKey.getType() == TemplateKeyType.MATCHER) { // TODO Validate , look like we can add matching key twice
                        MatchResult match = match(actual.get(actualKey), templateNode);
                        if (ordered) {
                            forceError = true;
                            results.put(templateKey, match);
                        } else if (match.hasDifference()) {
                            results.put(actualKey, match);
                        } // if no difference template was will be added in template if
                    }
                }
                actualKey = nextActual(actualKeys);
            }

        }
        if (forceError) {
            return results;
        }
        for (MatchResult result : results.values()) {
            if (result.hasDifference()) {
                return results;
            }
        }
        return template;
    }

    private static Object nextActual(Iterator<Object> actualKeys) {
        return actualKeys.hasNext() ? actualKeys.next() : EOS;
    }

    private static TemplateNode nextExpectedNode(Iterator<TemplateNode> expectedKeys) {
        return expectedKeys.hasNext() ? expectedKeys.next() : null;
    }
}
