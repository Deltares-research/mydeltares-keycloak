<html>
<body>
${kcSanitize(msg("emailVerificationBodyHtml",link, linkExpiration, realmName, linkExpirationFormatter(linkExpiration), user.lastName))?no_esc}
</body>
</html>
