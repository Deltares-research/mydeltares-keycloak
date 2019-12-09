function validateForm() {

    var email = document.getElementById("username").value;
    var msg = document.getElementById("validateId");

    if (email && email.toLocaleLowerCase().endsWith("@deltares.nl")){
        msg.style.display = "block"
        return false;
    }
    return true;

}