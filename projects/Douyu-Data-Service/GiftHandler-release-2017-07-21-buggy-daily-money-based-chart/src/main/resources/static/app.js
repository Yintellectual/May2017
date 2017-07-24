var stompClient = null;
var myChart0 = null;
var ctx0;
var myChart1 = null;
var ctx1;
var myChart2 = null;
var ctx2;
var myChart3 = null;
var ctx3;

var myChart5 = null;
var ctx5;

var loading = 0;
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
		stompClient.subscribe('/topic/greetings', function(giving) {
			showGiving(JSON.parse(giving.body));

		});
		stompClient.subscribe(
				'/topic/2-dimensional/static_day_chart/init',
				function(init) {

					var initJSON = $.parseJSON(init.body);
					init = initJSON;
					drawBarChart1(init);
				});
		stompClient.subscribe(
				'/topic/2-dimensional/static_day_chart/update',
				function(update) {
					var updateJSON = $.parseJSON(update.body);
					updateBarChart(myChart1, updateJSON);
				});
		stompClient.subscribe('/topic/2-dimensional/user/init', function(init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart0(init);
		});
		stompClient.subscribe('/topic/2-dimensional/user/update', function(
				update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(myChart0, updateJSON);
		});
		stompClient.subscribe('/topic/2-dimensional/time/init', function(init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart1(init);
		});
		stompClient.subscribe('/topic/2-dimensional/time/update', function(
				update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(myChart1, updateJSON);
		});
		stompClient.subscribe('/topic/2-dimensional/generic/init', function(
				init) {

			var initJSON = $.parseJSON(init.body);
			init = initJSON;
			drawBarChart2(init);
		});
		stompClient.subscribe('/topic/2-dimensional/generic/update2', function(
				update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart2(myChart2, updateJSON);
			loading -= 1;
		});
		stompClient.subscribe('/topic/2-dimensional/generic/update', function(
				update) {
			var updateJSON = $.parseJSON(update.body);
			updateBarChart(myChart2, updateJSON);
		});
		stompClient.subscribe(
				'/topic/2-dimensional/time_based_single_user_chart/init',
				function(init) {

					var initJSON = $.parseJSON(init.body);
					init = initJSON;
					drawBarChart3(init);
				});
		stompClient.subscribe(
				'/topic/2-dimensional/time_based_single_user_chart/update',
				function(update) {
					var updateJSON = $.parseJSON(update.body);
					updateBarChart(myChart3, updateJSON);
				});
		stompClient.subscribe(
				'/topic/2-dimensional/time_based_single_user_chart/update2',
				function(update) {
					var updateJSON = $.parseJSON(update.body);
					updateBarChart2(myChart3, updateJSON);
					loading -= 1;
				});
		stompClient.subscribe(
				'/topic/2-dimensional/day_based_single_user_chart/init',
				function(init) {

					var initJSON = $.parseJSON(init.body);
					init = initJSON;
					drawBarChart5(init);
				});
		stompClient.subscribe(
				'/topic/2-dimensional/day_based_single_user_chart/update',
				function(update) {
					var updateJSON = $.parseJSON(update.body);
					updateBarChart(myChart5, updateJSON);
				});
		stompClient.subscribe(
				'/topic/2-dimensional/day_based_single_user_chart/update2',
				function(update) {
					var updateJSON = $.parseJSON(update.body);
					updateBarChart2(myChart5, updateJSON);
					loading -= 1;
				});
		console.log('open');
		stompClient.send(
				"/app/2-dimensional/static_day_chart/init", {},
				'test');
		stompClient.send("/app/2-dimensional/user/init", {}, 'test');
		stompClient.send("/app/2-dimensional/time/init", {}, 'test');
		stompClient.send("/app/2-dimensional/generic/init", {}, 'test');
		stompClient.send(
				"/app/2-dimensional/time_based_single_user_chart/init", {},
				'test');
		stompClient.send(
				"/app/2-dimensional/day_based_single_user_chart/init", {},
				'test');
	});
}

function updateBarChart(myChart, updateJSON) {
	var index = updateJSON.index;

	myChart.data.labels[index] = updateJSON.label;
	myChart.data.datasets[0].data[index] = updateJSON.data;
	myChart.data.datasets[0].backgroundColor[index] = updateJSON.color;
	myChart.update();
}

