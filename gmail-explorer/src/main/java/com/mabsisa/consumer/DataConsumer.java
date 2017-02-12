/**
 * 
 */
package com.mabsisa.consumer;

import org.springframework.stereotype.Service;

import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * @author abhinab
 *
 */
@Service
public class DataConsumer implements Consumer<Event<String>> {

	@Override
	public void accept(Event<String> data) {
		System.out.println("event data: "+data.getData());
	}

}
