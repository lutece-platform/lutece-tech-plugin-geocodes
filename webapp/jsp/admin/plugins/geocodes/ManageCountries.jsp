<jsp:useBean id="managegeocodesCountry" scope="session" class="fr.paris.lutece.plugins.geocodes.web.CountryJspBean" />
<% String strContent = managegeocodesCountry.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
