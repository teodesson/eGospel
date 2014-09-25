angular.module('gospel.controller', ['gospel.service', 'ngUpload'])
        .controller('LoginRedirectorController', ['$window', function($window) {
                $window.location = 'login.html';
            }])
        .controller('MenubarController', ['$http', '$scope', function($http, $scope) {
                $scope.userinfo = {};
                $http.get('gospel/homepage/userinfo').success(function(data) {
                    $scope.userinfo = data;
                });
            }])
        .controller('AboutController', ['$http', '$scope', function($http, $scope) {
                $scope.appinfo = {};
                $http.get('gospel/homepage/appinfo').success(function(data) {
                    $scope.appinfo = data;
                });
            }])
        .controller('ApplicationSessionsController', ['ApplicationSessionsService', '$scope', function(ApplicationSessionsService, $scope) {
                $scope.refresh = function() {
                    ApplicationSessionsService.list().success(function(data) {
                        $scope.sessioninfo = data
                    });
                }
                $scope.refresh();
                $scope.kick = function(user) {
                    ApplicationSessionsService.kick(user).success($scope.refresh);
                };

            }])
        .controller('ApplicationConfigController', ['$scope', 'ApplicationConfigService', function($scope, ApplicationConfigService) {
                $scope.configs = ApplicationConfigService.query();
                $scope.edit = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.currentConfig = ApplicationConfigService.get({configId: x.id}, function(data) {
                        $scope.original = angular.copy(data);
                    });
                };
                $scope.add = function() {
                    $scope.currentConfig = null;
                    $scope.original = null;
                }
                $scope.save = function() {
                    ApplicationConfigService.save($scope.currentConfig)
                            .success(function() {
                                $scope.configs = ApplicationConfigService.query();
                                $scope.add();
                            });
                }
                $scope.remove = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    ApplicationConfigService.remove(x).success(function() {
                        $scope.configs = ApplicationConfigService.query();
                    });
                }
                $scope.isClean = function() {
                    return angular.equals($scope.original, $scope.currentConfig);
                }
                $scope.isConfigNameAvailable = function(value) {
                    if ($scope.currentConfig != null && $scope.currentConfig.id != null) {
                        return true;
                    }
                    for (var i = 0; i < $scope.configs.length; i++) {
                        var u = $scope.configs[i];
                        if (u.name === value) {
                            return false;
                        }
                    }
                    return true;
                }
            }])
        .controller('MenuController', ['$scope', 'MenuService', function($scope, MenuService) {
                $scope.menus = MenuService.query();
                $scope.reloadMenupage = function(page) {
                    if (!page || page < 0) {
                        page = 0;
                    }

                    $scope.menupage = MenuService.query4Page(page, function() {
                        $scope.pages = _.range(1, ($scope.menupage.totalPages + 1));
                    });
                }
                $scope.reloadMenupage();

                $scope.edit = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.currentMenu = MenuService.get({id: x.id}, function(data) {
                        $scope.original = angular.copy(data);
                    });

                    $scope.parentSelection = _.filter($scope.menus, function(m) {
                        return m.id != x.id;
                    });
                };
                $scope.add = function() {
                    $scope.currentMenu = null;
                    $scope.original = null;
                }
                $scope.save = function() {
                    MenuService.save($scope.currentMenu)
                            .success(function() {
                                $scope.reloadMenupage();
                                $scope.add();
                            });
                }
                $scope.remove = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    MenuService.remove(x).success(function() {
                        $scope.reloadMenupage();
                    });
                }
                $scope.isClean = function() {
                    return angular.equals($scope.original, $scope.currentMenu);
                }
//        $scope.isParentSelected = function(value){
//            if ($scope.currentMenu.parent.id == value){
//                return true;
//            }
//            else {
//                return false;
//            }
//        }  
            }])
        .controller('PermissionController', ['$scope', 'PermissionService', function($scope, PermissionService) {
                $scope.permissions = PermissionService.query();
                $scope.reloadPermissionpage = function(page) {
                    if (!page || page < 0) {
                        page = 0;
                    }

                    $scope.permissionpage = PermissionService.query4Page(page, function() {
                        $scope.pages = _.range(1, ($scope.permissionpage.totalPages + 1));
                    });
                }
                $scope.reloadPermissionpage();

                $scope.edit = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.currentPermission = PermissionService.get({id: x.id}, function(data) {
                        $scope.original = angular.copy(data);
                    });
                };
                $scope.add = function() {
                    $scope.currentPermission = null;
                    $scope.original = null;
                }
                $scope.save = function() {
                    PermissionService.save($scope.currentPermission)
                            .success(function() {
                                $scope.reloadPermissionpage();
                                $scope.add();
                            });
                }
                $scope.remove = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    PermissionService.remove(x).success(function() {
                        $scope.reloadPermissionpage();
                    });
                }
                $scope.isClean = function() {
                    return angular.equals($scope.original, $scope.currentPermission);
                }
                $scope.isPermissionValueAvailable = function(value) {
                    if ($scope.currentPermission != null && $scope.currentPermission.id != null) {
                        return true;
                    }
                    for (var i = 0; i < $scope.permissions.length; i++) {
                        var u = $scope.permissions[i];
                        if (u.value === value) {
                            return false;
                        }
                    }
                    return true;
                }
            }])
        .controller('PostController', ['$scope', '$location', 'PostService', 'stagingProp', function($scope, $location, PostService, stagingProp) {
                //$scope.posts = PostService.query();
//        $scope.go = function ( path ) {
//            $location.path( path );
//        };

                $scope.reloadPostpage = function(page) {
                    if (!page || page < 0) {
                        page = 0;
                    }

                    $scope.postpage = PostService.query4Page('post', page, function() {
                        $scope.pages = _.range(1, ($scope.postpage.totalPages + 1));
                    });
                }

                $scope.reloadPostpage();

                $scope.edit = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.currentPost = PostService.get({id: x.id}, function(data) {
                        $scope.original = angular.copy(data);

                        stagingProp.setModel('post');
                        stagingProp.setItem($scope.currentPost.id);

                        //$window.location.href = '/post-new';
                        $location.path('/content/post-new');
                    });
                };
                $scope.add = function() {
                    //$scope.currentPost = null;
                    //$scope.original = null;
                    $location.path('/content/post-new');
                }
                $scope.save = function() {
                    //construct object
                    //$scope.currentPost = PostService.getDummyPost();
                    $scope.currentPost.author = {};//ApplicationSessionsService.getCurrentUser();           

                    //$scope.currentPost.postOn = undefined;
                    //$scope.currentPost.postStatus = 'draft';
                    //$scope.currentPost.commentStatus = 'open';
                    //$scope.currentPost.postModified = undefined;

                    PostService.save($scope.currentPost)
                            .success(function() {
                                $scope.posts = PostService.query();
                                $scope.add();
                            });
                }
                $scope.remove = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    PostService.remove(x).success(function() {
                        $scope.posts = PostService.query();
                    });
                }
                $scope.isClean = function() {
                    return angular.equals($scope.original, $scope.currentPost);
                }
            }])
        .controller('PostNewController', ['$scope', '$location', 'PostService', 'stagingProp', function($scope, $location, PostService, stagingProp) {
                //$scope.posts = PostService.query();
                $scope.edit = function(x) {
                    if (x == null) {
                        return;
                    }
                    //$scope.currentPost = PostService.get({id: x}, function(data){
                    //    $scope.original = angular.copy(data);                
                    //})
                    $('#loading').show();
                    setTimeout(function() {
                        $scope.$apply(function() {
                            PostService.get4Edit(x).success(function(data) {
                                $scope.currentPost = angular.copy(data);
                                $('#loading').hide();
                            });
                        });
                    }, 1000);
                };
                $scope.add = function() {
                    if (stagingProp.getModel() == 'post') { // there is value
                        $scope.edit(stagingProp.getItem());
                    }
                    else {
                        $scope.currentPost = null;
                        $scope.original = null;
                    }
                    stagingProp.setModel('empty');
                }

                angular.element(document).ready(function() {
                    //document.getElementById('msg').innerHTML = 'Hello';
                    $scope.add();
                });

                $scope.save = function() {
                    //construct object
                    //$scope.currentPost = PostService.getDummyPost();
                    $scope.currentPost.author = {};//ApplicationSessionsService.getCurrentUser();           

                    //$scope.currentPost.postOn = undefined;
                    //$scope.currentPost.postStatus = 'draft';
                    //$scope.currentPost.commentStatus = 'open';
                    //$scope.currentPost.postModified = undefined;

                    PostService.save($scope.currentPost)
                            .success(function() {
                                // leave it alone  
                                //$scope.posts = PostService.query();
                                //$scope.add();
                            });
                }
                $scope.remove = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    PostService.remove(x).success(function() {
                        $scope.posts = PostService.query();
                    });
                }
                $scope.isClean = function() {
                    return angular.equals($scope.original, $scope.currentPost);
                }
            }])
        .controller('RoleController', ['$scope', 'RoleService', function($scope, RoleService) {
                //$scope.roles = RoleService.query();

                $scope.unselectedPermission = [];
                $scope.unselectedMenu = [];

                $scope.selectedPermission = [];
                $scope.selectedMenu = [];

                $scope.reloadRolepage = function(page) {
                    if (!page || page < 0) {
                        page = 0;
                    }

                    $scope.rolepage = RoleService.query4Page(page, function() {
                        $scope.pages = _.range(1, ($scope.rolepage.totalPages + 1));
                    });
                }
                $scope.reloadRolepage();

                $scope.edit = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.currentRole = RoleService.get({id: x.id}, function(data) {
                        $scope.original = angular.copy(data);
                    });

                    RoleService.unselectedPermission(x).success(function(data) {
                        $scope.unselectedPermission = data;
                    });
                    RoleService.unselectedMenu(x).success(function(data) {
                        $scope.unselectedMenu = data;
                    });
                };
                $scope.prepareDelete = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.deleteRole = x;
                };
                $scope.add = function() {
                    $scope.currentRole = null;
                    $scope.original = null;

                    $scope.unselectedPermission = [];
                    $scope.unselectedMenu = [];

                    $scope.selectedPermission = [];
                    $scope.selectedMenu = [];
                }
                $scope.save = function() {
                    RoleService.save($scope.currentRole)
                            .success(function() {
                                $scope.reloadRolepage();
                                $scope.add();
                            });
                }
                $scope.remove = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    RoleService.remove(x).success(function() {
                        $scope.reloadRolepage();
                    });
                }
                $scope.isClean = function() {
                    return angular.equals($scope.original, $scope.currentRole);
                }
