"www.w3school.com.cn" 
http://www.w3school.com.cn/tiy/t.asp?f=css3_transition1
1. 

	div
	{
	width:100px;
	height:100px;
	background:yellow;
	transition:width 2s;
	-moz-transition:width 2s; /* Firefox 4 */
	-webkit-transition:width 2s; /* Safari and Chrome */
	-o-transition:width 2s; /* Opera */
	}

	div:hover
	{
	width:300px;
	}


2. 

	html, body {
		background-color: #333;
	}

	@keyframes FadeIn {
		from {
			background-color: #d2f9f3;
		}
  
		to {
			background-color: white;
		}
	}
	
	.FoodConsumptionTable tr {
		background-color: white;
		animation: FadeIn 0.75s ease-in-out forwards;
		-webkit-animation: FadeIn 0.4s ease-in-out forwards; /* Safari and Chrome*/
	}