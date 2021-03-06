/*
 * Developed by Won.
 * Last modified 19. 5. 20 오전 9:57.
 * Copyright (c) 2019. All rights reserved.
 */

package com.devplayg.coffee.config;

import com.devplayg.coffee.framework.RequestInterceptor;
import com.devplayg.coffee.vo.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    private final AppConfig appConfig;

    public WebConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Request interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("# interceptor: {}", appConfig.getPathPatternsNotToBeIntercepted().toString());
        registry.addInterceptor(localeChangeInterceptor());

        // Add interceptor to registry
        registry.addInterceptor(new RequestInterceptor(appConfig))
                .excludePathPatterns(appConfig.getPathPatternsNotToBeIntercepted());
    }

    /**
     * Locale interceptor
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.KOREAN);
        cookieLocaleResolver.setCookieName("APPLICATION_LOCALE");
        return cookieLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setHttpMethods();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }

    /**
     * Timezone
     */
    @Bean
    public List<TimeZone> timezoneList() {
        LocalDateTime now = LocalDateTime.now();
        List<TimeZone> list = ZoneId.getAvailableZoneIds().stream()
                .map(ZoneId::of)
                .sorted(new ZoneComparator())
                .map(id -> new TimeZone(id.getId(), getOffset(now, id)))
                .collect(Collectors.toList());
        return list;
    }

    private class ZoneComparator implements Comparator<ZoneId> {
        @Override
        public int compare(ZoneId zoneId1, ZoneId zoneId2) {
            return zoneId1.toString().compareToIgnoreCase(zoneId2.toString());
        }
    }

    private String getOffset(LocalDateTime dateTime, ZoneId id) {
        return dateTime
                .atZone(id)
                .getOffset()
                .getId()
                .replace("Z", "+00:00");
    }
}
