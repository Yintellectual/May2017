var stompClient = null;
var myChart0 =null;
var ctx0 ;
var myChart1 =null;
var ctx1 ;
var myChart2 =null;
var ctx2 ;
var myChart3 =null;
var ctx3 ;

var myChart5 =null;
var ctx5 ;
function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

function connect() {
	var socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/greetings', function(greeting) {
			showGreeting(JSON.parse(greeting.body).content);
		});
		stompClient.subscribe('/topic/2-dimensional/user/init', function(init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart0(init);
		});
		stompClient.subscribe('/topic/2-dimensional/user/update', function(update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(myChart0, updateJSON);
		});
		stompClient.subscribe('/topic/2-dimensional/time/init', function(init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart1(init);
		});
		stompClient.subscribe('/topic/2-dimensional/time/update', function(update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(myChart1, updateJSON);
		});
		stompClient.subscribe('/topic/2-dimensional/generic/init', function(init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart2(init);
		});
		stompClient.subscribe('/topic/2-dimensional/generic/update', function(update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(myChart2, updateJSON);
		});
		stompClient.subscribe('/topic/2-dimensional/time_based_single_user_chart/init', function(init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart3(init);
		});
		stompClient.subscribe('/topic/2-dimensional/time_based_single_user_chart/update', function(update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(myChart3, updateJSON);
		});
		console.log('open');
		stompClient.send("/app/2-dimensional/user/init", {}, 'test');
		stompClient.send("/app/2-dimensional/time/init", {}, 'test');
		stompClient.send("/app/2-dimensional/generic/init", {}, 'test');
		stompClient.send("/app/2-dimensional/time_based_single_user_chart/init", {}, 'test');
	});
}


function updateBarChart(myChart, updateJSON){
	var index = updateJSON.index;
	
	myChart.data.labels[index] = updateJSON.label;
	myChart.data.datasets[0].data[index] = updateJSON.data;
	myChart.data.datasets[0].backgroundColor[index] = updateJSON.color;
	myChart.update();
}
function drawBarChart0(init){
	
	myChart0 = new Chart(ctx0, {
	    type: 'doughnut',
	    data: {
	        labels: init.labels,
	        datasets: [{
	        	label: '# of Votes',
	            data: init.data,
	            borderWidth: 1,
	            borderColor:'rgba(13,190,152,0.7)',
	            backgroundColor:init.color,//'rgba(54, 162, 235, 1)',
	            //hoverBackgroundColor:'rgba(75, 192, 192, 1)',
		        //hoverBorderColor:'rgba(75, 192, 192, 1)',
		        hoverBorderWidth:1
	        }],
	    },
	    options: {
	        legend:{
	        	display:false
	        },
	    	animation:{
	        	duration: 150,
	    		easing: 'linear'
	        }
// scales: {
// xAxes: [{
// stacked: true
// }],
// yAxes: [{
// stacked: true
// }]
// }
	    }
	});
}

function drawBarChart1(init){
	
	myChart1 = new Chart(ctx1, {
	    type: 'bar',
	    data: {
	        labels: init.labels,
	        datasets: [{
	        	label: '# of Votes',
	            data: init.data,
	            borderWidth: 1,
	            borderColor:'rgba(54, 162, 235, 1)',
	            backgroundColor:init.color,//'rgba(54, 162, 235, 1)',
	            //hoverBackgroundColor:'rgba(75, 192, 192, 1)',
		        //hoverBorderColor:'rgba(75, 192, 192, 1)',
		        hoverBorderWidth:1
	        }],
	    },
	    options: {
	        legend:{
	        	display:false
	        },
	    	animation:{
	        	duration: 150,
	    		easing: 'linear'
	        }
// scales: {
// xAxes: [{
// stacked: true
// }],
// yAxes: [{
// stacked: true
// }]
// }
	    }
	});
}

function drawBarChart2(init){
	
	myChart2 = new Chart(ctx2, {
	    type: 'doughnut',
	    data: {
	        labels: init.labels,
	        datasets: [{
	        	label: '# of Votes',
	            data: init.data,
	            borderWidth: 1,
	            borderColor:'rgba(54, 162, 235, 1)',
	            backgroundColor:init.color,//'rgba(54, 162, 235, 1)',
	            //hoverBackgroundColor:'rgba(75, 192, 192, 1)',
		        //hoverBorderColor:'rgba(75, 192, 192, 1)',
		        hoverBorderWidth:1
	        }],
	    },
	    options: {
	    	onClick:function(e, r){
	    		alert(JSON.stringify(e));
//	    		alert(
//	    				this.active[0]._chart.config.data.labels[this.active[0]._index]//this.active[0]._chart.config.data.datasets[0].data[this.active[0]._index]//_index//_chart.config.data.datasets[0].backgroundColor
//	    				
//	    		);
	    		stompClient.send("/app/2-dimensional/time_based_single_user_chart/init", {}, this.active[0]._chart.config.data.labels[this.active[0]._index]);
	    	},
	    	legend:{
	        	display:false
	        },
	    	animation:{
	        	duration: 150,
	    		easing: 'linear'
	        }
// scales: {
// xAxes: [{
// stacked: true
// }],
// yAxes: [{
// stacked: true
// }]
// }
	    }
	});
	
}


function drawBarChart3(init){
	
	myChart3 = new Chart(ctx3, {
	    type: 'bar',
	    data: {
	        labels: init.labels,
	        datasets: [{
	        	label: '# of Votes',
	            data: init.data,
	            borderWidth: 1,
	            borderColor:'rgba(54, 162, 235, 1)',
	            backgroundColor:init.color,//'rgba(54, 162, 235, 1)',
	            //hoverBackgroundColor:'rgba(75, 192, 192, 1)',
		        //hoverBorderColor:'rgba(75, 192, 192, 1)',
		        hoverBorderWidth:1
	        }],
	    },
	    options: {
	    	onClick:function(e, r){
	    		alert(JSON.stringify(e));
	    		//alert(
	    				//this.active[0]._chart.config.data.labels[this.active[0]._index]
	    				//this.active[0]._chart.config.data.datasets[0].data[this.active[0]._index]//_index//_chart.config.data.datasets[0].backgroundColor
	    				
	    		//);
	    	},
	    	legend:{
	        	display:false
	        },
	    	animation:{
	        	duration: 150,
	    		easing: 'linear'
	        }
// scales: {
// xAxes: [{
// stacked: true
// }],
// yAxes: [{
// stacked: true
// }]
// }
	    }
	});
	
}



function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
	stompClient.send("/app/hello", {}, JSON.stringify({
		'name' : $("#name").val()
	}));
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
	//window.scrollTo(0, document.body.scrollHeight);
	ff();
}

function ff() {
	var ol = document.getElementById("greetings");
	if (ol.childElementCount > 100) {
		ol.removeChild(ol.getElementsByTagName("tr")[0]);
	}
}
function init() {
	ctx0 = document.getElementById("myChart0").getContext('2d');
	ctx1 = document.getElementById("myChart1").getContext('2d');
	ctx2 = document.getElementById("myChart2").getContext('2d');
	ctx3 = document.getElementById("myChart3").getContext('2d');
	
	ctx5 = document.getElementById("myChart5").getContext('2d');
	connect();
}
$(document).ready(function() {
	init();
});

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendName();
	});
});
