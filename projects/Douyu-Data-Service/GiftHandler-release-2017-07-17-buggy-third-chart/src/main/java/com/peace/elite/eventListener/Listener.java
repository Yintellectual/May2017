package com.peace.elite.eventListener;

public interface Listener<T>{
	void handle(Event<T> e);
}