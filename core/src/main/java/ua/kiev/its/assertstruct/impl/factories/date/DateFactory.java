package ua.kiev.its.assertstruct.impl.factories.date;

import ua.kiev.its.assertstruct.service.AssertStructService;
import ua.kiev.its.assertstruct.service.Parser;
import ua.kiev.its.assertstruct.service.ParserContainer;
import ua.kiev.its.assertstruct.service.ParserFactory;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;
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
                new NowParser(env.getConfig().getNowPrecision()*1000, dateTimeFormatters, env.getConfig().isNowStrictCheck()),
                new DateParser()
        ));
    }

    public static List<DateTimeFormatter> buildFormatters(List<String> formats) {
        return formats
                .stream()
                .map(DateTimeFormatter::ofPattern)
                .map(f->f.withResolverStyle(ResolverStyle.STRICT))
                .collect(Collectors.toList());
    }
}