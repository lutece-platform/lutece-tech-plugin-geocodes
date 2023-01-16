<jsp:useBean id="managegeocodesCity" scope="session" class="fr.paris.lutece.plugins.geocodes.web.CityJspBean" />
<% String strContent = managegeocodesCity.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
