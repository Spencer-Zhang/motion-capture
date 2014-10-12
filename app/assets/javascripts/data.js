var measurements = [];

function getData() {
  $.get('/data', function(data) {
    measurements.push(data);
  })

  $('#test').text(measurements[measurements.length - 1]);
}

$(function() {
  var svg = d3.select('#canvas').append('svg')

  setInterval(function() {
    getData()
  }, 500);
})