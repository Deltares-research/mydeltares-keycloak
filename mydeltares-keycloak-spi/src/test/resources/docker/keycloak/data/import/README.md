When replacing the realm-export.json you must perform some manual steps

<ul>
<li>Remove the section <strong>authorizationSettings</strong> otherwise the realm-export cannot be imported</li>
<li>Reset the passwords for the clients. These are always anonymized on export</li>
</ul>