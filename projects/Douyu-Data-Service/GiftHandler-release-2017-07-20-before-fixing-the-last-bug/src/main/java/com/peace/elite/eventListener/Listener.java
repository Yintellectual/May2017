package com.peace.elite.eventListener;

public interface Listener<T extends Comparable<T>>{
	void handle(Event<T> e);
}