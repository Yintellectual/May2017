package com.peace.elite.eventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFactory<T>{
	private List<Listener<T>> handlers = new ArrayList<>();
	
	public void register(Listener<T> h){
		//System.out.println("\n\n\n"+this.getClass()+ " got a new listener: " + h.getClass());
		handlers.add(h);
		//System.out.println("\n\n\n"+this.getClass()+ " has "+handlers.size()+" listeners. ");
	}
	
	public void publish(Event<T> e){
		//System.out.println("\n\n\n"+this.getClass()+ " ask " + handlers.size() + " classes to handle " + e.getData());
		for(Listener<T> h:handlers){
			//System.out.println("\n\n\n"+this.getClass()+ " ask " + h.getClass() + " to handle " + e.getData());
			h.handle(e);
		}
	}
}