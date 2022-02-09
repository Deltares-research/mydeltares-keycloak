<#macro mainLayout active bodyClass>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <title>${msg("accountManagementTitle")}</title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico">
    <#if properties.stylesCommon?has_content>
        <#list properties.stylesCommon?split(' ') as style>
            <link href="${url.resourcesCommonPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
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

    <#if referrer?? >
        <script>
            addReferrerCookie("${referrer.name}", "${referrer.url}");
        </script>
    </#if>
</head>
<body class="admin-console user ${bodyClass}" onload="loadReferrer()">
        
    <header class="navbar navbar-default navbar-pf navbar-main header">
        <nav class="navbar" role="navigation">
            <div class="navbar-header">
                <div class="container">
                    <h1 class="navbar-title">
                        My<svg xmlns="http://www.w3.org/2000/svg" width="200px" height="40px" viewBox="0 0 200 40"><path
                                    d="M32.70053,19.40642C32.70053,6.23,25.28877.81818,13.28877.81818c-3.76471,0-7.41176,0-9.29412.11765S.93583,1.28877.93583,3.17112v36a104.5266,104.5266,0,0,0,10.70588.58824C23.877,39.75936,32.70053,32.58289,32.70053,19.40642Zm-7.88235.58823c0,8.47059-5.17647,13.52941-12.47059,13.52941a25.14218,25.14218,0,0,1-4-.29411V7.1123s2.11765-.17647,4.47059-.17647C21.28877,6.93583,24.81818,11.52406,24.81818,19.99465Zm11.88235,5.29412c0,9.64706,4.70589,14.47059,14.35294,14.47059A25.57074,25.57074,0,0,0,59.99465,38.23V32.70053a25.64113,25.64113,0,0,1-8,1.41177c-5.17647,0-8-2.70588-8-7.88235H58.46524c2.35294,0,3.29412-.58824,3.29412-2.2353V22.93583c0-7.05882-3.76471-12.58824-11.76471-12.58824-8.47059,0-13.29412,6.47059-13.29412,14.94118Zm13.05883-9.52941c3.05882,0,4.70588,2.11764,4.70588,5.64706H44.1123c.47059-3.52942,2.58823-5.64706,5.64706-5.64706ZM74.46524.93583h-4c-2.35294,0-3.29412.58823-3.29412,2.23529V30.46524c0,6.58823,2,9.29412,7.41177,9.29412a16.70005,16.70005,0,0,0,4.82353-.76471V33.64171a10.98823,10.98823,0,0,1-2.47059.35294c-1.47059,0-2.47059-.70588-2.47059-3.7647Zm16.70588,0H87.28877c-2.35294,0-3.29412.58823-3.29412,2.23529V30.46524c0,6.58823,2.11765,9.29426,8.2353,9.29426a21.5292,21.5292,0,0,0,5.7647-.76485V33.40642a15.40581,15.40581,0,0,1-3.41176.47059c-2.11765,0-3.41177-1.05883-3.41177-4.58824V16.58289h3.52941c2.35294,0,3.29412-.58824,3.29412-2.2353V10.93583H91.17112Zm10.35294,25.52941c0,9.64706,6.11765,13.29412,13.64706,13.29412a35.36986,35.36986,0,0,0,12.35294-2.11765V11.877a36.59976,36.59976,0,0,0-10.11764-1.52942C107.52406,10.34759,101.52406,16.81818,101.52406,26.46524Zm7.52941-.70588c0-6.82353,3.41177-9.76471,7.64707-9.76471a15.47072,15.47072,0,0,1,3.64706.41177v17a11.90114,11.90114,0,0,1-4.58824.70588c-4.70589,0-6.70589-3.17647-6.70589-8.35294Zm25.88235,13.41176H142.23V21.99465c0-4,1.05882-5.41176,3.88235-5.41176h.35294c2.11765,0,3.17647-.35294,3.17647-2.2353V10.93583h-4.47058c-8,0-10.23531,4.94118-10.23531,11.05882Zm16.35295-13.88235c0,9.64706,4.70589,14.47059,14.35294,14.47059A25.57074,25.57074,0,0,0,174.58289,38.23V32.70053a25.64118,25.64118,0,0,1-8,1.41177c-5.17647,0-8-2.70588-8-7.88235h14.47059c2.35294,0,3.29412-.58824,3.29412-2.2353V22.93583c0-7.05882-3.76471-12.58824-11.76471-12.58824C156.1123,10.34759,151.28877,16.81818,151.28877,25.28877Zm13.05883-9.52941c3.05882,0,4.70588,2.11764,4.70588,5.64706h-10.353c.4706-3.52942,2.58824-5.64706,5.64707-5.64706Zm16.47058,3.64706c0,9.41176,11.47059,6.82353,11.47059,11.7647,0,1.88235-1.82353,2.94118-4.64706,2.94118a23.5708,23.5708,0,0,1-6.05882-.82353l-.11765,5.88235a28.65,28.65,0,0,0,5.82353.58828c7.52941,0,12.52941-3.6471,12.52941-9.29416,0-9.17647-11.58823-7.29412-11.58823-11.76471,0-1.88235,1.64705-2.82352,4.23529-2.82352.94118,0,1.88236.05882,2.58824.05882,1.647,0,2.94118-.29412,2.94118-3.11765v-2a33.178,33.178,0,0,0-5.17648-.47059C184.81818,10.34759,180.81818,14.46524,180.81818,19.40642Z"
                                    style="fill:#080C80"></path></svg>
                    </h1>
