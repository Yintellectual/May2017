package com.peace.elite.chartService;

import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.peace.elite.ChartData2D;
import com.peace.elite.ChartDataServiceFor2DimensionalCharts;
import com.peace.elite.chartService.chartData.ChartData;
import com.peace.elite.entities.ChartEntry2D;

import lombok.Data;


public abstract class ChartService2D<T>  implements ChartData<ChartEntry2D>{

	protected ChartDataServiceFor2DimensionalCharts chartData;
	//websocket handlers
	public abstract ChartData2D initRequestHandler();
	public abstract ChartData2D sortRequestHandler();
	
	
	@Override
	public void setAndSend(ArrayList<ChartEntry2D> data) {
		// TODO Auto-generated method stub
		chartData.setAndSend(data);
	}

	@Override
	public void updateAndSend(ChartEntry2D entry) {
		// TODO Auto-generated method stub
		chartData.updateAndSend(entry);
	}

	@Override
	public ChartData2D getChartData() {
		// TODO Auto-generated method stub
		return chartData.getChartData();
	}

	@Override
	public void sort() {
		// TODO Auto-generated method stub
		chartData.sort();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		chartData.reset();
	}

	@Override
	public void setWebSocket(SimpMessagingTemplate webSocket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWEB_SOCKET_PUBLISH_CHANNEL(String channel) {
		// TODO Auto-generated method stub
		
	}
}
