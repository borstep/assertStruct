package config;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import lombok.extern.slf4j.Slf4j;
import org.assertstruct.service.AssertStructConfigurator;
import org.assertstruct.service.Config;
import org.assertstruct.service.ConfigDefaults;

@Slf4j
public class ExampleAssertStructConfigurator implements AssertStructConfigurator {
    @Override
    public Config.ConfigBuilder configure(Config.ConfigBuilder config) {
        ConfigDefaults.defaultJson5FactoryBuilder()
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .build();
        log.info("Custom assertStruct configuration setup");
        return config;
    }
}

