function enableSave(index){
    document.getElementById("saveMailing").disabled = false;
    document.getElementById("rowState" + index).value = "save";
}
function checkFileSize() {

    var uploadField = document.getElementById("avatar");
    if (!uploadField || !uploadField.files){
        document.getElementById("saveAvatar").disabled = true;
        return;
    }
    var uploadFile = uploadField.files[0];
    var fileSizeKb = uploadFile.size / 1024;
    if (fileSizeKb > avatarMaxSizeKb){
        alert("Selected file size " + fileSizeKb.toFixed(1) + "(KB) is larger than maximum allowed size " + avatarMaxSizeKb + "(KB)!")
        uploadField.files[0]=null;
        document.getElementById("saveAvatar").disabled = true;
        return;
    }

    //file ok. set status save button
    document.getElementById("saveAvatar").disabled = false;
}

function saveMailings(url, urlParams) {

    var rows = document.getElementById("mailingsTable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;

    for (var i = 0; i < rows; i++ ){

        if (document.getElementById("rowState" + i).value !== "save") continue;
        var userMailing = {};
        userMailing.id = document.getElementById("id" + i).value;
        userMailing.mailingId = document.getElementById("mailingId" + i).value;
        var enabled = document.getElementById("enabled" + i).checked;
        var langElement = document.getElementById("language" + i);
        userMailing.language = langElement.options[langElement.selectedIndex].value;
        var delElement = document.getElementById("delivery" + i);
        userMailing.deliveryTxt = delElement.options[delElement.selectedIndex].value;
        var hasId = userMailing.id !== '';
        if (hasId && !enabled){
            sendHttpMessage(url + '/' + userMailing.id + '?' + urlParams,"DELETE", userMailing);
        } else if(!hasId && enabled){
            sendHttpMessage(url + '?' + urlParams,"POST", userMailing);
        } else if(hasId && enabled){
            sendHttpMessage(url + '?' + urlParams,"PUT", userMailing);
        }
    }
}

function sendHttpMessage(url, method, data){
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, false);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var json = JSON.parse(xhr.responseText);
            console.log(json);
        } else {
            console.error();
        }
    };
    xhr.send(JSON.stringify(data));
}

function cancel() {
    document.location.reload();
}

function addReferrerCookie(referrerName, referrerUrl) {
    document.cookie = 'referrer=' + referrerName;
    document.cookie = 'referrer_uri=' + referrerUrl;
}

function loadReferrer() {

    var referrer_name = getCookie("referrer");
    var elementById = document.getElementById('backPage');
    if (referrer_name){
        var text = elementById.textContent;
        elementById.textContent = text + ' ' + referrer_name;
    }
    var referrer_uri = getCookie("referrer_uri");
    if (referrer_uri){
        elementById.href=referrer_uri;
    }

}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

