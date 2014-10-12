var measurements = [];
var temperatures = [];

function getData() {
  $.get('/data', function(data) {
    measurements.push(data);
    if(getDelay() > 4) {
      temperatures.push(20)
    } else {
      temperatures.push(data['temperature'])
    }

    if(temperatures.length > 50) {
      temperatures.splice(0, 1)
    }

    updateStatus();
    drawLines();
  })
}

function getDelay() {
  if(measurements.length == 0) {
    return 99;
  } else {
    var index = measurements.length - 1;
    var latestTime = measurements[index]['created_at'];
    index -= 1
    while(index >= 0) {
      if(measurements[index]['created_at'] != latestTime) { break }
      index -= 1;
    }
    return measurements.length - 1 - index;
  }
}

function updateStatus() {
  if(getDelay() > 5) {
    $('#status').text('OK').css('background-color', '#66BB66')
  } else {
    $('#status').text(temperatures[temperatures.length-1] + "\xb0C")
      .css('background-color', 'red')
  }
}

var y = d3.scale.linear().domain([20,0]).range([50, 250])
var x = d3.scale.linear().domain([-50, 0]).range([50, 750])

function drawLines() {
  d3.select('#lines').selectAll('path').remove()

  var line = d3.svg.line()
    .interpolate('cardinal')
    .x(function(d,i) { return x(i - temperatures.length) })
    .y(function(d) { return y(d) })

  d3.select('#lines').append("svg:path").attr('d', line(temperatures))
    .attr("stroke", "blue")
    .attr('fill', 'none')
    .attr('stroke-width', '4')
}

$(function() {
  var svg = d3.select('#canvas').append('svg')
    .attr('width', 800)
    .attr('height', 300)

  var lines = svg.append('g')
    .attr('id', 'lines')

  lines.append("svg:line")
    .attr("x1", x(-50))
    .attr("y1", y(0))
    .attr("x2", x(0))
    .attr("y2", y(0))
    .attr('stroke', 'black')

  lines.selectAll("horizLines")
    .data(y.ticks(4)).enter()
      .append("svg:line")
      .attr("x1", x(-50))
      .attr("y1", function(d) {return y(d)})
      .attr("x2", x(0))
      .attr("y2", function(d) {return y(d)})
      .attr('stroke', '#cccccc')

  lines.selectAll("horizLabel")
    .data(y.ticks(4)).enter()
      .append("svg:text")
      .text(String)
      .attr('x', x(1)-3)
      .attr('y', function(d) {return y(d)+5})
 
  lines.append("svg:line")
    .attr("x1", x(0))
    .attr("y1", y(0))
    .attr("x2", x(0))
    .attr("y2", y(20))
    .attr('stroke', 'black')


  setInterval(function() {
    getData()
  }, 500);
})