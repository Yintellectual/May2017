package com.peace.elite.eventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFactory<T>{
	private List<Listener<T>> handlers = new ArrayList<>();
	
	public void register(Listener<T> h){
		handlers.add(h);
	}
	
	public void publish(Event<T> e){
		for(Listener<T> h:handlers){
			h.handle(e);
		}
	}
}