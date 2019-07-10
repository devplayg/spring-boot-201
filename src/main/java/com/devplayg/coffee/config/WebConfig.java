package com.devplayg.coffee.config;

import com.devplayg.coffee.framework.RequestInterceptor;
import com.devplayg.coffee.vo.TimeZone;
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
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
//    @Qualifier(value = "requestInterceptor")
//    private HandlerInterceptor interceptor;

    /*
     * Request interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());

        // 모든 Request 에 대해 Interceptor 를 적용하는 경우
        // registry.addInterceptor(interceptor);

        // 일부 Request Interceptor 를 적용하는 경우
        registry.addInterceptor(new RequestInterceptor())
                .addPathPatterns("/audit/**", "/members/**", "/login/**");
    }

    /*
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

    /*
     * Timezone list
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
            LocalDateTime now = LocalDateTime.now();
            ZoneOffset offset1 = now.atZone(zoneId1).getOffset();
            ZoneOffset offset2 = now.atZone(zoneId2).getOffset();

            return offset1.compareTo(offset2);
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
