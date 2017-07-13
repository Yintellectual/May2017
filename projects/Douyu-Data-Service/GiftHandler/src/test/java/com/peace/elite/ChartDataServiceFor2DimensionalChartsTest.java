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

import com.peace.elite.entities.ChartEntry2D;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ChartDataServiceFor2DimensionalChartsTest {

	@Autowired
	private ChartDataServiceFor2DimensionalCharts chartService;
	
	private ChartData2D chartData2D;
	private Set<Long> uidSet;
	@Before
	public void init(){
		uidSet = new HashSet<>();
		
		Random random = new Random();
		final int UPDATES = 1000;
		final int USERS = 100;
		IntStream.range(0, UPDATES).forEach(i-> {
			long uid = random.nextInt(USERS);
			chartService.update(new ChartEntry2D(1l, "User "+uid, uid+""));
			uidSet.add(uid);
		});
		chartData2D = chartService.sendInitData();
		//1000 updates for 100 users
	}
	@After
	public void cleanUp(){
		chartService.reset();
	}
	@Test
	public void setNameAsUnknowIfNameIsNullOrEmpty(){
		chartService.update(null, 111l);
		ChartData2D updatedChartData = chartService.sendInitData();
	
		assertTrue("\nbefore: "+chartData2D.getLabels()+"\nafter: "+updatedChartData.getLabels(),
				contains(updatedChartData.getLabels(), "匿名\\d*"));
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
		chartService.update("No.10", 10l);
		ChartData2D updatedChartData = chartService.sendInitData();
		
		assertTrue("\nbefore: "+chartData2D.getLabels()+"\nafter: "+updatedChartData.getLabels(),
				contains(updatedChartData.getLabels(), "No.10"));
		assertTrue("\nbefore: "+chartData2D.getLabels()+"\nafter: "+updatedChartData.getLabels(),
				!contains(updatedChartData.getLabels(), "User 10"));
		
	}
	private long sum(Long[] arr){
		return Arrays.stream(arr).reduce(0l, ((l1,l2)->l1+l2));
	}
	@Test
	public void incrCounterOnceByOne(){
		chartService.update("No.10", 10l);
		ChartData2D updatedChartData = chartService.sendInitData();
		
		assertTrue(sum(chartData2D.getData())==1000l);
		assertTrue(sum(updatedChartData.getData())==1001l);
	}
	@Test 
	public void countupByIndexOfUid(){
		chartService.update("User 200", 200l);
		ChartData2D updatedChartData = chartService.sendInitData();
		
		assertTrue(updatedChartData.getData()[updatedChartData.getData().length-1]==1l);
		assertTrue(updatedChartData.getLabels()[updatedChartData.getLabels().length-1].equals("User 200"));
	}
	@Test
	public void keepAllReferedUsers(){
		for(Long uid:chartData2D.getData()){
			assertTrue(uidSet.contains(uid));
		}
		assertTrue(uidSet.size() == chartData2D.getData().length);
	}
	
}
