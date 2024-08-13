package com.MTAPizza.Sympoll.votingservice.config;

import com.MTAPizza.Sympoll.votingservice.client.PollClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${poll.service.url}")
    private String pollServiceUrl;

    @Bean
    public PollClient pollClient(){
        RestClient restClient = RestClient.builder()
                .baseUrl(pollServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(PollClient.class);
    }
}
