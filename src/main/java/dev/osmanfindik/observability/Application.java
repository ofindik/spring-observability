package dev.osmanfindik.observability;

import dev.osmanfindik.observability.post.JsonPlaceholderService;
import dev.osmanfindik.observability.post.Post;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class Application {
	public static final Logger logger = LoggerFactory.getLogger (Application.class);

	public static void main (String[] args) {
		SpringApplication.run (Application.class, args);
	}

	@Bean
	JsonPlaceholderService jsonPlaceholderService () {
		RestClient client = RestClient.create ("https://jsonplaceholder.typicode.com/");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builderFor (RestClientAdapter.create (client))
				.build ();
		return factory.createClient (JsonPlaceholderService.class);
	}

	@Bean
	@Observed (name = "post.load-all-posts", contextualName = "post.find-all")
	CommandLineRunner commandLineRunner (JsonPlaceholderService jsonPlaceholderService) {
		return args -> {
			jsonPlaceholderService.findAll ();
		};
	}
}
