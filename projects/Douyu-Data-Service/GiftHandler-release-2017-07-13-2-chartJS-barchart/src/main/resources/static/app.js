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
		stompClient.subscribe('/topic/barchart/timeline/init', function(init) {
			// alert(init);
			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			// alert(initJSON.labels);
			// alert(init.data);
			drawBarChart(init);
		});
		stompClient.subscribe('/topic/barchart/timeline/update', function(update) {
			// alert(update);
			var updateJSON = $.parseJSON(update.body);
			// alert(updateJSON.index);
			// alert(updateJSON.name);
			// alert(updateJSON.counter);
			updateBarChart(updateJSON);
		});
		console.log('open');
		stompClient.send("/app/barchart/timeline/init", {}, 'test');
	});
}
function updateBarChart(updateJSON){
	var index = updateJSON.index;
	myChart.data.labels[index] = updateJSON.name;
	myChart.data.datasets[0].data[index] = updateJSON.counter;
	myChart.update();
}


function drawBarChart(init){
	
	var ctx = document.getElementById("myChart").getContext('2d');
	myChart = new Chart(ctx, {
	    type: 'line',
	    data: {
	        labels: init.labels,
	        datasets: [{
	        	fill: false,
	        	label: '# of Votes',
	            data: init.data,
	            borderWidth: 1,
	            borderColor:'rgba(54, 162, 235, 1)',
	            // backgroundColor:'rgba(0,0,0,0)',//'rgba(54, 162, 235, 1)',
	            // hoverBackgroundColor:'rgba(75, 192, 192, 1)',
		        hoverBorderColor:'rgba(75, 192, 192, 1)',
		        hoverBorderWidth:2
	        }],
	    },
	    options: {
	        animation:{
	        	duration: 150,
	    		easing: 'linear'
	        },
	        
	        
	        scales: {
	        	yAxes: [{
                    id: 'left-y-axis',
                    type: 'linear',
                    position: 'left',
                    ticks: {
    	                    // Include a dollar sign in the ticks
    	                    callback: function(value, index, values) {
    	                        return '$' + value;
    	                    }    	
                    }
	        	}, {
                    id: 'right-y-axis',
                    type: 'category',
                    position: 'right',
                    gridLines:{
                    	color:'rgba(100,192,192,1)'
                    }
                }]
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
