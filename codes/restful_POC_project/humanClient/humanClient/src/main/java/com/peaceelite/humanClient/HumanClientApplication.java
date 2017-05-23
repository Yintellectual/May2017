package com.peaceelite.humanClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.CommandLineRunner;
import com.peaceelite.humanService.Person;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.text.SimpleDateFormat;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@SpringBootApplication
@Configuration
public class HumanClientApplication extends WebMvcConfigurerAdapter{

	public static void main(String[] args) {
		SpringApplication.run(HumanClientApplication.class, args);
	}
	
	@Bean
	public RestTemplate initRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
/*	
	@Override
    public void configureMessageConverters(
        List<HttpMessageConverter<?>> converters) {
			
			Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
			builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
			//builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
*/
	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
		
			String url = "http://localhost:8080/people";
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(APPLICATION_JSON_UTF8);
			HttpEntity<Person> requestEntity = new HttpEntity<>(
				new Person(),
				requestHeaders
			);
			
			System.out.println(requestEntity.getBody());
			
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			System.out.println(response.getBody());
		};
	}
	
}
