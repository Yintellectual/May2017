package com.example.demo;

public interface AudienceRepository {
	void audienceAdd(Audience audience);
	void moneyIncrBy(long uid, long amont);
	void chatIncr(long uid);
	void chatRecord(long uid, String chat);
}
