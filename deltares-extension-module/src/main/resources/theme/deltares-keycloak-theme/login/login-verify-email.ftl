<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        ${msg("emailVerifyTitle")}
    <#elseif section = "form">
        <p class="instruction">
            ${msg("emailVerifyInstruction1")}
        </p>
        <p class="instruction">
            ${msg("emailVerifyInstruction2")} <p>${msg("emailVerifyInstruction4")}<a href="${url.loginAction}">${msg("doClickHere")}</a> ${msg("emailVerifyInstruction3")} </p>
        </p>
        
	${msg("pageExpiredMsg1")} <a id="loginRestartLink" href="${url.loginRestartFlowUrl}">${msg("doClickHere")}</a> .<br/>
	
    </#if>
</@layout.registrationLayout>