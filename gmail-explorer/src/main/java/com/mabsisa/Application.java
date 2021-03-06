/**
 * 
 */
package com.mabsisa;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.mabsisa.consumer.DataConsumer;
import com.mabsisa.service.MailChecker;
import com.mabsisa.util.CommonConstant;
import com.mashape.unirest.http.Unirest;

import reactor.Environment;
import reactor.bus.EventBus;
import reactor.core.config.DispatcherType;

import static reactor.bus.selector.Selectors.$;

import java.io.IOException;

/**
 * @author abhinab
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { ValidationAutoConfiguration.class, DataSourceAutoConfiguration.class,
		JmxAutoConfiguration.class, WebSocketAutoConfiguration.class })
@ComponentScan(basePackages = "com.mabsisa")
public class Application implements CommandLineRunner {

	@Autowired
	private org.springframework.core.env.Environment env;
	
	@Autowired
	DataConsumer dataConsumer;

	private volatile static EventBus EVENT_BUS = null;

	private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
	public static int REACTOR_THREAD_COUNT;

	static {
		try {
			REACTOR_THREAD_COUNT = Integer
					.parseInt(System.getProperty("reactor.threads", String.valueOf(AVAILABLE_PROCESSORS)));
		} catch (Exception e) {
			REACTOR_THREAD_COUNT = AVAILABLE_PROCESSORS;
		}
	}

	public static EventBus getEventBus() {
		while (EVENT_BUS == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		return EVENT_BUS;
	}

	@Bean
	Environment env() {
		return Environment.initializeIfEmpty().assignErrorJournal();
	}

	@Bean
	EventBus createEventBus(Environment env) {
		EVENT_BUS = EventBus.create(env, Environment.newDispatcher(REACTOR_THREAD_COUNT, REACTOR_THREAD_COUNT,
				DispatcherType.THREAD_POOL_EXECUTOR));
		return EVENT_BUS;
	}

	@Override
	public void run(String... args) throws Exception {
		EVENT_BUS.on($(CommonConstant.CONSUMER_KEY), dataConsumer);
		MailChecker.check(env.getProperty("search.base"), Integer.parseInt(env.getProperty("search.length")));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PreDestroy
	void shutdownBus() {
		try {
			Unirest.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Environment.terminate();
	}

}