<#--                    <br><a href="#" id="backPage">${msg("back")}</a>&ndash;&gt;-->
                </div>
            </div>
            <div class="navbar-collapse navbar-collapse-1">
                <div class="container">
                    <div class="sign-out"><a href="${url.logoutUrl}">${msg("doSignOut")}</a></div>
                    <div class="referrer"><a id="backPage">${msg("back")}</a></div>

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

    <div class="container">
        <div class="bs-sidebar col-sm-3">
            <ul>
                <li class="<#if active=='account'>active</#if>"><a href="${url.accountUrl}">${msg("account")}</a></li>
                <#if features.passwordUpdateSupported><li class="<#if active=='password'>active</#if>"><a href="${url.passwordUrl}">${msg("password")}</a></li></#if>
<!--                <li class="<#if active=='totp'>active</#if>"><a href="${url.totpUrl}">${msg("authenticator")}</a></li> -->
                <#if features.identityFederation><li class="<#if active=='social'>active</#if>"><a href="${url.socialUrl}">${msg("federatedIdentity")}</a></li></#if>
                <li class="<#if active=='sessions'>active</#if>"><a href="${url.sessionsUrl}">${msg("sessions")}</a></li>
                <li class="<#if active=='mailings'>active</#if>"><a href="${url.mailingsUrl}">${msg("mailings")}</a></li>
                <li class="<#if active=='applications'>active</#if>"><a href="${url.applicationsUrl}">${msg("applications")}</a></li>
                <#if features.log><li class="<#if active=='log'>active</#if>"><a href="${url.logUrl}">${msg("log")}</a></li></#if>
                <#if realm.userManagedAccessAllowed && features.authorization><li class="<#if active=='authorization'>active</#if>"><a href="${url.resourceUrl}">${msg("myResources")}</a></li></#if>
            </ul>
        </div>

        <div class="col-sm-9 content-area">
            <#if message?has_content>
                <div class="alert alert-${message.type}">
                    <#if message.type=='success' ><span class="pficon pficon-ok"></span></#if>
                    <#if message.type=='error' ><span class="pficon pficon-error-circle-o"></span></#if>
                    <span class="kc-feedback-text">${kcSanitize(message.summary)?no_esc}</span>
                </div>
            </#if>

            <#nested "content">
        </div>
    </div>

<#--    <footer class="footer-pos">-->
<#--        <div class="footer">-->
<#--            <div class="inner-footer">-->
<#--                <div class="misc-column">-->
<#--                    <div>Colofon: <a target="_blank" href="https://oss.deltares.nl/nl/colofon">NL</a> <a target="_blank" href="https://oss.deltares.nl/en/colofon">EN</a></div>-->
<#--                    <div>Disclaimer: <a target="_blank" href="https://oss.deltares.nl/nl/disclaimer">NL</a>  <a target="_blank" href="https://oss.deltares.nl/en/disclaimer">EN</a></div>-->
<#--                    <div>Privacy statement: <a target="_blank" href="https://oss.deltares.nl/nl/privacy-declaration">NL</a> <a target="_blank" href="https://oss.deltares.nl/nl/privacy-declaration">EN</a></div>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </footer>-->



</body>
</html>
</#macro>