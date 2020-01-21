
module.service('UserAvatar', function(Auth) {
    this.url = function(user, realm) {
        return authUrl + '/realms/' + realm.realm + '/avatar-provider/admin/' + user.id + "?access_token=" + Auth.authz.token + "&" + + new Date().getTime();
    }
});

module.controller('UserAvatarCtrl', function($scope, $http, Notifications, UserAvatar) {
    $scope.avatarUrl = UserAvatar.url($scope.user, $scope.realm);

    $scope.uploadAvatar = function(files) {
        var fd = new FormData();
        //Take the first selected file
        fd.append("image", files[0]);
    
        $http.post($scope.avatarUrl, fd, {
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function() {
            Notifications.success("Your changes have been saved to the user.");
            $scope.avatarUrl = UserAvatar.url($scope.user, $scope.realm);
        }, function(error) {
            console.error(error);
            Notifications.error("Could not save the avatar");
        });        
    };


    $scope.deleteAvatar = function() {
        //Take the first selected file

        $http.delete($scope.avatarUrl, {
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function() {
            Notifications.success("Avatar has been deleted.");
            $scope.avatarUrl = UserAvatar.url($scope.user, $scope.realm);
        }, function(error) {
            console.error(error);
            Notifications.error("Could not delete the avatar");
        });
    };

    $scope.downloadAvatar = function() {

        $http.get($scope.avatarUrl, {
            responseType: "arraybuffer",
            transformRequest: angular.identity
        }).then(function(response) {
            var headers = response.headers(0);
            var type = headers['content-type'];
            var fileName = parseHeader(headers['content-disposition'], "filename");
            if (!fileName){
                fileName = "avatar.img";
            }
            var downloadArray = new Uint8Array(response.data);
            saveAs(new Blob([downloadArray], { type: type }), fileName);
            $scope.avatarUrl = UserAvatar.url($scope.user, $scope.realm);
        }, function(error) {
            console.error(error);
            Notifications.error("Could not download the avatar");
        });
    }

});

function parseHeader(headerValue, searchKey){
    if (headerValue) {
        var regExpString = searchKey + '[^;=\\n]*=(([\'"]).*?\\2|[^;\\n]*)';
        var regExp = new RegExp(regExpString);
        var matches = regExp.exec(headerValue);
        if (matches != null && matches[1]) {
            return matches[1].replace(/['"]/g, '').trim();
        }

    }
}