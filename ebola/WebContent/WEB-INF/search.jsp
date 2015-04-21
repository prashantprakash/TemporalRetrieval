<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- meta charec set -->
<meta charset="utf-8">
<!-- Always force latest IE rendering engine or request Chrome Frame -->
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<!-- Page Title -->
<title>Ebola</title>
<!-- Meta Description -->
<meta name="description" content="">
<!-- Mobile Specific Meta -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<style type="text/css">
.gm-style .gm-style-cc span, .gm-style .gm-style-cc a, .gm-style .gm-style-mtc div
	{
	font-size: 10px
}
</style>

<!--  <link rel="stylesheet" href="css/style.css" /> -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<style type="text/css">
html {
	height: 100%;
	width: 100%;
	overflow: hidden;
	min-width: 100%;
	min-height: 100%;
}

body {
	height: 100%;
	width: 100%;
	padding: 0;
	margin: 0;
}

.container-fluid {
	height: 100%;
	display: table;
	width: 100%;
	padding: 0;
}

.row-fluid {
	height: 100%;
	display: table-cell;
	vertical-align: middle;
}

.centering {
	float: none;
	margin: 0 auto;
}
</style>
<script>
	var inputtextval = "";
	var i = 0;
	$(document)
			.ready(
					function() {
						$("#searchresultdiv").hide();
						$("#results").hide();
						//$("#searchsubmit").click(function() {
						//console.log("search clicked");
						//inputtextval = $("#inptext").val();
						//$("#searchresultdiv").show();
						//$('#searchdiv').hide();
						//$("#inptext1").val(inputtextval);

						//});

						$("#inptext").keypress(function() {
							// console.log( "Handler for .keypress() called." );
							inputtextval = $("#inptext").val();
							console.log(inputtextval);
							console.log(i);

							if (i >= 1) {
								$("#searchresultdiv").show();
								$('#searchdiv').hide();
								$('#results').show();
								$("#inptext1").val(inputtextval);
								$("#inptext1").focus();
								$("#inptext1").keypress();
							}
							i = i + 1;

						});

						var availableTags;
						$.getJSON("query?choice=getquery", function(data) {
							console.log(data);
							availableTags = data;
						});

						$("#inptext1").autocomplete(
								{
									source : function(request, response) {
										var results = $.ui.autocomplete.filter(
												availableTags, request.term);

										response(results.slice(0, 10));
									}
								});

						$("#searchsubmit").click(
								function() {
									
									var query = $("#inptext").val();
									$.getJSON("query?choice=getresults&query="
											+ query, function(data) {
										console.log(data);
									});
								});

						$("#searchsubmit1")
								.click(
										function() {
											$('#results').empty();
											console.log("search1 clicked");
											var query = $("#inptext1").val();
											$
													.getJSON(
															"query?choice=getresults&query="
																	+ query,
															function(data) {
																console
																		.log(data);
																$('#results')
																		.append(
																				'<div class=\"row\">'
																						+ '<div class=\"col-xs-2\"></div>'
																						+ '<div class=\"col-xs-8\"><span>about x in y </span></div>');
																for (i = 0; i < data.length; i++) {
																	$(
																			'#results')
																			.append(
																					'<div class=\"row\"><div class=\"col-xs-2\"></div>'
																							+ '<div class=\"col-xs-8\"><div style=\"width:100%;height:25px;\">'
																							+ '<span style=\"float:left;\"> DocID:'
																							+ data[i].docId
																							+ '</span><span style=\"float:right;\"> Score:'
																							+ data[i].score
																							+ '</span></div><div><p>'
																							+ data[i].text
																							+ '</p></div></div></div>');
																}
															});
										});

					});
</script>
</head>
<body style="overflow: scroll;">
	<div class="container-fluid">
		<div id="searchdiv" class="row-fluid">
			<div class="centering text-center">
				<img src="img/search.jpg" width="370" height="150" /> <input
					id="inptext" type="text" class="form-control" placeholder=""
					style="width: 600px; margin-left: 215px;" />
				<button id="searchsubmit" type="submit" class="btn btn-primary"
					style="margin-top: 15px; width: 120px;">Search</button>
			</div>
		</div>

		<div id="searchresultdiv">
			<div class="row">
				<div class="col-xs-2">
					<img
						style="width: 100%; height: 68px; margin-top: 10px; border-radius: 179px; -webkit-border-radius: 155px; -moz-border-radius: 150px;"
						src="img/search.jpg">
				</div>
				<div class="col-xs-8"></div>
				<div class="col-xs-8">
					<div class="input-group">
						<input id="inptext1" type="text" class="form-control"
							placeholder="" style="width: 600px; margin-top: 20px;" /> <span
							class="input-group-btn">
							<button id="searchsubmit1" class="btn btn-primary" type="button"
								style="margin-top: 20px;">Search</button>
						</span>
					</div>
				</div>

			</div>
		</div>
		<div id="results">
			<!--  <div class="row">
				<div class="col-xs-2"></div>
				<div class="col-xs-8">
					<span>about x in y </span>
				</div>

			</div>-->
			<!--   <div class="row">
				<div class="col-xs-2"></div>

				<div class="col-xs-8">
					<p>Prashant is going to rock one day.Prashant is going to rock
						one day.Prashant is going to rock one day.Prashant is going to
						rock one day.Prashant is going to rock one day.Prashant is going
						to rock one day.Prashant is going to rock one day.Prashant is
						going to rock one day.Prashant is going to rock one day.Prashant
						is going to rock one day.</p>
				</div>
				
			</div>
			<br>
			<div class="row">
				<div class="col-xs-2"></div>

				<div class="col-xs-8">
					<div style="width:100%;">
					<span style="float:left;"> 
						 Doc Id: 1234 
					</span>
					
					<span style="float:right;"> 
						 Doc Id: 1234 
					</span>
					</div>
					<div>
					<p>Prashant is going to rock one day.Prashant is going to rock
						one day.Prashant is going to rock one day.Prashant is going to
						rock one day.Prashant is going to rock one day.Prashant is going
						to rock one day.Prashant is going to rock one day.Prashant is
						going to rock one day.Prashant is going to rock one day.Prashant
						is going to rock one day.</p></div>
				</div>
				
			</div>
			-->
			<!--  <div class="row">
				<div class="col-xs-2"></div>

				<div class="col-xs-8">
					<nav>
					<ul class="pagination">
						<li><a href="#" aria-label="Previous"> <span
								aria-hidden="true">&laquo;</span>
						</a></li>
						<li><a href="#">1</a></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">4</a></li>
						<li><a href="#">5</a></li>

						<li><a href="#" aria-label="Next"> <span
								aria-hidden="true">&raquo;</span>
						</a></li>
					</ul>
					</nav>
				</div>
			</div> -->
		</div>
	</div>

</body>
</html>