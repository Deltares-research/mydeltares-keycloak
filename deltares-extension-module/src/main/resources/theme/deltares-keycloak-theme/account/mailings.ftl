<#import "template.ftl" as layout>
<@layout.mainLayout active='mailings' bodyClass='mailings'; section>

    <div class="row">
        <div class="col-md-10">
            <h2>${msg("mailingsHtmlTitle")}</h2>
        </div>
    </div>
    <input type="hidden" name="stateChecker" value="${stateChecker}">
    <table class="table table-striped table-bordered" id="mailingsTable" >
        <thead>
        <tr>
            <td>${msg("mailing-enabled")}</td>
            <td>${msg("mailing-name")}</td>
            <td>${msg("mailing-language")}</td>
            <td>${msg("mailing-frequency")}</td>
            <td>${msg("mailing-delivery")}</td>
            <td>${msg("mailing-description")}</td>
        </tr>
        </thead>
        <tbody>
        <#list mailings.mailings as mailing>
            <tr>
                <td style="display: none">
                    <input id="id${mailing?index}" type="text" value="${mailing.id}">
                </td>
                <td style="display: none">
                    <input id="mailingId${mailing?index}" type="text" value="${mailing.mailingId}">
                </td>
                <td>
                    <label class="switch">
                        <#if mailing.enabled>
                            <input id="enabled${mailing?index}" type="checkbox" checked>
                        <#else>
                            <input id="enabled${mailing?index}" type="checkbox" >
                        </#if>
                        <span class="slider round"></span>
                    </label>
                </td>
                <td>${mailing.name}</td>
                <td>
                    <select id="language${mailing?index}" >
                        <#list mailing.supportedLanguages as language>
                            <#if mailing.language == language >
                                <option selected value="${language}">${language}</option>
                            <#else>
                                <option value="${language}">${language}</option>
                            </#if>
                        </#list>
                    </select>
                </td>
                <td>${mailing.frequency}</td>
                <td>
                    <select id="delivery${mailing?index}" >
                        <#list mailing.supportedDeliveries as delivery>
                            <#if mailing.delivery == delivery >
                                <option selected value="${delivery}">${delivery}</option>
                            <#else>
                                <option value="${delivery}">${delivery}</option>
                            </#if>
                        </#list>
                    </select>
                </td>
                <td>
                    <#assign desc = mailing.description/>
                    <div class="popup" onclick="fullDescription(this)" >
                        <#if desc?length < 10>
                            ${desc}
                        <#else >
                            ${desc[0..<10]}...
                        </#if>
                        <span class="popuptext">${desc}</span>
                    </div>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
    <#assign  userMailingUrl = url.accountUrl?replace("^(.*)(/account/?)(\\?(.*))?$", "$1/user-mailings", 'r') >
    <form>
        <div class="form-group">
            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">
                <div class="">
                    <#if url.referrerURI??><a href="${url.referrerURI}">${kcSanitize(msg("backToApplication")?no_esc)}</a></#if>
                    <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" onclick="saveMailings('${userMailingUrl}')"> ${msg("doSave")}</button>
                    <button class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" onclick="cancel()"> ${msg("doCancel")}</button>
                </div>
            </div>
        </div>
    </form>

<#--    <form action="${userMailingUrl}" class="form-horizontal" method="post">-->

<#--        <table class="table table-striped table-bordered" id="mailingsTable" >-->
<#--            <thead>-->
<#--            <tr>-->
<#--                <td>${msg("mailing-enabled")}</td>-->
<#--                <td>${msg("mailing-name")}</td>-->
<#--                <td>${msg("mailing-language")}</td>-->
<#--                <td>${msg("mailing-delivery")}</td>-->
<#--                <td>${msg("mailing-description")}</td>-->
<#--            </tr>-->
<#--            </thead>-->
<#--            <tbody>-->
<#--            <#list mailings.mailings as mailing>-->
<#--                <tr>-->
<#--                    <td style="display: none">-->
<#--                        <input type="text" class="form-control" id="fid${mailing?index}" name="id" value="${(mailing.id!'')}"/>-->
<#--                    </td>-->
<#--                    <td style="display: none">-->
<#--                        <input type="text" class="form-control" id="fmailingId${mailing?index}" name="mailingId" value="${mailing.mailingId}"/>-->
<#--                    </td>-->
<#--                    <td>-->
<#--                        <label class="switch">-->
<#--                            <#if mailing.enabled>-->
<#--                                <input id="fenabled${mailing?index}" type="checkbox" checked value="${mailing.enabled?c}">-->
<#--                            <#else>-->
<#--                                <input id="fenabled${mailing?index}" type="checkbox" value="${mailing.enabled?c}" >-->
<#--                            </#if>-->
<#--                            <span class="slider round"></span>-->
<#--                        </label>-->
<#--                    </td>-->
<#--                    <td>${mailing.name}</td>-->
<#--                    <td>-->
<#--                        <select id="flanguage${mailing?index}" >-->
<#--                            <#list mailing.supportedLanguages as language>-->
<#--                                <#if mailing.language == language >-->
<#--                                    <option selected value="${language}">${language}</option>-->
<#--                                <#else>-->
<#--                                    <option value="${language}">${language}</option>-->
<#--                                </#if>-->
<#--                            </#list>-->
<#--                        </select>-->
<#--                    </td>-->
<#--                    <td>${mailing.delivery}</td>-->
<#--                    <td>>-->
<#--                        <#assign desc = mailing.description/>-->
<#--                        <div class="popup" onclick="fullDescription(this)" >-->
<#--                            <#if desc?length < 10>-->
<#--                                ${desc}-->
<#--                            <#else >-->
<#--                                ${desc[0..<10]}...-->
<#--                            </#if>-->
<#--                            <span class="popuptext" >${desc}</span>-->
<#--                        </div>-->
<#--                    </td>-->
<#--                </tr>-->
<#--            </#list>-->
<#--            </tbody>-->
<#--        </table>-->

<#--        <div class="form-group">-->
<#--            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">-->
<#--                <div class="">-->
<#--                    <#if url.referrerURI??><a href="${url.referrerURI}">${kcSanitize(msg("backToApplication")?no_esc)}</a></#if>-->
<#--                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save">${msg("doSave")}</button>-->
<#--                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Cancel">${msg("doCancel")}</button>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </form>-->
</@layout.mainLayout>