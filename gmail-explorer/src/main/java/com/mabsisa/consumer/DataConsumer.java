/**
 * 
 */
package com.mabsisa.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mabsisa.domain.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * @author abhinab
 *
 */
@Service
public class DataConsumer implements Consumer<Event<String>> {
	
	@Autowired
	private Environment env;
	
	private static Gson mapper = new Gson();
	
	@Override
	public void accept(Event<String> data) {
		 try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(env.getProperty("google.url"))
			         .header("Content-Type", "application/x-www-form-urlencoded")
			         .field("Email", data.getData())
			         .asJson();
			Thread.sleep(500);
            Response response = mapper.fromJson(jsonResponse.getBody().toString(), Response.class);
            if (response.getEmail() != null) {
            	System.out.println(response.getEmail());
			}
		} catch (UnirestException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
