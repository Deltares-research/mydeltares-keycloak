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
                <input id="rowState${mailing?index}" type="hidden" value="">
                <td style="display: none">
                    <input id="id${mailing?index}" type="text" value="${mailing.id}">
                </td>
                <td style="display: none">
                    <input id="mailingId${mailing?index}" type="text" value="${mailing.mailingId}">
                </td>
                <td>
                    <label class="switch" >
                        <#if mailing.enabled>
                            <input id="enabled${mailing?index}" type="checkbox" onchange="enableSave(${mailing?index})" checked>
                        <#else>
                            <input id="enabled${mailing?index}" type="checkbox" onchange="enableSave(${mailing?index})">
                        </#if>
                        <span class="slider round"></span>
                    </label>
                </td>
                <td>${mailing.name}</td>
                <td>
                    <select id="language${mailing?index}" onchange="enableSave(${mailing?index})">
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
                    <select id="delivery${mailing?index}" onchange="enableSave(${mailing?index})">
                        <#list mailing.supportedDeliveries as delivery>
                            <#if mailing.delivery == delivery >
                                <option selected value="${delivery}">${delivery}</option>
                            <#else>
                                <option value="${delivery}">${delivery}</option>
                            </#if>
                        </#list>
                    </select>
                </td>
                <td>${mailing.description}</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <#assign userMailingUrl = url.userMailingsUrl />
    <#assign urlParams = url.pathParams >
    <form>
        <div class="form-group">
            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">
                <div class="">
                    <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}"
                            disabled="disabled" id="saveMailing" onclick="saveMailings('${userMailingUrl}','${urlParams}')"> ${msg("doSave")}
                    </button>
                    <button class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" onclick="cancel()"> ${msg("doCancel")}</button>
                </div>
            </div>
        </div>
    </form>
</@layout.mainLayout>
