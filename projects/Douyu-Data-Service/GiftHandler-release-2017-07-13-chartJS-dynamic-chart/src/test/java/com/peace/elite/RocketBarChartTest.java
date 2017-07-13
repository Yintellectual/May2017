package com.peace.elite;

import static org.junit.Assert.*;
import static org.mockito.Matchers.intThat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketBarChartTest {

	@Autowired
	private RocketBarChart rocketBarChart;
	
	private BarChartData barChartData;
	private Set<Long> uidSet;
	@Before
	public void init(){
		uidSet = new HashSet<>();
		
		Random random = new Random();
		final int UPDATES = 1000;
		final int USERS = 100;
		IntStream.range(0, UPDATES).forEach(l-> {
			long uid = random.nextInt(USERS);
			rocketBarChart.update(uid, "User "+uid);
			uidSet.add(uid);
		});
		barChartData = rocketBarChart.sendInitData();
		//1000 updates for 100 users
	}
	@After
	public void cleanUp(){
		rocketBarChart.reset();
	}
	@Test
	public void setNameAsUnknowIfNameIsNullOrEmpty(){
		rocketBarChart.update(111l, null);
		BarChartData updatedBarChartData = rocketBarChart.sendInitData();
	
		assertTrue("\nbefore: "+barChartData.getLabels()+"\nafter: "+updatedBarChartData.getLabels(),
				contains(updatedBarChartData.getLabels(), "用户\\d*"));
	}
	
	private boolean contains(String[] arr, String regex){
		boolean result = false;
		Pattern p = Pattern.compile(regex);
		for(String name:arr){
			Matcher matcher = p.matcher(name);
			if(matcher.find()){
				result = true;
				break;
			}
		}
		return result;
	}
	@Test
	public void setNameByIndexOfUid(){
		rocketBarChart.update(10l, "No.10");
		BarChartData updatedBarChartData = rocketBarChart.sendInitData();
		
		assertTrue("\nbefore: "+barChartData.getLabels()+"\nafter: "+updatedBarChartData.getLabels(),
				contains(updatedBarChartData.getLabels(), "No.10"));
		assertTrue("\nbefore: "+barChartData.getLabels()+"\nafter: "+updatedBarChartData.getLabels(),
				!contains(updatedBarChartData.getLabels(), "User 10"));
		
	}
	private long sum(Long[] arr){
		return Arrays.stream(arr).reduce(0l, ((l1,l2)->l1+l2));
	}
	@Test
	public void incrCounterOnceByOne(){
		rocketBarChart.update(10l, "No.10");
		BarChartData updatedBarChartData = rocketBarChart.sendInitData();
		
		assertTrue(sum(barChartData.data)==1000l);
		assertTrue(sum(updatedBarChartData.data)==1001l);
	}
	@Test 
	public void countupByIndexOfUid(){
		rocketBarChart.update(200l, "User 200");
		BarChartData updatedBarChartData = rocketBarChart.sendInitData();
		
		assertTrue(updatedBarChartData.data[updatedBarChartData.data.length-1]==1l);
		assertTrue(updatedBarChartData.labels[updatedBarChartData.labels.length-1].equals("User 200"));
	}
	@Test
	public void keepAllReferedUsers(){
		for(Long uid:barChartData.data){
			assertTrue(uidSet.contains(uid));
		}
		assertTrue(uidSet.size() == barChartData.data.length);
	}
	
}
