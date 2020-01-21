module.controller('MailingTabCtrl', function($scope, $location, Dialog, Notifications, Current) {
    $scope.removeMailing = function() {
        Dialog.confirmDelete($scope.mailing.id, 'mailing', function() {
            $scope.mailing.$remove({
                realm : Current.realm.realm,
                mailingId : $scope.mailing.id
            }, function() {
                $location.url("/realms/" + Current.realm.realm + "/mailings");
                Notifications.success("The mailing has been deleted.");
            }, function() {
                Notifications.error("Mailing couldn't be deleted");
            });
        });
    };
});

module.controller('MailingListCtrl', function($scope, realm, Mailing, MailingSearchState, BruteForce, Notifications, $route, $http, Dialog) {

    $scope.init = function() {
        $scope.realm = realm;

        MailingSearchState.query.realm = realm.realm;
        $scope.query = MailingSearchState.query;
        $scope.query.briefRepresentation = 'true';

        if (!MailingSearchState.isFirstSearch) $scope.searchQuery();
    };

    $scope.firstPage = function() {
        $scope.query.first = 0;
        $scope.searchQuery();
    };

    $scope.previousPage = function() {
        $scope.query.first -= parseInt($scope.query.max);
        if ($scope.query.first < 0) {
            $scope.query.first = 0;
        }
        $scope.searchQuery();
    };

    $scope.nextPage = function() {
        $scope.query.first += parseInt($scope.query.max);
        $scope.searchQuery();
    };

    $scope.searchQuery = function() {
        console.log("query.search: " + $scope.query.search);
        $scope.searchLoaded = false;

        $scope.mailings = Mailing.query($scope.query, function() {
            $scope.searchLoaded = true;
            $scope.lastSearch = $scope.query.search;
            MailingSearchState.isFirstSearch = false;
        });
    };

    $scope.removeMailing = function(mailing) {
        Dialog.confirmDelete(mailing.id, 'mailing', function() {
            mailing.$remove({
                realm : realm.realm,
                mailingId : mailing.id
            }, function() {
                $route.reload();

                if ($scope.mailings.length === 1 && $scope.query.first > 0) {
                    $scope.previousPage();
                }

                Notifications.success("The mailing has been deleted.");
            }, function() {
                Notifications.error("Mailing couldn't be deleted");
            });
        });
    };

    $scope.exportUserMailings = function(mailing) {
        var exportUrl = authUrl + '/realms/' + realm.realm + '/mailing-provider/admin/export/usermailings/' + mailing.id;
        var fileName = mailing.name + '-export.csv';
        $http.get(exportUrl)
            .then(function(response) {
                var download = response.data;
                saveAs(new Blob([download], { type: 'text/csv' }), fileName);
            }).catch(function(err) {
            Notifications.error("Sorry, something went wrong: "  + err);
        });
    };

});

module.controller('MailingDetailCtrl', function($scope, realm, mailing, BruteForceUser, Mailing,
                                                Components, $location, $http, Dialog, Notifications){
    $scope.realm = realm;
    $scope.create = !mailing.id;

    if ($scope.create) {
        $scope.mailing = {
            supportedDeliveries: ["email", "post", "both"],
            supportedFrequencies: ["weekly", "monthly", "quarterly", "annually", "other"]
        }
    } else {
        // convertAttributeValuesToString(user);
        $scope.mailing = angular.copy(mailing);
    }
    $scope.changed = false; // $scope.create;

    $scope.$watch('mailing', function() {
        if (!angular.equals($scope.mailing, mailing)) {
            $scope.changed = true;
        }
    }, true);

    $scope.save = function() {
        // convertAttributeValuesToLists();

        if ($scope.create) {
            Mailing.save({
                realm: realm.realm
            }, $scope.mailing, function (data, headers) {
                $scope.changed = false;
                // convertAttributeValuesToString($scope.user);
                mailing = angular.copy($scope.mailing);
                var l = headers().location;

                console.debug("Location == " + l);

                var id = l.substring(l.lastIndexOf("/") + 1);


                $location.url("/load/mailing/" + realm.realm + "/" + id);
                Notifications.success("The mailing has been created.");
            });
        } else {
            Mailing.update({
                realm: realm.realm
            }, $scope.mailing, function () {
                $scope.changed = false;
                // convertAttributeValuesToString($scope.user);
                mailing = angular.copy($scope.mailing);
                Notifications.success("Your changes have been saved to the mailing.");
            });
        }
    };

    // function convertAttributeValuesToLists() {
    //     var attrs = $scope.user.attributes;
    //     for (var attribute in attrs) {
    //         if (typeof attrs[attribute] === "string") {
    //             var attrVals = attrs[attribute].split("##");
    //             attrs[attribute] = attrVals;
    //         }
    //     }
    // }

    // function convertAttributeValuesToString(user) {
    //     var attrs = user.attributes;
    //     for (var attribute in attrs) {
    //         if (typeof attrs[attribute] === "object") {
    //             var attrVals = attrs[attribute].join("##");
    //             attrs[attribute] = attrVals;
    //         }
    //     }
    // }

    $scope.reset = function() {
        $scope.mailing = angular.copy(mailing);
        $scope.changed = false;
    };

    $scope.cancel = function() {
        $location.url("/realms/" + realm.realm + "/mailings");
    };

});