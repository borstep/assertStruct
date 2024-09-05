package org.assertstruct.impl.factories.date;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.assertstruct.impl.parser.ExtToken;
import org.assertstruct.matcher.Matcher;
import org.assertstruct.template.TemplateKey;
import org.assertstruct.template.node.ScalarNode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
public class NowNode extends ScalarNode {
    /**
     * Precision in milliseconds
     * default = 0
     */
    long precision;
    List<DateTimeFormatter> formatters;
    boolean strictCheck;

    public NowNode(long precision, boolean strictCheck, List<DateTimeFormatter> dateTimeFormatters, TemplateKey key, ExtToken token) {
        super(key, token);
        this.precision = precision;
        this.strictCheck = strictCheck;
        this.formatters = dateTimeFormatters;
    }

    @Override
    public boolean match(Object value, Matcher context) {
        if (strictCheck) {
            return strictMatch(value, context);
        } else {
            return unstrictMatch(value, context);
        }
    }

    public boolean strictMatch(Object value, Matcher context) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                Instant valueParsed = formatter.parse(value.toString(), Instant::from);
                return (Math.abs(Instant.now().toEpochMilli() - valueParsed.toEpochMilli()) < precision);
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public boolean unstrictMatch(Object value, Matcher context) {

        Object srcValue = context.getCurrentSource();
        Instant now = Instant.now();
        long valueInt = -1;
        if (srcValue instanceof java.util.Date) {
            valueInt = ((java.util.Date) srcValue).getTime();
        } else if (srcValue instanceof Instant) {
            valueInt = ((Instant) srcValue).toEpochMilli();
        } else if (srcValue instanceof Long) {
            valueInt = (Long) srcValue;
        } else if (srcValue instanceof ZonedDateTime) {
            valueInt = ((ZonedDateTime) srcValue).toInstant().toEpochMilli();
        } else if (srcValue instanceof LocalDateTime) {
            valueInt = ((LocalDateTime) srcValue).toInstant(ZonedDateTime.now().getOffset()).toEpochMilli();
        } else if (srcValue instanceof Calendar) {
            valueInt = ((Calendar) srcValue).getTimeInMillis();
        } else if (value instanceof Number) { // May be date in number
            valueInt = ((Number) value).longValue();
        } else if (value instanceof String) { // May be date in string
            for (DateTimeFormatter formatter : formatters) {
                try {
                    Instant valueParsed = formatter.parse(value.toString(), Instant::from);
                    valueInt = valueParsed.toEpochMilli();
                    break;
                } catch (Exception ignored) {
                }
            }
        } else {
            return false;
        }
        return valueInt != -1 && (Math.abs(now.toEpochMilli() - valueInt) < precision);
    }
}
