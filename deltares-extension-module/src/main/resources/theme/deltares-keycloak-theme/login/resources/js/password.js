const format = /[ `!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;

function validateCredentials(event, minLength, minDigits, minUppercase, minLowercase, minSpecial) {

    let passwordField = document.getElementById("password-new");
    let password = passwordField.value;

    let rules = "Password does not meet complexity rules:</br><ul>"
    if (minLength > 0){
        rules = rules.concat("<li>Minimum length = " + minLength + "</li>");
    }
    if (minDigits > 0) {
        rules = rules.concat("<li>Minimum number of digits = " + minDigits + "</li>");
    }
    if (minUppercase > 0) {
        rules = rules.concat("<li>Minimum number of uppercase characters = " + minUppercase + "</li>");
    }
    if (minLowercase > 0) {
        rules = rules.concat("<li>Minimum number of lowercase characters = " + minLowercase + "</li>");
    }
    if (minSpecial > 0) {
        rules = rules.concat("<li>Minimum number of special characters = " + minSpecial + "</li>");
    }
    rules.concat("</ul>")

    let special = 0;
    let digits = 0;
    let upperCase = 0;
    let lowerCase = 0;

    for (let i = 0; i < password.length; i++) {
        let c = password[i];

        if (!isNaN(c*1)) {
            digits++;
            continue;
        }
        if (format.test(c)){
            special++;
            continue;
        }
        if (c === c.toUpperCase()) {
            upperCase++;
            continue;
        }
        if (c === c.toLowerCase()) {
            lowerCase++;
            // continue;
        }
    }


    if (password.length < minLength ||  special < minSpecial || upperCase < minUppercase || lowerCase < minLowercase || digits < minDigits){
        event.preventDefault();
        passwordField.setAttribute("aria-invalid", "true");
        let errorField = document.getElementById("input-error-password-policy");
        errorField.innerHTML = rules;
        return false;
    }
    return true;
}