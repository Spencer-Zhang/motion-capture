var measurements = [];

function getData() {
  $.get('/data', function(data) {
    measurements.push(data);
  })

  $('#test').text(measurements[measurements.length - 1]['created_at']);
  $('#delay').text(getDelay());
}

function getDelay() {
  if(measurements.length == 0) {
    return 0;
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

$(function() {
  var svg = d3.select('#canvas').append('svg')

  setInterval(function() {
    getData()
  }, 500);
})