function updateBarChart2(myChart, init) {

	myChart.data.labels = init.labels;
	myChart.data.datasets[0].data = init.data;
	myChart.data.datasets[0].backgroundColor = init.color;
	myChart.update();
}
function drawBarChart0(init) {

	myChart0 = new Chart(ctx0, {
		type : 'doughnut',
		data : {
			labels : init.labels,
			datasets : [ {
				label : '0.1￥ ',
				data : init.data,
				borderWidth : 1,
				borderColor : 'rgba(13,190,152,0.7)',
				backgroundColor : init.color,// 'rgba(54, 162, 235, 1)',
				// hoverBackgroundColor:'rgba(75, 192, 192, 1)',
				// hoverBorderColor:'rgba(75, 192, 192, 1)',
				hoverBorderWidth : 1
			} ],
		},
		options : {
			legend : {
				display : false
			},
			animation : {
				duration : 150,
				easing : 'linear'
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

function drawBarChart1(init) {

	myChart1 = new Chart(ctx1, {
		type : 'bar',
		data : {
			labels : init.labels,
			datasets : [ {
				label : '0.1￥ ',
				data : init.data,
				borderWidth : 1,
				borderColor : 'rgba(54, 162, 235, 1)',
				backgroundColor : init.color,// 'rgba(54, 162, 235, 1)',
				// hoverBackgroundColor:'rgba(75, 192, 192, 1)',
				// hoverBorderColor:'rgba(75, 192, 192, 1)',
				hoverBorderWidth : 1
			} ],
		},
		options : {
			maintainAspectRatio: false,
			legend : {
				display : false
			},
			hover : {
				intersect : true,
				animationDuration : 0
			},

			onClick : function(e, r) {
				// alert(JSON.stringify(e));
				if (this.active[0] != null && loading==0) {
					// alert(
					//
					// this.active[0]._chart.config.data.labels[this.active[0]._index]//
					// this.active[0]._chart.config.data.datasets[0].data[this.active[0]._index]//_index//_chart.config.data.datasets[0].backgroundColor
					//
					// );
					stompClient
							.send(
									"/app/2-dimensional/generic/update2",
									{},
									this.active[0]._chart.config.data.labels[this.active[0]._index]);
					//alert(this.active[0]._chart.config.data.labels[this.active[0]._index]);
					loading +=1;
					updateBarChart2(myChart2, {
						data : [ 0 ],
						labels : [ 'LOADING' ],
						color : [ 'white' ]
					});
					$("#greetings tr").empty();
				}
			},
			animation : {
				duration : 150,
				easing : 'linear'
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

function drawBarChart2(init) {

	myChart2 = new Chart(
			ctx2,
			{
				type : 'doughnut',
				data : {
					labels : init.labels,
					datasets : [ {
						label : '0.1￥ ',
						data : init.data,
						borderWidth : 1,
						borderColor : 'rgba(54, 162, 235, 1)',
						backgroundColor : init.color,// 'rgba(54, 162, 235,
						// 1)',
						// hoverBackgroundColor:'rgba(75, 192, 192, 1)',
						// hoverBorderColor:'rgba(75, 192, 192, 1)',
						hoverBorderWidth : 1
					} ],
				},
				options : {
					tooltips : {
						enabled : true,
						mode : 'index',
						intersect : true
					},
					hover : {
						intersect : true,
						animationDuration : 0
					},
					onClick : function(e, r) {
						// alert(JSON.stringify(e));
						if (this.active[0] != null && loading==0) {
							// alert(
							//
							// this.active[0]._chart.config.data.labels[this.active[0]._index]//
							// this.active[0]._chart.config.data.datasets[0].data[this.active[0]._index]//_index//_chart.config.data.datasets[0].backgroundColor
							//
							// );
							stompClient
									.send(
											"/app/2-dimensional/time_based_single_user_chart/update2",
											{},
											this.active[0]._chart.config.data.labels[this.active[0]._index]);
							stompClient
							.send(
									"/app/2-dimensional/day_based_single_user_chart/update2",
									{},
									this.active[0]._chart.config.data.labels[this.active[0]._index]);
							loading +=2;
							updateBarChart2(myChart3, {
								data : [ 0 ],
								labels : [ 'LOADING' ],
								color : [ 'white' ]
							});
							updateBarChart2(myChart5, {
								data : [ 0 ],
								labels : [ 'LOADING' ],
								color : [ 'white' ]
							});
							$("#greetings tr").empty();
						}
					},

					legend : {
						display : false
					},

					animation : {
						duration : 150,
						easing : 'linear'
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

function drawBarChart3(init) {

	myChart3 = new Chart(ctx3, {
		type : 'bar',
		data : {
			labels : init.labels,
			datasets : [ {
				label : '0.1￥ ',
				data : init.data,
				borderWidth : 1,
				borderColor : 'rgba(54, 162, 235, 1)',
				backgroundColor : init.color,// 'rgba(54, 162, 235, 1)',
				// hoverBackgroundColor:'rgba(75, 192, 192, 1)',
				// hoverBorderColor:'rgba(75, 192, 192, 1)',
				hoverBorderWidth : 1
			} ],
		},
		options : {
			scales : {
				yAxes : [ {
					ticks : {
						beginAtZero : true
					}
				} ]
			},

			hover : {
				intersect : true,
				animationDuration : 0
			},

			onClick : function(e, r) {
				// alert(JSON.stringify(e));
				// //alert(
				// //this.active[0]._chart.config.data.labels[this.active[0]._index]
				// //this.active[0]._chart.config.data.datasets[0].data[this.active[0]._index]//_index//_chart.config.data.datasets[0].backgroundColor
				//	    				
				// //);
			},
			legend : {
				display : false
			},
			animation : {
				duration : 150,
				easing : 'linear'
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
function drawBarChart5(init) {

	myChart5 = new Chart(ctx5, {
		type : 'bar',
		data : {
			labels : init.labels,
			datasets : [ {
				label : '0.1￥ ',
				data : init.data,
				borderWidth : 1,
				borderColor : 'rgba(54, 162, 235, 1)',
				backgroundColor : init.color,// 'rgba(54, 162, 235, 1)',
				// hoverBackgroundColor:'rgba(75, 192, 192, 1)',
				// hoverBorderColor:'rgba(75, 192, 192, 1)',
				hoverBorderWidth : 1
			} ],
		},
		options : {
			scales : {
				yAxes : [ {
					ticks : {
						beginAtZero : true
					}
				} ]
			},

			hover : {
				intersect : true,
				animationDuration : 0
			},

			onClick : function(e, r) {
			
			},
			legend : {
				display : false
			},
			animation : {
				duration : 150,
				easing : 'linear'
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

var formatTime = function(timestamp) {

	var dt = new Date(parseInt(timestamp));

	var hours = dt.getHours();
	var minutes = dt.getMinutes();
	var seconds = dt.getSeconds();

	// the above dt.get...() functions return a single digit
	// so I prepend the zero here when needed
	if (hours < 10)
		hours = '0' + hours;

	if (minutes < 10)
		minutes = '0' + minutes;

	if (seconds < 10)
		seconds = '0' + seconds;

	return "(" + hours + ":" + minutes + ":" + seconds + ")";
}

function showGiving(giving) {
	var gid = giving.gid;

	if (gid == 196) {
		// alert(JSON.stringify(giving));
		$("#greetings").prepend(
				"<tr><td style=\"background-color:gold;color:black\">"
						+ formatTime(giving.timeStamp) + ": " + giving.userName
						+ " 送出一个火箭</td></tr>");
	}
	if (gid == 195) {
		$("#greetings").prepend(
				"<tr><td style=\"background-color:gray;color:gold\">"
						+ formatTime(giving.timeStamp) + ": " + giving.userName
						+ " 的飞机</td></tr>");
	}
	if (gid == 988) {
		// alert(JSON.stringify(giving));
		$("#greetings").prepend(
				"<tr><td style=\"background-color:gold;color:black\">"
						+ formatTime(giving.timeStamp) + ": " + giving.userName
						+ " 送出一个猫耳火箭</td></tr>");
	}
	if (gid == 997) {
		$("#greetings").prepend(
				"<tr><td style=\"background-color:gold;color:black\">"
						+ formatTime(giving.timeStamp) + ": " + giving.userName
						+ " 送出一个大头火箭</td></tr>");
	}
	if (gid == 999) {
		$("#greetings").prepend(
				"<tr><td style=\"background-color:gold;color:red;\">"
						+ formatTime(giving.timeStamp) + ": " + giving.userName
						+ " 送出一个超级火箭!!!</td></tr>");
	}
	ff();
}
function showGreeting(message) {
	$("#greetings").prepend("<tr><td>" + message + "</td></tr>");
	// window.scrollTo(0, document.body.scrollHeight);
	ff();
}

function ff() {
	var ol = document.getElementById("greetings");
	// if (ol.childElementCount > 100) {
	// ol.removeChild(ol.getElementsByTagName("tr")[0]);
	// }
}
function init() {
	ctx0 = document.getElementById("myChart0").getContext('2d');
	ctx1 = document.getElementById("myChart1").getContext('2d');
	ctx1.height=300;
	ctx2 = document.getElementById("myChart2").getContext('2d');
	ctx3 = document.getElementById("myChart3").getContext('2d');
	ctx5 = document.getElementById("myChart5").getContext('2d');
	connect();

}
$(document)
		.ready(
				function() {
					init();
					$(function() {
						
						$("form").on('submit', function(e) {
							e.preventDefault();
						});
						$("#connect").click(function() {
						});
						$("#disconnect").click(function() {
							disconnect();
						});
						$("#global-statistics")
//								function(){
//									alert(loading);
//								}
//						);
								.click(
										function() {

											if (loading==0) {
												stompClient
														.send(
																"/app/2-dimensional/time_based_single_user_chart/update2",
																{}, "global");
												loading +=1;
												updateBarChart2(myChart3, {
													data : [ 0 ],
													labels : [ 'LOADING' ],
													color : [ 'white' ]
												});
												$("#greetings tr").empty();
											}
										});
					});

				});
