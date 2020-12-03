<#import "template.ftl" as layout>
<@layout.mainLayout active='mailings' bodyClass='mailings'; section>

    <div class="row">
        <div class="col-md-10">
            <h2>${msg("unsubscribeHtmlTitle")}</h2>
        </div>
    </div>
    <#if status?has_content && status?boolean>
        ${kcSanitize(msg("unsubscribeSuccess", mailingName))?no_esc}
    <#else>
        ${kcSanitize(msg("unsubscribeNotFound", mailingName))?no_esc}
    </#if>
    ${kcSanitize(msg("unsubscribeManageAccount",url.accountUrl))?no_esc}

    ${kcSanitize(msg("unsubscribeContact"))?no_esc}
</@layout.mainLayout>
