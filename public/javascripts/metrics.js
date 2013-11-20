var colorSet = {ok:"#5cb85c", warn:"#f0ad4e", error:"#d9534f", neutral: "#46426B"}

var allMetrics = null;
var apiStatus = null;

//  Charts //
var memoryChart = null;
var requestRateChart = null;
//  Charts //

$(function(){
    requestMetrics();
    requestStatus();
});

function populateData()
{
    drawMemoryChart(allMetrics);
    populateLatency(allMetrics);
    drawRequestRateChart(allMetrics);
}

function drawMemoryChart(allMetrics)
{
    $(".max-heap").html(getMb(allMetrics["gauges"]["application.memory-usage.heap.max"].value));
    $(".used-heap-mb").html(getMb(allMetrics["gauges"]["application.memory-usage.heap.used"].value));
    var usedHeap = allMetrics["gauges"]["application.memory-usage.heap.usage"].value;
    var uh = parseInt(usedHeap * 100);
    var c = colorSet.ok;
    if (uh > 70 && uh <= 90)
    {
        c = colorSet.warn;
    }
    else if(uh > 90)
    {
        c = colorSet.error;
    }

    $(".used-heap").css("color", c).html(uh + "%");
    if (memoryChart == null)
    {
        var ctx = $(".heap-utilization").get(0).getContext("2d");
        memoryChart = new Chart(ctx);

        var data = [
            {
                value: parseInt(usedHeap * 100),
                color: c
            },
            {
                value: 100 - parseInt(usedHeap * 100),
                color: colorSet.neutral
            }
        ];
        memoryChart.Doughnut(data);
    }
}

function drawRequestRateChart(allMetrics)
{
    var requestPerMinuteMean = convertPerSecToPerMin(allMetrics["timers"]["allrequests.timer"]["meanRate"]);
    var requestPerMinuteFifteen = convertPerSecToPerMin(allMetrics["timers"]["allrequests.timer"]["fifteenMinuteRate"]);
    var requestPerMinuteOne = convertPerSecToPerMin(allMetrics["timers"]["allrequests.timer"]["oneMinuteRate"]);

    if (requestRateChart == null)
    {
        var ctx = $(".request-rate").get(0).getContext("2d");
        requestRateChart = new Chart(ctx);

        var options = {

            //Boolean - If we show the scale above the chart data
            scaleOverlay : false,

            //Boolean - If we want to override with a hard coded scale
            scaleOverride : false,

            //** Required if scaleOverride is true **
            //Number - The number of steps in a hard coded scale
            scaleSteps : null,
            //Number - The value jump in the hard coded scale
            scaleStepWidth : null,
            //Number - The scale starting value
            scaleStartValue : null,

            //String - Colour of the scale line
            scaleLineColor : "rgba(0,0,0,0)",

            //Number - Pixel width of the scale line
            scaleLineWidth : 1,

            //Boolean - Whether to show labels on the scale
            scaleShowLabels : false,

            //Interpolated JS string - can access value
            scaleLabel : "<%=value%>",

            //String - Scale label font declaration for the scale label
            scaleFontFamily : "'Arial'",

            //Number - Scale label font size in pixels
            scaleFontSize : 12,

            //String - Scale label font weight style
            scaleFontStyle : "normal",

            //String - Scale label font colour
            scaleFontColor : "#666",

            ///Boolean - Whether grid lines are shown across the chart
            scaleShowGridLines : false,

            //String - Colour of the grid lines
            scaleGridLineColor : "rgba(0,0,0,.05)",

            //Number - Width of the grid lines
            scaleGridLineWidth : 0,

            //Boolean - If there is a stroke on each bar
            barShowStroke : true,

            //Number - Pixel width of the bar stroke
            barStrokeWidth : 2,

            //Number - Spacing between each of the X value sets
            barValueSpacing : 5,

            //Number - Spacing between data sets within X values
            barDatasetSpacing : 1,

            //Boolean - Whether to animate the chart
            animation : true,

            //Number - Number of animation steps
            animationSteps : 60,

            //String - Animation easing effect
            animationEasing : "easeOutQuart",

            //Function - Fires when the animation is complete
            onAnimationComplete : null

        }

        var data = {
            labels : ["mean", "15", "1"],
            datasets : [
                {
                    fillColor : "#46426B",
                    strokeColor : "rgba(220,220,220,1)",
                    data : [requestPerMinuteMean, requestPerMinuteFifteen, requestPerMinuteOne]
                }
            ]
        };
        requestRateChart.Bar(data, options);
    }

    $(".request-minute-mean").html(requestPerMinuteMean);
    $(".request-minute-fifteen").html(requestPerMinuteFifteen);
    $(".request-minute-one").html(requestPerMinuteOne);
}

function populateLatency(allMetrics)
{
    var meanR = convertNanoToSeconds(allMetrics["timers"]["allrequests.timer"]["snapshot"]["mean"]);
    var medianR = convertNanoToSeconds(allMetrics["timers"]["allrequests.timer"]["snapshot"]["median"])
    $(".response-mean").html(meanR + " sec").css("color", getLatencyColor(meanR));
    $(".response-median").html(medianR + " sec").css("color", getLatencyColor(medianR));
    $(".response-max").html(convertNanoToSeconds(allMetrics["timers"]["allrequests.timer"]["snapshot"]["max"]) + " sec");

    $(".response-seventy-five").html(convertNanoToSeconds(allMetrics["timers"]["allrequests.timer"]["snapshot"]["75thPercentile"]) + " sec");
    $(".response-ninety-five").html(convertNanoToSeconds(allMetrics["timers"]["allrequests.timer"]["snapshot"]["95thPercentile"]) + " sec");
}

function requestMetrics()
{
    $.getJSON("/admin/metrics", function (data){
        allMetrics = data;
        populateData();
    });
}

function requestStatus()
{
    $.getJSON("/admin/apistatus", function (data){
        apiStatus = data;
        if (apiStatus["event"])
        {
            $(".status-event").html("Ok").removeClass(["ok", "error", "neutral"]).addClass("ok");
        }

        if (apiStatus["type"])
        {
            $(".status-type").html("Ok").removeClass(["ok", "error", "neutral"]).addClass("ok");
        }

        if(apiStatus["type"] && apiStatus["event"])
        {
            $(".status-id").html("Ok").removeClass(["ok", "error", "neutral"]).addClass("ok");
        }
    }).error(function(){
        $(".status-event, .status-type, .status-id").html("error").removeClass(["ok", "error", "neutral"]).addClass("error");
    });
}


// Utilities
    function getMb(value)
    {
        return parseInt(parseInt(value) / 1024 / 1024);
    }

    function convertNanoToSeconds(value)
    {
        var v = value / 1000000000;
        return Math.round(v * 1000) / 1000;
    }

    function getLatencyColor(value)
    {
        var c = colorSet.ok;
        if (value > 3 && value <= 10)
        {
            c = colorSet.warn;
        }
        else if(value > 10)
        {
            c = colorSet.error;
        }
        return c;
    }

    function convertPerSecToPerMin(value)
    {
        value = value * 60;
        return Math.round(value * 100) / 100;
    }
// Utilities