var DataController = ['$scope', '$http', function($scope, $http) {

  this.getData = function() {
    $http.get('http://192.168.1.53')
    .success(function(body) {
      console.log(body)
    })
    .error(function(body) {
      console.log(body)
    })
  }

}]