function enableAccept() {

    var acceptTerms = document.getElementById("acceptTerms").checked;
    var acceptPrivacy = document.getElementById("acceptPrivacy").checked;
    let acceptButton = document.getElementById("kc-accept");
    if ( acceptTerms && acceptPrivacy ){
        acceptButton.disabled = false;
        acceptButton.classList.remove("btn-secondary")
        acceptButton.classList.add("btn-primary")
    } else {
        acceptButton.disabled = true;
        acceptButton.classList.add("btn-secondary")
        acceptButton.classList.remove("btn-primary")
    }

}