//        $scope.isRoleNameAvailable = function(value){
//            if($scope.currentRole != null && $scope.currentRole.id != null){
//                return true;
//            }            
//            
//            for(var i = 0; i < $scope.roles.length; i++){
//                var u = $scope.roles[i];
//                if(u.name === value){
//                    return false;
//                }
//            }
//            return true;
//        }        
                $scope.selectAllPermission = function($event) {
                    if ($event.target.checked) {
                        for (var i = 0; i < $scope.unselectedPermission.length; i++) {
                            var p = $scope.unselectedPermission[i];
                            if ($scope.selectedPermission.indexOf(p.id) < 0) {
                                $scope.selectedPermission.push(p.id);
                            }
                        }
                    } else {
                        $scope.selectedPermission = [];
                    }
                }
                $scope.updateSelectedPermission = function($event, id) {
                    var checkbox = $event.target;
                    if (checkbox.checked && $scope.selectedPermission.indexOf(id) < 0) {
                        $scope.selectedPermission.push(id);
                    } else {
                        $scope.selectedPermission.splice($scope.selectedPermission.indexOf(id), 1);
                    }
                }
                $scope.isPermissionSelected = function(id) {
                    return $scope.selectedPermission.indexOf(id) >= 0;
                }
                $scope.isAllPermissionSelected = function() {
                    return $scope.unselectedPermission.length === $scope.selectedPermission.length;
                }
                $scope.saveSelectedPermission = function() {
                    console.log($scope.selectedPermission);
                    if ($scope.selectedPermission.length < 1)
                        return;
                    for (var i = 0; i < $scope.selectedPermission.length; i++) {
                        var p = {id: $scope.selectedPermission[i]};
                        $scope.currentRole.permissionSet.push(p);
                    }
                    RoleService.save($scope.currentRole)
                            .success(function() {
                                RoleService.unselectedPermission($scope.currentRole)
                                        .success(function(data) {
                                            $scope.unselectedPermission = data;
                                            $scope.currentRole = RoleService.get({
                                                id: $scope.currentRole.id
                                            });
                                        });
                            });
                    $('#dialogPermission').modal('hide');
                }
                $scope.cancelSelectedPermission = function() {
                    $scope.selectedPermission = [];
                    console.log($scope.selectedPermission);
                    $('#dialogPermission').modal('hide');
                }
                $scope.removeSelectedPermission = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    var ixPermission = -1;
                    for (var i = 0; i < $scope.currentRole.permissionSet.length; i++) {
                        if (x.id === $scope.currentRole.permissionSet[i].id) {
                            ixPermission = i;
                            break;
                        }
                    }
                    if (ixPermission >= 0) {
                        $scope.currentRole.permissionSet.splice(ixPermission, 1);
                        RoleService.save($scope.currentRole)
                                .success(function() {
                                    RoleService.unselectedPermission($scope.currentRole)
                                            .success(function(data) {
                                                $scope.unselectedPermission = data;
                                                $scope.currentRole = RoleService.get({
                                                    id: $scope.currentRole.id
                                                });
                                            });
                                });
                    }
                }
                $scope.selectAllMenu = function($event) {
                    if ($event.target.checked) {
                        for (var i = 0; i < $scope.unselectedMenu.length; i++) {
                            var p = $scope.unselectedMenu[i];
                            if ($scope.selectedMenu.indexOf(p.id) < 0) {
                                $scope.selectedMenu.push(p.id);
                            }
                        }
                    } else {
                        $scope.selectedMenu = [];
                    }
                }
                $scope.updateSelectedMenu = function($event, id) {
                    var checkbox = $event.target;
                    if (checkbox.checked && $scope.selectedMenu.indexOf(id) < 0) {
                        $scope.selectedMenu.push(id);
                    } else {
                        $scope.selectedMenu.splice($scope.selectedMenu.indexOf(id), 1);
                    }
                }
                $scope.isMenuSelected = function(id) {
                    return $scope.selectedMenu.indexOf(id) >= 0;
                }
                $scope.isAllMenuSelected = function() {
                    return $scope.unselectedMenu.length === $scope.selectedMenu.length;
                }
                $scope.saveSelectedMenu = function() {
                    console.log($scope.selectedMenu);
                    if ($scope.selectedMenu.length < 1)
                        return;
                    for (var i = 0; i < $scope.selectedMenu.length; i++) {
                        var p = {id: $scope.selectedMenu[i]};
                        $scope.currentRole.menuSet.push(p);
                    }
                    RoleService.save($scope.currentRole)
                            .success(function() {
                                RoleService.unselectedMenu($scope.currentRole)
                                        .success(function(data) {
                                            $scope.unselectedMenu = data;
                                            $scope.currentRole = RoleService.get({
                                                id: $scope.currentRole.id
                                            });
                                        });
                            });
                    $('#dialogMenu').modal('hide');
                }
                $scope.cancelSelectedMenu = function() {
                    $scope.selectedMenu = [];
                    console.log($scope.selectedMenu);
                    $('#dialogMenu').modal('hide');
                }
                $scope.removeSelectedMenu = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    var ixMenu = -1;
                    for (var i = 0; i < $scope.currentRole.menuSet.length; i++) {
                        if (x.id === $scope.currentRole.menuSet[i].id) {
                            ixMenu = i;
                            break;
                        }
                    }
                    if (ixMenu >= 0) {
                        $scope.currentRole.menuSet.splice(ixMenu, 1);
                        RoleService.save($scope.currentRole)
                                .success(function() {
                                    RoleService.unselectedMenu($scope.currentRole)
                                            .success(function(data) {
                                                $scope.unselectedMenu = data;
                                                $scope.currentRole = RoleService.get({
                                                    id: $scope.currentRole.id
                                                });
                                            });
                                });
                    }
                }
            }])
        .controller('UserController', ['$http', '$scope', 'UserService', 'RoleService', function($http, $scope, UserService, RoleService) {
                $scope.errorOccured = false;
                $scope.usermsg = {};
                $http.get('homepage/usermsg').success(function(data) {
                    $scope.usermsg = data;
                });
                //$scope.users = UserService.query();
                $scope.roles = RoleService.query();

                $scope.reloadUserpage = function(page) {
                    if (!page || page < 0) {
                        page = 0;
                    }

                    $scope.userpage = UserService.query4Page(page, function() {
                        $scope.pages = _.range(1, ($scope.userpage.totalPages + 1));
                    });
                }

                $scope.reloadUserpage();

                $scope.edit = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.currentUser = UserService.get({id: x.id}, function(data) {
                        $scope.original = angular.copy(data);
                    });
                };
                $scope.prepareDelete = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    $scope.deleteUser = x;
                };
                $scope.add = function() {
                    $scope.errorOccured = false;
                    $scope.currentUser = null;
                    $scope.original = null;
                }
                $scope.save = function() {
                    if ($scope.currentUser.enabled == null) {
                        $scope.currentUser.enabled = false;
                    }
                    var obj = $scope.currentUser;
                    delete obj.uploadError;
                    console.log("Save obj ", obj);
                    UserService.save(obj)
                            .success(function() {
                                $scope.reloadUserpage();
                                $scope.add();                                
                            });
                }
                $scope.uploadComplete = function(content, completed) {
                    if (completed) {
                        $scope.currentUser.uploadError = content.msg + "  [" + content.status + "]";
                        if (content.status == "200") {
                            if (content.msg=='No file uploaded') {
                                $scope.currentUser.photo = 'no_photo';
                            }
                            $scope.save();
                        }
                        else {
                            $scope.errorOccured = true;
                        }
                    }
                }
                $scope.remove = function(x) {
                    if (x.id == null) {
                        return;
                    }
                    UserService.remove(x).success(function() {
                        $scope.reloadUserpage();
                    });
                }
                $scope.isClean = function() {
                    // not really equal if undefined or null
                    return angular.equals($scope.original, $scope.currentUser);
                }
//        $scope.isUsernameAvailable = function(value){
//            if($scope.currentUser != null && $scope.currentUser.id != null){
//                return true;
//            }
//            
//            for(var i = 0; i < $scope.users.length; i++){
//                var u = $scope.users[i];
//                if(u.username === value){                    
//                    return false;
//                }
//            }
//            
//            return true;
//        }
            }])
        .controller('SignupController', ['$scope', 'UserService', function($scope, UserService) {
                $scope.errorOccured = false;
                $scope.currentUser = null;
                //$scope.users = UserService.query();
                $scope.add = function() {
                    $scope.currentUser = null;
                    $scope.original = null;
                }
                $scope.save = function() {
                    $scope.currentUser.id = null;
                    $scope.currentUser.enabled = true;
                    $scope.currentUser.photo = "";
                    $scope.currentUser.email = $scope.currentUser.username;
                    $scope.currentUser.role = {};

                    var obj = $scope.currentUser;
                    console.log("Save obj ", obj);
                    UserService.save(obj)
                            .success(function() {
                                $scope.errorOccured = true;
                                //$scope.users = UserService.query();
                                //$scope.add();
                            });
                }
                $scope.isClean = function() {
                    return angular.equals($scope.original, $scope.currentUser);
                }
            }])
        ;