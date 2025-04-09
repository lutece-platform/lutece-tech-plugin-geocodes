<jsp:useBean id="managegeocodesChanges" scope="session" class="fr.paris.lutece.plugins.geocodes.web.ChangesJspBean" />
<% String strContent = managegeocodesChanges.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
