angular.module('dallanube', ['ngRoute', 'ui', 'dallanube.controller'])
    .config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {templateUrl: 'pages/home.html'})
            .when('/index', {templateUrl: 'index.html', controller: 'IndexController'})
            //.when('/signup', {templateUrl: 'signup.html', controller: 'SIgnupController'})
            .when('/401', {templateUrl: 'pages/404.html', controller: 'LoginRedirectorController'})
            .when('/system/config', {templateUrl: 'pages/system/config.html', controller: 'ApplicationConfigController'})
            .when('/system/sessions', {templateUrl: 'pages/system/sessions.html', controller: 'ApplicationSessionsController'})
            .when('/system/user', {templateUrl: 'pages/system/user.html', controller: 'UserController'})
            .when('/system/role', {templateUrl: 'pages/system/role.html', controller: 'RoleController'})
            .when('/system/permission', {templateUrl: 'pages/system/permission.html', controller: 'PermissionController'})
            .when('/system/menu', {templateUrl: 'pages/system/menu.html', controller: 'MenuController'})
            .when('/content/post', {templateUrl: 'pages/content/post.html', controller: 'PostController'})
            .when('/content/post-new', {templateUrl: 'pages/content/post-new.html', controller: 'PostNewController'})
            .when('/about', {templateUrl: 'pages/about.html', controller: 'AboutController'})
            .otherwise({templateUrl: 'pages/404.html'});
    }])
    .config(['$httpProvider', function($httpProvider){
        var sessionTimeoutInterceptor = ['$rootScope', '$location', '$q', function($rootScope, $location, $q){
            function success(response){
                return response;
            }
            
            function error(response){
                if (response.status === 401) {
                    $location.path('/401');
                }
            }
            
            return function(promise) {
                return promise.then(success, error);
            }
        }];
        
        
        $httpProvider.responseInterceptors.push(sessionTimeoutInterceptor);
        $httpProvider.responseInterceptors.push('httpLoadingSpinner');
        var spinnerFunction = function (data, headersGetter) {
            $('#loading').show();
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    }])
    .factory('httpLoadingSpinner', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                // do something on success
                $('#loading').hide();
                return response;

            }, function (response) {
                // do something on error
                $('#loading').hide();
                return $q.reject(response);
            });
        };
    })
    .directive('ckEditor', function() {
      return {
        require: '?ngModel',
        link: function(scope, elm, attr, ngModel) {
          var ck = CKEDITOR.replace(elm[0]);

          if (!ngModel) return;

          ck.on('instanceReady', function() {
            ck.setData(ngModel.$viewValue);
          });

          function updateModel() {
              scope.$apply(function() {
                  ngModel.$setViewValue(ck.getData());
              });
          }

          ck.on('change', updateModel);
          ck.on('key', updateModel);
          ck.on('dataReady', updateModel);

          ngModel.$render = function(value) {
            ck.setData(ngModel.$viewValue);
          };
        }
      };
    })
;