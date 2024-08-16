package ua.kiev.its.assertstruct.impl.factories.date;

import ua.kiev.its.assertstruct.service.AssertStructService;
import ua.kiev.its.assertstruct.service.Parser;
import ua.kiev.its.assertstruct.service.ParserContainer;
import ua.kiev.its.assertstruct.service.ParserFactory;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnyDateFactory implements ParserFactory {
    public static final AnyDateFactory INSTANCE = new AnyDateFactory();
    @Override
    public Parser buildParser(AssertStructService env) {

        return new ParserContainer(Arrays.asList(
                new AnyDateParser(buildFormatters(env.getConfig().getDateFormats()), "$ANY_DATE"),
                new AnyDateParser(buildFormatters(env.getConfig().getDateTimeFormats()), "$ANY_DATETIME"),
                new AnyDateParser(buildFormatters(env.getConfig().getTimeFormats()), "$ANY_TIME")
        ));
    }

    static List<DateTimeFormatter> buildFormatters(List<String> formats) {
        return formats
                .stream()
                .map(DateTimeFormatter::ofPattern)
                .collect(Collectors.toList());
    }
}
