module.controller('AttributeListCtrl', function($scope, realm, BruteForce, Notifications, $route, $http) {

    $scope.init = function() {
        $scope.realm = realm;
        $scope.query.search = "";
    };

    $scope.exportUserAttributes = function() {
        var exportUrl = authUrl + '/realms/' + realm.realm + '/user-attributes/admin/export?search=' + $scope.query.search;

        $http.get(exportUrl)
            .then(response => {
                var fileName = 'attributes-export.csv';
                let mimeType = response.headers("Content-Type");
                if (mimeType.includes("text/plain") ){
                    Notifications.info(response.data);
                } else if (mimeType.includes("text/csv") ){
                    saveAs(new Blob([response.data], { type: 'application/octet-stream' }), fileName);
                }

            }).catch(function(err) {
            Notifications.error("Sorry, something went wrong: "  + err);
        });
    };

});
