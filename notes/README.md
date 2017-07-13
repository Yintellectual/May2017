"http://www.chartjs.org" 

further readings:

	- data graphics and color vision
	http://betweentwobrackets.com/data-graphics-and-colour-vision/
	
		"Use labels to ensure accessibility of your charts."
	
	
	- CanvasPattern
	https://developer.mozilla.org/en-US/docs/Web/API/CanvasGradient
	
	- Patternomaly 
	https://github.com/ashiguruma/patternomaly

0. Configuration 

1. bar chart

	http://www.chartjs.org/docs/latest/charts/bar.html
	
	1.1 basic usage
		var myBarChart = new Chat(ctx, {
			type:'bar',
			data: data,
			options:options
		});
		
		1.1.1 type: 'horizontalBar' will make a horizontal bar chart
		
	1.2 data 
		
		1.2.1 data is a json consists of two properties:
		
			1.2.1.1 labels
			
				1.2.1.1.1 labels is an array of string labels
				
			1.2.1.2 datasets
			
				1.2.1.2.1 datasets is an array of datasets
				
				1.2.1.2.2 a dataset can have properties  below:
					1.2.1.2.2.0 data
					1.2.1.2.2.1 label
					1.2.1.2.2.2 xAxisID 
					1.2.1.2.2.3 yAxisID
					1.2.1.2.2.4 backgroundColor
					1.2.1.2.2.5 borderColor
					1.2.1.2.2.6 borderWidth
					1.2.1.2.2.7 borderSkipped
						-Options are
							-"bottom"
							-"left"
							-"top"
							-"right"
							
					1.2.1.2.2.8 hoverBackgroundColor
					1.2.1.2.2.9 hoverBorderColor
					1.2.1.2.2.10 hoverBorderWidth
					
	1.3 add and update data
	
		1.3.1 https://codepen.io/k3no/pen/gwdYzr
		
			1.3.1.1 set data like 
			
				myBarChart.data.labels[13] ="2018";
				myBarChart.data.datasets[0].data[13] = 600;
						
			1.3.1.2 update chart by calling
			
				myBarChart.update();
				
2. Charts and configurations

	2.1 General
	
		2.1.0 This part defines some "constants" that can be refered to in both data or options
		
		2.1.1 colors
		
			2.1.1.0 colors can be hexadecimal, rgb, or hsl
			
			2.1.1.1 default color is stored in Chart.defaults.global.defaultColor 
				
				- 'rgba(0,0,0,0.1)'
			
			2.1.1.2 a "CanvasPattern" or "CanvasGradient" can be used as color. 
			
			2.1.1.2 "Patternomaly" library can be used to generate patterns.
	
		2.1.2 fonts
		
			2.1.2.1 default font options: 
				
				- Chart.defaults.global.defaultFontColor
				- Chart.defaults.global.defaultFontFamily
				- Chart.defaults.global.defaultFontSize
				- Chart.defaults.global.defaultFontStyle
			
		2.1.3 Hover Interactions
		
			2.1.3.1 default hover configuration:
				
					Chart.default.global.hover
					
			2.1.3.2 "model"
			
			2.1.3.3 "intersect"
			
			2.1.3.4 "animationDuration"
			
			2.1.3.5 configure hover interactions under
			
					options.hover
					
		2.1.4 Responsive
		
			DO NOT reply on <canvas> to achieve responsive effects, use its parent div instead.
			
	2.2 Configuration
	
		2.2.1 global configurations are in 
			
			Chart.defaults.global
			
		2.2.2 .animation
		
			- duration
			
			- easing
			
			- onProgress
			
			- onComplete
		
		2.2.3 .layout
		
			- padding
			
				- left
				
				- right
				
				- top
				
				- bottom
				
		2.2.4 .legend
		
			- display
			
			- position
			
			- fullWidth
			
			- onClick 
			// can be used to create a link
			// has access to an argument of "item" interface
			// function(e, item)
			//"item"
{
    // Label that will be displayed
    text: String,

    // Fill style of the legend box
    fillStyle: Color,

    // If true, this item represents a hidden dataset. Label will be rendered with a strike-through effect
    hidden: Boolean,

    // For box border. See https://developer.mozilla.org/en/docs/Web/API/CanvasRenderingContext2D/lineCap
    lineCap: String,

    // For box border. See https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/setLineDash
    lineDash: Array[Number],

    // For box border. See https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/lineDashOffset
    lineDashOffset: Number,

    // For box border. See https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/lineJoin
    lineJoin: String,

    // Width of box border
    lineWidth: Number,

    // Stroke style of the legend box
    strokeStyle: Color

    // Point style of the legend box (only used if usePointStyle is true)
    pointStyle: String
}
			
			- onHover
			
			- reverse
			
			- labels
			
				- boxWidth
				
				- fontSize
				
				- fontStyle
				
				- fontColor
				
				- fontFamily
				
				- padding
				
				- generateLabels
				
				- filter
				
				- usePointStyle
		
			- HTML legends, legendCallback: HTML function(chart)
			
		2.2.5 Title
		
			- options.title
			
			- Chart.default.global.title
			
			- desplay
			
			- position
			
				- top, left, bottom, right
			
			- fontSize
			
			- fontFamily
			
			- fontColor
			
			- fontStyle
			
			- padding
			
			- text
			
		2.2.6 Tooltips ?? what is tooltips

		2.2.7 elements
		
			- point
			
			- line
			
			- rectangle
			
			- arc
			
	3. charts
	
		- line: show trend data, or comparison of two data sets
		
		- bar: show trend data, or comparison of multiple data sets
		
		- radar: comparing two or more different data sets
		
		- doughnut & pie: best to show relational proportions between data
		
		- polar area: like a pie, but more fun
		
		- bubble: bubble is used to display three dimensions at a time
		
		- scatter: ??
		
		- area: both line and radar charts can be made area by enabling "fill"
		
		- mixed: a bar chart can also include a line dataset
		
	4. axes
	
		- cartesian axes are for bar and line charts
		
			- linear
			
			- logarithmic //幂
			
			- category ??
			
			- time
		
		- radial axes are for radial charts
		
		- labelling
		
			achieved through scales.yAxes[0].ticks.callback = function(value, index, values){}
			
		- styling
		
			configure under the scale configuration in the gridLines key
			
			- display
			
			- color
			
			- borderDash
			
			- borderDashOffset
			
			- lineWidth
			
			- drawBorder
			
			- drawOnChartArea
			
			- drawTicks
			
			- tickMarkLength
			
			- zeroLineWidth
			
			- zeroLineColor
			
			- zeroLineBorderDash
			
			- zeroLineBorderDashOffset
			
			- offsetGridLines
		
			configure under the scale configuration in the ticks key
			
			- callback
			
			- display
			
			- fontColor
			
			- fontFamily
			
			- fontSize
			
			- fontStyle
			
			- reverse
			
			
		