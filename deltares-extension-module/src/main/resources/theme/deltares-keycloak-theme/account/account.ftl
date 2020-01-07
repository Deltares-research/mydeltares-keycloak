<#import "template.ftl" as layout>
<@layout.mainLayout active='account' bodyClass='user'; section>

    <div class="row">
        <div class="col-md-10">
            <h2>${msg("editAccountHtmlTitle")}</h2>
        </div>
        <div class="col-md-2 subtitle">
            <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
        </div>
    </div>

    <form action="${url.accountUrl}" class="form-horizontal" method="post">

        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">

        <#if !realm.registrationEmailAsUsername>
            <div class="form-group ${messagesPerField.printIfExists('username','has-error')}">
                <div class="col-sm-2 col-md-2">
                    <label for="username" class="control-label">${msg("username")}</label> <#if realm.editUsernameAllowed><span class="required">*</span></#if>
                    <br/><span>${msg("usernameChangeInfo")}</span>
                </div>

                <div class="col-sm-10 col-md-10">
                    <input type="text" class="form-control" id="username" name="username" <#if !realm.editUsernameAllowed>disabled="disabled"</#if> value="${(account.username!'')}"/>
                </div>
            </div>
        </#if>

        <div class="form-group ${messagesPerField.printIfExists('email','has-error')}">
            <div class="col-sm-2 col-md-2">
            <label for="email" class="control-label">${msg("email")}</label> <span class="required">*</span>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="email" name="email" autofocus value="${(account.email!'')}"/>
            </div>
        </div>

        <div class="form-group ${messagesPerField.printIfExists('academicTitle','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="academicTitle" class="control-label">${msg("academicTitle")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="academicTitle" name="user.attributes.academicTitle" value="${(account.attributes["academicTitle"] !'')}"/>               
            </div>
        </div>        

        <div class="form-group ${messagesPerField.printIfExists('firstName','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="firstName" class="control-label">${msg("firstName")}</label> <span class="required">*</span>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="firstName" name="firstName" value="${(account.firstName!'')}"/>
            </div>
        </div>

        <div class="form-group ${messagesPerField.printIfExists('lastName','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="lastName" class="control-label">${msg("lastName")}</label> <span class="required">*</span>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="lastName" name="lastName" value="${(account.lastName!'')}"/>
            </div>
        </div>

	<div class="form-group ${messagesPerField.printIfExists('jobTitle','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="jobTitle" class="control-label">${msg("jobTitle")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="jobTitle" name="user.attributes.jobTitle" value="${(account.attributes["jobTitle"] !'')}"/>               
            </div>
        </div>
        
        <hr>
        <h2>${msg("organization")}</h2>
        
        <div class="form-group ${messagesPerField.printIfExists('org_name','has-error')}">
            <div class="col-sm-2 col-md-2">
                <label for="org_name" class="control-label">${msg("org_name")}</label>
            </div>

            <div class="col-sm-10 col-md-10">
                <input type="text" class="form-control" id="org_name" name="user.attributes.org_name" value="${(account.attributes["org_name"] !'')}"/>               
            </div>
        </div>
        
        <div class="form-group ${messagesPerField.printIfExists('org_address','has-error')}">
	    <div class="col-sm-2 col-md-2">
		<label for="org_address" class="control-label">${msg("org_address")}</label>
	    </div>

	    <div class="col-sm-10 col-md-10">
		<input type="text" class="form-control" id="org_address" name="user.attributes.org_address" value="${(account.attributes["org_address"] !'')}"/>               
	    </div>
        </div>
        
	<div class="form-group ${messagesPerField.printIfExists('org_city','has-error')}">
	    <div class="col-sm-2 col-md-2">
		<label for="org_city" class="control-label">${msg("org_city")}</label>
	    </div>

	    <div class="col-sm-10 col-md-10">
		<input type="text" class="form-control" id="org_city" name="user.attributes.org_city" value="${(account.attributes["org_city"] !'')}"/>               
	    </div>
        </div>
        
	<div class="form-group ${messagesPerField.printIfExists('org_country','has-error')}">
	    <div class="col-sm-2 col-md-2">
		<label for="org_country" class="control-label">${msg("org_country")}</label>
	    </div>

	    <div class="col-sm-10 col-md-10">
		<input type="text" class="form-control" id="org_country" name="user.attributes.org_country" value="${(account.attributes["org_country"] !'')}"/>               
	    </div>
        </div>
                
        <div class="form-group">
            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">
                <div class="">
                    <#if url.referrerURI??><a href="${url.referrerURI}">${kcSanitize(msg("backToApplication")?no_esc)}</a></#if>
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save">${msg("doSave")}</button>
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Cancel">${msg("doCancel")}</button>
                </div>
            </div>
        </div>
    </form>

    <div class="row">
        <div class="col-md-10">
            <h2>${msg("changeAvatarHtmlTitle")}</h2>
        </div>
    </div>

    <#assign avatarUrl = url.accountUrl?replace("^(.*)(/account/?)(\\?(.*))?$", "$1/avatar-provider$4", 'r') />
    <#assign errorImg = url.resourcesPath + '/img/avatar.png' />
    <form action="${avatarUrl}" class="form-horizontal" method="post" enctype="multipart/form-data" >

        <img src="${avatarUrl}" style="max-width:100px" onerror="if (this.src != '${errorImg}') this.src = '${errorImg}';"  />
        <input type="file" id="avatar" name="image" onchange="checkFileSize();">

        <input type="hidden" name="stateChecker" value="${stateChecker}">

        <div class="form-group">
            <div id="kc-form-buttons" class="col-md-offset-2 col-md-10 submit">
                <div class="">
                    <button id="saveAvatar" type="submit" disabled="disabled" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save">${msg("doSave")}</button>
                    <!-- delete action is same as save. pressing button when no file is selected deletes avatar -->
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="deleteAction" value="Delete">${msg("doRemove")}</button>
                </div>
            </div>
        </div>
    </form>



</@layout.mainLayout>