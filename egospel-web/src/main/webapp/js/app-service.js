angular.module('dallanube.service', ['ngResource'])
    .service('stagingProp', function () {
        var item;
        var model = 'empty';

        return {
            getItem: function () {
                return item;
            },
            setItem: function(value) {
                item = value;
            },
            getModel: function () {
                return model;
            },
            setModel: function(value) {
                model = value;
            }
        };
    })
    .factory('ApplicationConfigService', ['$resource', '$http', function($resource, $http){
        var service = {
            applicationConfig: $resource('config/:configId'),
            get: function(param, callback){ return this.applicationConfig.get(param, callback) }, 
            query: function(){ return this.applicationConfig.query() },
            save: function(obj){
                if(obj.id == null){
                    return $http.post('config', obj);
                } else {
                    return $http.put('config/'+obj.id, obj);
                }
            }, 
            remove: function(obj){
                if(obj.id != null){
                    return $http.delete('config/'+obj.id);
                }
            }
        };
            
        return service;
    }])
    .factory('ApplicationSessionsService', ['$http', function($http){
        var service = {
            getCurrentUser: function() {
                return $http.get('homepage/myuserinfo');
            },
            list: function(){ 
                return $http.get('homepage/sessioninfo');
            }, 
            kick: function(user){
                return $http.delete('homepage/kick/'+user.sessionid);
            }
        };
            
        return service;
    }])
    .factory('MenuService', ['$resource', '$http', function($resource, $http){
        var service = {
            menu: $resource('menu/:id'),
            menu4Page: $resource('menu4page/:id', {}, {
                queryPage: {method:'GET', isArray: false}
            }),
            get: function(param, callback){ return this.menu.get(param, callback) }, 
            query: function(){ return this.menu.query() },
            query4Page: function(p, callback){ return this.menu4Page.queryPage({"page.page": p, "page.size": 10}, callback) },
            save: function(obj){
                if(obj.id == null){
                    return $http.post('menu', obj);
                } else {
                    return $http.put('menu/'+obj.id, obj);
                }
            }, 
            remove: function(obj){
                if(obj.id != null){
                    return $http.delete('menu/'+obj.id);
                }
            }
        };
            
        return service;
    }])
    .factory('PermissionService', ['$resource', '$http', function($resource, $http){
        var service = {
            permission: $resource('permission/:id'),
            permission4Page: $resource('permission4page/:id', {}, {
                queryPage: {method:'GET', isArray: false}
            }),
            get: function(param, callback){ return this.permission.get(param, callback) }, 
            query: function(){ return this.permission.query() },
            query4Page: function(p, callback){ return this.permission4Page.queryPage({"page.page": p, "page.size": 10}, callback) },
            save: function(obj){
                if(obj.id == null){
                    return $http.post('permission', obj);
                } else {
                    return $http.put('permission/'+obj.id, obj);
                }
            }, 
            remove: function(obj){
                if(obj.id != null){
                    return $http.delete('permission/'+obj.id);
                }
            }
        };
            
        return service;
    }])
    .factory('PostService', ['$resource', '$http', function($resource, $http){
        var service = {
            getDummyPost: function() {
                return $http.get('post/dummy');
            },
            post: $resource('post/:id'),
            post4Page: $resource('post4page/:id', {}, {
                queryPage: {method:'GET', isArray: false}
            }),
            get: function(param, callback){
                return this.post.get(param, callback) }, 
            get4Edit: function(id){
                return $http.get('post/'+id);
            },
            //query: function(){ return this.post.query() },
            query4Page: function(postType, p, callback){ 
                var posts = this.post4Page.queryPage({"postType" : postType, "page.page": p, "page.size": 10}, callback);
                return posts },
            save: function(obj){
                if(obj.id == null){
                    return $http.post('post', obj);                    
                } else {
                    return $http.put('post/'+obj.id, obj);
                }
            }, 
            remove: function(obj){
                if(obj.id != null){
                    return $http.delete('post/'+obj.id);
                }
            }
        };
            
        return service;
    }])
    .factory('RoleService', ['$resource', '$http', function($resource, $http){
        var service = {
            role: $resource('role/:id'),
            role4Page: $resource('role4page/:id', {}, {
                queryPage: {method:'GET', isArray: false}
            }),
            get: function(param, callback){ return this.role.get(param, callback) }, 
            query: function(){ return this.role.query() },
            query4Page: function(p, callback){ return this.role4Page.queryPage({"page.page": p, "page.size": 10}, callback) },
            save: function(obj){
                if(obj.id == null){
                    return $http.post('role', obj);
                } else {
                    return $http.put('role/'+obj.id, obj);
                }
            }, 
            remove: function(obj){
                if(obj.id != null){
                    return $http.delete('role/'+obj.id);
                }
            },
            unselectedPermission: function(obj){
                return $http.get('role/'+obj.id+'/unselected-permission');
            },
            unselectedMenu: function(obj){
                return $http.get('role/'+obj.id+'/unselected-menu');
            }
        };
            
        return service;
    }])
    .factory('UserService', ['$resource', '$http', function($resource, $http){
        var service = {
            user: $resource('user/:id'),
            user4Page: $resource('user4page/:id', {}, {
                queryPage: {method:'GET', isArray: false}
            }),
            get: function(param, callback){ return this.user.get(param, callback) }, 
            query: function(){ return this.user.query() },
            query4Page: function(p, callback){ return this.user4Page.queryPage({"page.page": p, "page.size": 10}, callback) },
            save: function(obj){
                //console.log("in UserService save ", obj)
                if(obj.id == null){
                    return $http.post('user', obj);
                } else {
                    return $http.put('user/'+obj.id, obj);
                }
            }, 
            remove: function(obj){
                if(obj.id != null){
                    return $http.delete('user/'+obj.id);
                }
            }
        };
            
        return service;
    }])
;