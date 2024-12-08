package org.assertstruct.impl.factories.date;

import org.assertstruct.service.AssertStructService;
import org.assertstruct.service.Parser;
import org.assertstruct.service.ParserContainer;
import org.assertstruct.service.ParserFactory;
import org.assertstruct.utils.MapUtils;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DateFactory implements ParserFactory {
    public static final DateFactory INSTANCE = new DateFactory();

    @Override
    public Parser buildParser(AssertStructService env) {

        List<DateTimeFormatter> dateTimeFormatters = buildFormatters(env.getConfig().getDateTimeFormats());
        return new ParserContainer(Arrays.asList(
                new AnyDateParser(buildFormatters(env.getConfig().getDateFormats()), "$ANY_DATE"),
                new AnyDateParser(dateTimeFormatters, "$ANY_DATETIME"),
                new AnyDateParser(buildFormatters(env.getConfig().getTimeFormats()), "$ANY_TIME"),
                new NowParser(env.getConfig().getNowPrecision() * 1000L, dateTimeFormatters, env.getConfig().isNowStrictCheck()),
                new DateParser()
        ));
    }

    public static List<DateTimeFormatter> buildFormatters(List<String> formats) {
        return formats
                .stream()
                .map(DateFactory::str2Formatter)
                .collect(Collectors.toList());
    }

    static final Map<String, DateTimeFormatter> standardFormatters = MapUtils.mapOf(
            "BASIC_ISO_DATE", DateTimeFormatter.BASIC_ISO_DATE
            , "ISO_OFFSET_DATE", DateTimeFormatter.ISO_OFFSET_DATE
            , "ISO_OFFSET_DATE_TIME", DateTimeFormatter.ISO_OFFSET_DATE_TIME
            , "ISO_OFFSET_TIME", DateTimeFormatter.ISO_OFFSET_TIME
            , "ISO_INSTANT", DateTimeFormatter.ISO_INSTANT
            , "ISO_LOCAL_DATE", DateTimeFormatter.ISO_LOCAL_DATE
            , "ISO_LOCAL_DATE_TIME", DateTimeFormatter.ISO_LOCAL_DATE_TIME
            , "ISO_LOCAL_TIME", DateTimeFormatter.ISO_LOCAL_TIME
            , "RFC_1123_DATE_TIME", DateTimeFormatter.RFC_1123_DATE_TIME
            , "ISO_DATE", DateTimeFormatter.ISO_DATE
            , "ISO_DATE_TIME", DateTimeFormatter.ISO_DATE_TIME
            , "ISO_TIME", DateTimeFormatter.ISO_TIME

    );

    public static DateTimeFormatter str2Formatter(String format) {
        if (standardFormatters.containsKey(format)) {
            return standardFormatters.get(format);
        } else {
            return DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.STRICT);
        }
    }
}
