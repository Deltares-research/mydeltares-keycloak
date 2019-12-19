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

function saveMailings(url) {

    var rows = document.getElementById("mailingsTable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;

    for (var i = 0; i < rows; i++ ){
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
            sendHttpMessage(url + '/' + userMailing.id,"DELETE", userMailing);
        } else if(!hasId && enabled){
            sendHttpMessage(url,"POST", userMailing);
        } else if(hasId && enabled){
            sendHttpMessage(url,"PUT", userMailing);
        }
    }
}

function sendHttpMessage(url, method, data){
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var json = JSON.parse(xhr.responseText);
            console.log(json);
        }
    };
    xhr.send(JSON.stringify(data));
}

function cancel() {
    document.location.reload();
}

// When the user clicks on <div>, open the popup
function fullDescription(popup) {
    popup.childNodes[1].classList.toggle("show")
}


