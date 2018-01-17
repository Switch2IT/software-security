/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var module = angular.module('product', []);

var auth = {};

var logout = function () {
    console.log('*** LOGOUT');
    auth.loggedIn = false;
    auth.authz = null;
    window.location = auth.logoutUrl;
};

var login = function () {
    console.log("Logging in");
    var keycloakAuth = new Keycloak('keycloak.json');
    auth.loggedIn = false;

    keycloakAuth.init({onLoad: 'login-required'}).success(function () {
        auth.loggedIn = true;
        auth.authz = keycloakAuth;
        auth.logoutUrl = keycloakAuth.authServerUrl + "/realms/" + keycloakAuth.realm + "/protocol/openid-connect/logout?redirect_uri=" + window.location.href;
        module.factory('Auth', function () {
            return auth;
        });
        angular.bootstrap(document, ["product"]);
    }).error(function () {
        window.location.reload();
    });
}

angular.element(document).ready(function () {
    var keycloakAuth = new Keycloak('keycloak.json');
    auth.loggedIn = false;

    keycloakAuth.init({onLoad: 'check-sso'}).success(function () {
        auth.loggedIn = true;
        auth.authz = keycloakAuth;
        auth.logoutUrl = keycloakAuth.authServerUrl + "/realms/" + keycloakAuth.realm + "/protocol/openid-connect/logout?redirect_uri=" + window.location.href;
        module.factory('Auth', function () {
            return auth;
        });
        angular.bootstrap(document, ["product"]);
    }).error(function () {
        window.location.reload();
    });
});

module.controller('GlobalCtrl', function ($scope, $http, Auth) {
    $scope.responseContent = "";
    $scope.token = Auth.authz.token;
    $scope.showPrivateEndpointResponse = function () {
        $http.get("/software-security/v1/api/private").success(function (data) {
            $scope.textColor = {'color': 'green'};
            $scope.responseContent = data.responseContent;
        }).error(function (data) {
            $scope.textColor = {'color': 'red'};
            $scope.responseContent = data.responseContent;
        });
    };
    $scope.showPublicEndpointResponse = function () {
        $http.get("/software-security/v1/api/public").success(function (data) {
            $scope.textColor = {'color': 'green'};
            $scope.responseContent = data.responseContent;
        }).error(function (data) {
            $scope.textColor = {'color': 'red'};
            $scope.responseContent = data.responseContent;
        });
    };
    $scope.logout = logout;
    $scope.login = login;
});


module.factory('authInterceptor', function ($q, Auth) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            config.headers.Accept = "application/json";
            var deferred = $q.defer();
            if (Auth.authz && Auth.authz.token && config.url.endsWith("private")) {
                Auth.authz.updateToken(5).success(function () {
                    config.headers.Authorization = 'Bearer ' + Auth.authz.token;
                    deferred.resolve(config);
                }).error(function () {
                    deferred.reject('Failed to refresh token');
                });
            } else {
                deferred.resolve(config);
            }
            return deferred.promise;
        }
    };
});


module.config(function ($httpProvider) {
    $httpProvider.responseInterceptors.push('errorInterceptor');
    $httpProvider.interceptors.push('authInterceptor');

});

module.factory('errorInterceptor', function ($q) {
    return function (promise) {
        return promise.then(function (response) {
            return response;
        }, function (response) {
            if (response.status === 401) {
                auth.loggedIn = false;
                auth.authz = null;
            } else if (response.status === 403) {
                alert("Forbidden");
            } else if (response.status === 404) {
                alert("Not found");
            } else if (response.status) {
                if (response.data && response.data.errorMessage) {
                    alert(response.data.errorMessage);
                } else {
                    alert("An unexpected server error has occurred");
                }
            }
            return $q.reject(response);
        });
    };
});
