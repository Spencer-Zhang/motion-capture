var measurements = [];

function getData() {
  $.get('/data', function(data) {
    console.log(data);
    measurements.push(data);
  })
}

$(function() {
  var svg = d3.select('#canvas').append('svg')

  window.getData = getData
})