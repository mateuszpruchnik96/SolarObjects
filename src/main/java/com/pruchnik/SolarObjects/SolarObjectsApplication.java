package com.pruchnik.SolarObjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pruchnik.SolarObjects.simulation.NBodySimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableScheduling
public class SolarObjectsApplication implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SolarObjectsApplication.class, args);

//		NBodySimulator nbody = new NBodySimulator();
//		nbody.run();


//		displayAllBeans();
	}

	public static void displayAllBeans() {
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		for(String beanName : allBeanNames) {
			System.out.println(beanName);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SolarObjectsApplication.applicationContext = applicationContext;
	}
}
