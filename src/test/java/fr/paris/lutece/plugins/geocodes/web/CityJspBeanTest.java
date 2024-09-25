/*
 * Copyright (c) 2002-2023, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.geocodes.web;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import java.util.List;
import java.io.IOException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityHome;
/**
 * This is the business class test for the object City
 */
public class CityJspBeanTest extends LuteceTestCase
{
    private static final String CODECOUNTRY1 = "CodeCountry1";
    private static final String CODECOUNTRY2 = "CodeCountry2";
    private static final String CODE1 = "Code1";
    private static final String CODE2 = "Code2";
    private static final String VALUE1 = "Value1";
    private static final String VALUE2 = "Value2";
    private static final String CODEZONE1 = "CodeZone1";
    private static final String CODEZONE2 = "CodeZone2";

public void testJspBeans(  ) throws AccessDeniedException, IOException
	{	
     	MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockServletConfig config = new MockServletConfig();

		//display admin City management JSP
		CityJspBean jspbean = new CityJspBean();
		String html = jspbean.getManageCities( request );
		assertNotNull(html);

		//display admin City creation JSP
		html = jspbean.getCreateCity( request );
		assertNotNull(html);

		//action create City
		request = new MockHttpServletRequest();

		response = new MockHttpServletResponse( );
		AdminUser adminUser = new AdminUser( );
		adminUser.setAccessCode( "admin" );
		
        
        request.addParameter( "code_country" , CODECOUNTRY1 );
        request.addParameter( "code" , CODE1 );
        request.addParameter( "value" , VALUE1 );
        request.addParameter( "code_zone" , CODEZONE1 );
		request.addParameter("action","createCity");
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createCity" ));
		request.setMethod( "POST" );
        
		
		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 
			
			
			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}

		//display modify City JSP
		request = new MockHttpServletRequest();
        request.addParameter( "code_country" , CODECOUNTRY1 );
        request.addParameter( "code" , CODE1 );
        request.addParameter( "value" , VALUE1 );
        request.addParameter( "code_zone" , CODEZONE1 );
		List<Integer> listIds = CityHome.getIdCitiesList( null, null, null, null, null);
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new CityJspBean();
		
		assertNotNull( jspbean.getModifyCity( request ) );	

		//action modify City
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");
		
        request.addParameter( "code_country" , CODECOUNTRY2 );
        request.addParameter( "code" , CODE2 );
        request.addParameter( "value" , VALUE2 );
        request.addParameter( "code_zone" , CODEZONE2 );
		request.setRequestURI("jsp/admin/plugins/example/ManageCities.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createCity, qui est l'action par défaut
		request.addParameter("action","modifyCity");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyCity" ));

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response );

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}
		
		//get remove City
		request = new MockHttpServletRequest();
        //request.setRequestURI("jsp/admin/plugins/example/ManageCities.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new CityJspBean();
		request.addParameter("action","confirmRemoveCity");
		assertNotNull( jspbean.getModifyCity( request ) );
				
		//do remove City
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("jsp/admin/plugins/example/ManageCityts.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createCity, qui est l'action par défaut
		request.addParameter("action","removeCity");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeCity" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.setMethod("POST");
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}	
     
     }
}
