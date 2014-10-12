var measurements = [];
var delay = 0;

function getData() {
  $.get('/data', function(data) {
    if(measurements.length > 0 || data['created_at'] != measurements[measurements.length-1]['created_at']) {
      measurements.push(data);
      delay = 0;
    } else {
      delay += 1;
      if(delay >= 2) {
        measurements.push(undefined)
        delay = 0;
      }
    }
  })

  $('#test').text(measurements);
}

$(function() {
  var svg = d3.select('#canvas').append('svg')

  setInterval(function() {
    getData()
  }, 500);
})