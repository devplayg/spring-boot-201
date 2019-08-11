package com.devplayg.coffee.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class HttpConnectionConfig {

//    @Bean
//    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectTimeout(2000);
//        httpRequestFactory.setReadTimeout(3000);
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setMaxConnTotal(200)
//                .setMaxConnPerRoute(20)
//                .build();
//        httpRequestFactory.setHttpClient(httpClient);
//        return new RestTemplate(httpRequestFactory);
//    }
//
//    @Bean
//    public ObjectMapper jacksonObjectMapper()
//    {
//        return new ObjectMapper().setPropertyNamingStrategy(propertyNamingStrategy());
//    }
//
//    @Bean
//    public PropertyNamingStrategy propertyNamingStrategy()
//    {
//        return new PropertyNamingStrategy.UpperCamelCaseStrategy();
//    }

    @Bean
    public RestTemplate restTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .build();

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(2000);
        httpRequestFactory.setReadTimeout(3000);
        httpRequestFactory.setHttpClient(httpClient);

        // Message
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
//        jsonMessageConverter.setObjectMapper(jacksonObjectMapper());
//        messageConverters.add(jsonMessageConverter);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
//        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}