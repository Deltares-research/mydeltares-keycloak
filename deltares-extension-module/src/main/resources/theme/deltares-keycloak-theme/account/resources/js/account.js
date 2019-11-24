function checkFileSize() {

    var uploadField = document.getElementById("avatar");
    if (!uploadField || !uploadField.files) return;
    var uploadFile = uploadField.files[0];
    var fileSizeKb = uploadFile.size / 1024;
    if (fileSizeKb > avatarMaxSizeKb){
        alert("Selected file size " + fileSizeKb.toFixed(1) + "(KB) is larger than maximum allowed size " + avatarMaxSizeKb + "(KB)!")
        uploadField.files[0]=null;
    }

}

//
// module.service('UserAvatar', function(Auth) {
//     this.url = function(user, realm) {
//         return authUrl + '/realms/' + realm.realm + '/avatar-provider/admin/' + user.id + "?access_token=" + Auth.authz.token + "&" + + new Date().getTime();
//     }
// });

module.controller('UserAvatarCtrl', function($scope, $http, Notifications, UserAvatar) {
    // $scope.avatarUrl = UserAvatar.url($scope.user, $scope.realm);

    $scope.uploadAvatar = function(avatarUrl) {

        $scope.avatarUrl = avatarUrl;
        var uploadField = document.getElementById("avatar");
        if (!uploadField || !uploadField.files) return;
        var uploadFile = uploadField.files[0];

        var fd = new FormData();
        //Take the first selected file
        fd.append("image", uploadFile);

        $http.post($scope.avatarUrl, fd, {
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function() {
            Notifications.success("Your changes have been saved to the user.");
            // $scope.avatarUrl = UserAvatar.url($scope.user, $scope.realm);
            $scope.avatarUrl = avatarUrl;
        }, function(error) {
            console.error(error);
            Notifications.error("Could not save the avatar");
        });
    };


    $scope.deleteAvatar = function(avatarUrl) {
        //Take the first selected file
        $scope.avatarUrl = avatarUrl;

        $http.delete($scope.avatarUrl, {
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function() {
            Notifications.success("Avatar has been deleted.");
            $scope.avatarUrl = avatarUrl;
        }, function(error) {
            console.error(error);
            Notifications.error("Could not delete the avatar");
        });
    }
});


