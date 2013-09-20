<%@page import="com.liferay.portal.model.ColorScheme"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.model.User"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%
User selUser = (User)request.getAttribute("user.selUser");
String meu_tema = (String)request.getAttribute("meu_tema"); 
%>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<portlet:actionURL name='savePreference' var="saveURL" windowState="normal" />
<aui:form action="<%= saveURL %>" method="POST" name="fm">
	<aui:fieldset>
	<% 
		List<ColorScheme> colors = theme.getColorSchemes(); 
		for(ColorScheme scheme : colors ){
			%>
			<input type="radio" class="meu_tema" name="meu_tema" id="meu_tema_<%= scheme.getColorSchemeId() %>" value="<%= scheme.getName().toLowerCase() %>" <%= scheme.getName().toLowerCase().equalsIgnoreCase(meu_tema) ? " checked=\"checked\" " : "" %> />
			<label for="meu_tema_<%= scheme.getColorSchemeId() %>"><%= scheme.getName() %></label>
	<% } %>	
	</aui:fieldset>
	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>