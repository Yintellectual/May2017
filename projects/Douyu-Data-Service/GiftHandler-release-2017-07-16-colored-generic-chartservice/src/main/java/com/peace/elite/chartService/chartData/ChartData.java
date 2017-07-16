package com.peace.elite.chartService.chartData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.peace.elite.ChartData2D;
import com.peace.elite.entities.ChartEntry2D;

public interface ChartData<T> {
	void setWebSocket(SimpMessagingTemplate webSocket);
	void setWEB_SOCKET_PUBLISH_CHANNEL(String channel);
	void setAndSend(ArrayList<T> data);
	void updateAndSend(ChartEntry2D entry);
	ChartData2D getChartData();
	void sort();
	void reset();
}
