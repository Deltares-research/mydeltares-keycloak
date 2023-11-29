<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        ${msg("termsTitle")}
    <#elseif section = "form">
        <div id="kc-terms-text">
            ${kcSanitize(msg("termsText"))?no_esc}
        </div>
        <form class="${properties.kcFormClass!}" action="${url.loginAction}" method="POST">
            <div class="checkbox">
                <input type="checkbox" id="acceptTerms"  name="acceptTerms" onchange="enableAccept()">
                ${kcSanitize(msg("acceptTerms"))?no_esc}
                <span class="required">*</span>
            </div>
            <div class="checkbox">
                <input type="checkbox" id="acceptPrivacy"  name="acceptPrivacy" onchange="enableAccept()">
                ${kcSanitize(msg("acceptPrivacy"))?no_esc}
                <span class="required">*</span>
            </div>
            <br />
            <input disabled class="${properties.kcButtonClass!} ${properties.kcButtonSecondaryClass!} ${properties.kcButtonLargeClass!}" name="accept" id="kc-accept" type="submit" value="${msg("doAccept")}"/>
            <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="cancel" id="kc-decline" type="submit" value="${msg("doDecline")}"/>
            <div class="subtitle">
                <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
            </div>
        </form>
        <div class="clearfix"></div>
    </#if>
</@layout.registrationLayout>
