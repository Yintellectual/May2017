package com.peace.elite.partition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.Data;

//never holds the data

public class UidPartitions {
//	private GiftRepository giftRepository;
//	public UidPartitions(){
//		
//	}
//	public UidPartitions(GiftRepository giftRepository){
//		this.giftRepository = giftRepository;
//	}
//	
//	protected Map<Long, GivingPartition> uidPartitions = new HashMap<>();
//
//	protected GivingPartition newPartition(Long uid, String name) {
//		GivingPartition result = new GivingPartition((giving) -> {
//			return uid.equals(giving.getUid());
//		}, giftRepository.sumOfPrices);
//		result.setLabel(name);
//		result.setId(uid + "");
//		return result;
//	}
//
//	public void addPartition(Long uid, String name) {
//		if (!uidPartitions.keySet().contains(uid)) {
//			uidPartitions.put(uid, newPartition(uid, name));
//		}
//	}
//
//	public void addPartitions(Map<Long, String> users) {
//		for (Long uid : users.keySet()) {
//			addPartition(uid, users.get(uid));
//		}
//	}
//
//	public void remove(Long uid) {
//		uidPartitions.remove(uid);
//	}
//
//	public void partition(Giving giving) {
//		// TODO Auto-generated method stub
//		for (GivingPartition partition : uidPartitions.values()) {
//			if (partition.test(giving)) {
//				break;
//			}
//		}
//	}
//	public ArrayList<ChartEntry2D> getData() {
//		ArrayList<ChartEntry2D> result = new ArrayList<ChartEntry2D>();
//
//		for (GivingPartition partition : uidPartitions.values()) {
//			result.add(partition);
//		}
//
//		return result;
//	}
}

