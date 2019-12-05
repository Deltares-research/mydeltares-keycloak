<#macro mainLayout active bodyClass>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <title>${msg("accountManagementTitle")}</title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico">
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script type="text/javascript" src="${url.resourcesPath}/${script}"></script>
        </#list>
    </#if>
    <#if properties.avatar_max_kb?has_content>
        <script>
            var avatarMaxSizeKb =  parseInt(${properties.avatar_max_kb});
        </script>
    </#if>
</head>
<body class="admin-console user ${bodyClass}">
        
    <header class="navbar navbar-default navbar-pf navbar-main header">
        <nav class="navbar" role="navigation">
            <div class="navbar-header">
                <div class="container">
                     <h1 class="navbar-title"><img src="${url.resourcesPath}/img/wit_deltares_logo.png" style="float:left" /> MyDeltares</h1></span></div>
                </div>
            </div>
                  <div class="navbar-collapse navbar-collapse-1">
                      <div class="container">
                          <ul class="nav navbar-nav navbar-utility">
                              <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
				  <div id="kc-locale">
				      <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
					  <div class="kc-dropdown" id="kc-locale-dropdown">
					      <a href="#" id="kc-current-locale-link">${locale.current}</a>
					      <ul>
						  <#list locale.supported as l>
						      <li class="kc-dropdown-item"><a href="${l.url}">${l.label}</a></li>
						  </#list>
					      </ul>
					  </div>
				      </div>
				  </div>
        			</#if>
                          </ul>
                      </div>
            </div>
        </nav>
    </header>

    <div class="container-content">
        <div class="sub-bar">
            <ul>
                <li class="<#if active=='account'>active</#if>"><a href="${url.accountUrl}">${msg("account")}</a></li>
                <#if features.passwordUpdateSupported><li class="<#if active=='password'>active</#if>"><a href="${url.passwordUrl}">${msg("password")}</a></li></#if>
<!--                <li class="<#if active=='totp'>active</#if>"><a href="${url.totpUrl}">${msg("authenticator")}</a></li> -->
                <li class="<#if active=='sessions'>active</#if>"><a href="${url.sessionsUrl}">${msg("sessions")}</a></li>
                <li class="<#if active=='mailings'>active</#if>">
                    <#assign mailingUrl = url.accountUrl?replace("^(.*)(/account/?)(\\?(.*))?$", "$1/user-mailings/mailings-page?account&$4", 'r') />
                    <a href="${mailingUrl}">${msg("mailings")}</a>
                </li>

<!--                <#if features.identityFederation><li class="<#if active=='social'>active</#if>"><a href="${url.socialUrl}">${msg("federatedIdentity")}</a></li></#if>

                <li class="<#if active=='applications'>active</#if>"><a href="${url.applicationsUrl}">${msg("applications")}</a></li>
                <#if features.log><li class="<#if active=='log'>active</#if>"><a href="${url.logUrl}">${msg("log")}</a></li></#if>
                <#if realm.userManagedAccessAllowed && features.authorization><li class="<#if active=='authorization'>active</#if>"><a href="${url.resourceUrl}">${msg("myResources")}</a></li></#if> -->
            </ul>
        </div>

        <div class="content-area">
            <#if message?has_content>
                <div class="alert alert-${message.type}">
                    <#if message.type=='success' ><span class="pficon pficon-ok"></span></#if>
                    <#if message.type=='error' ><span class="pficon pficon-error-octagon"></span><span class="pficon pficon-error-exclamation"></span></#if>
                    ${kcSanitize(message.summary)?no_esc}
                </div>
            </#if>

            <#nested "content">
        </div>
    </div>

<footer class="footer-pos">
<div class="footer">
    <div class="inner-footer">
        <div class="misc-column">
            <div>Colofon: <a target="_blank" href="https://www.deltares.nl/nl/colofon/">NL</a> <a target="_blank" href="https://www.deltares.nl/en/colofon-2/">EN</a></div>
            <div>Disclaimer: <a target="_blank" href="https://www.deltares.nl/nl/disclaimer-2/">NL</a>  <a target="_blank" href="https://www.deltares.nl/en/disclaimer/">EN</a></div>
            <div>Privacy statement: <a target="_blank" href="https://www.deltares.nl/nl/privacy-verklaring-stichting-deltares/">NL</a> <a target="_blank" href="https://www.deltares.nl/en/privacy-statement-stichting-deltares/">EN</a></div>
        </div>
    </div>
</div>
</footer>

</body>
</html>
</#macro>