var stompClient = null;
var myChart =null;
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
		stompClient.subscribe('/topic/2-dimensional/generic/init', function(init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart(init);
		});
		stompClient.subscribe('/topic/2-dimensional/generic/update', function(update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(updateJSON);
		});
		console.log('open');
		stompClient.send("/app/2-dimensional/generic/init", {}, 'test');
	});
}


function updateBarChart(updateJSON){
	var index = updateJSON.index;
	
	myChart.data.labels[index] = updateJSON.label;
	myChart.data.datasets[0].data[index] = updateJSON.data;
	myChart.data.datasets[0].backgroundColor[index] = updateJSON.color;
	myChart.update();
}

function drawBarChart(init){
	var ctx = document.getElementById("myChart").getContext('2d');
	myChart = new Chart(ctx, {
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
	window.scrollTo(0, document.body.scrollHeight);
	ff();
}

function ff() {
	var ol = document.getElementById("greetings");
	if (ol.childElementCount > 100) {
		ol.removeChild(ol.getElementsByTagName("tr")[0]);
	}
}
function init() {
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
