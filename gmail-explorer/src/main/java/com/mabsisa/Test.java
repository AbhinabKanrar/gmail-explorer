package com.mabsisa;

import java.io.IOException;

import com.google.gson.Gson;
import com.mabsisa.domain.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Test {

	private static Gson mapper = new Gson();

	public static void test(String[] args) {
		try {
			System.out.println(System.getProperty("google.url"));
			HttpResponse<JsonNode> jsonResponse = Unirest.post(System.getProperty("google.url"))
					.header("Content-Type", "application/x-www-form-urlencoded")
					.field("Email", "abhinabkanrar")
					.asJson();
			Response response = mapper.fromJson(jsonResponse.getBody().toString(), Response.class);
			System.out.println(response.getEmail());
			System.out.println(response.getName());
		} catch (UnirestException e) {
			e.printStackTrace();
		} finally {
			try {
				Unirest.shutdown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
