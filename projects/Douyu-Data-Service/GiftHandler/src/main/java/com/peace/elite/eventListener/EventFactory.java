package com.peace.elite.eventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;

import com.peace.elite.redisRepository.GiftRepository;

public class EventFactory<T extends Comparable<T>>{
	protected List<Listener<T>> handlers = Collections.synchronizedList(new ArrayList<>());
	protected static ThreadLocalRandom random;
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
			if(h!=null)
				h.handle(e);

		//	
			//	ex.printStackTrace(System.out);
			//	try{Thread.sleep(random.nextInt(300));}catch(Exception exc){}
			//	publish(e);
		}
	}
}