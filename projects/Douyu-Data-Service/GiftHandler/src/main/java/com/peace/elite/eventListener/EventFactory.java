package com.peace.elite.eventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;

import com.peace.elite.redisRepository.GiftRepository;

public class EventFactory<T extends Comparable<T>>{
	private List<Listener<T>> handlers = new ArrayList<>();
	private static ThreadLocalRandom random;
	public void register(Listener<T> h){
		//System.out.println("\n\n\n"+this.getClass()+ " got a new listener: " + h.getClass());
		handlers.add(h);
		//System.out.println("\n\n\n"+this.getClass()+ " has "+handlers.size()+" listeners. ");
	}
	public void remove(Listener<T> h){
		handlers.remove(h);
	}
	
	public void publish(Event<T> e){
		//System.out.println("\n\n\n"+this.getClass()+ " ask " + handlers.size() + " classes to handle " + e.getData());
		for(Listener<T> h:handlers){
			//System.out.println("\n\n\n"+this.getClass()+ " ask " + h.getClass() + " to handle " + e.getData());
		//	try{
				h.handle(e);
			//}catch(Exception ex){
			//	ex.printStackTrace(System.out);
			//	try{Thread.sleep(random.nextInt(300));}catch(Exception exc){}
			//	publish(e);
			//}
		}
	}
}