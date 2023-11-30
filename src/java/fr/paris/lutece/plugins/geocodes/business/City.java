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
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.geocodes.business;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Date;
/**
 * This is the business class for the object City
 */ 
@JsonAutoDetect( creatorVisibility = Visibility.NONE, fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE )
public class City implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    public static final String ATTR_CODE = "code";
    public static final String ATTR_ID = "id";
    public static final String ATTR_DATE_CREATION = "creationDate";
    public static final String ATTR_DATE_END = "endDate";
    public static final String ATTR_VALUE_WITHOUT_ARTICLE = "valueWithoutArticle";
    public static final String ATTR_VALUE = "value";
    public static final String ATTR_CODE_ZONE = "codeZone";
    public static final String ATTR_CODE_COUNTRY = "codeCountry";
    
    @NotEmpty( message = "#i18n{geocodes.validation.city.CodeCountry.notEmpty}" )
    @Size( max = 10 , message = "#i18n{geocodes.validation.city.CodeCountry.size}" ) 
    private String _strCodeCountry;
    
    @NotEmpty( message = "#i18n{geocodes.validation.city.Code.notEmpty}" )
    @Size( max = 10 , message = "#i18n{geocodes.validation.city.Code.size}" ) 
    private String _strCode;
    
    @NotEmpty( message = "#i18n{geocodes.validation.city.Value.notEmpty}" )
    @Size( max = 255 , message = "#i18n{geocodes.validation.city.Value.size}" ) 
    private String _strValue;
    
    @Size( max = 10 , message = "#i18n{geocodes.validation.city.CodeZone.size}" ) 
    private String _strCodeZone;
    
    private Date _dateValidityStart;
    
    private Date _dateValidityEnd;
    
    private String _strValueMin;
    
    private String _strValueMinComplete;
    
    private Date _dateLastUpdate;

    /**
     * Returns the Id
     * @return The Id
     */
    @JsonProperty( ATTR_ID )
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */
    @JsonProperty( ATTR_ID )
    public void setId( int nId )
    {
        _nId = nId;
    }
    
    /**
     * Returns the CodeCountry
     * @return The CodeCountry
     */
    @JsonProperty( ATTR_CODE_COUNTRY )
    public String getCodeCountry( )
    {
        return _strCodeCountry;
    }

    /**
     * Sets the CodeCountry
     * @param strCodeCountry The CodeCountry
     */ 
    @JsonProperty( ATTR_CODE_COUNTRY )
    public void setCodeCountry( String strCodeCountry )
    {
        _strCodeCountry = strCodeCountry;
    }
    
    
    /**
     * Returns the Code
     * @return The Code
     */
    @JsonProperty( ATTR_CODE )
    public String getCode( )
    {
        return _strCode;
    }

    /**
     * Sets the Code
     * @param strCode The Code
     */ 
    @JsonProperty( ATTR_CODE )
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }
    
    
    /**
     * Returns the Value
     * @return The Value
     */
    public String getValue( )
    {
        return _strValue;
    }

    /**
     * Sets the Value
     * @param strValue The Value
     */ 
    public void setValue( String strValue )
    {
        _strValue = strValue;
    }
    
    
    /**
     * Returns the CodeZone
     * @return The CodeZone
     */
    @JsonProperty( ATTR_CODE_ZONE )
    public String getCodeZone( )
    {
        return _strCodeZone;
    }

    /**
     * Sets the CodeZone
     * @param strCodeZone The CodeZone
     */
    @JsonProperty( ATTR_CODE_ZONE )
    public void setCodeZone( String strCodeZone )
    {
        _strCodeZone = strCodeZone;
    }

    /**
     * Returns the dateValidityStart
     * @return The dateValidityStart
     */
	public Date getDateValidityStart( ) {
		return _dateValidityStart;
	}
    
	/**
     * Returns the dateValidityStart
     * @return The dateValidityStart
     */
	@JsonProperty( ATTR_DATE_CREATION )
    public String getDateValidityStartToString( ) {
		return _dateValidityStart.toString( );
	}


	/**
     * Sets the dateValidityStart
     * @param dateValidityStart The dateValidityStart
     */ 
    @JsonProperty( ATTR_DATE_CREATION )
	public void setDateValidityStart(Date dateValidityStart) {
		this._dateValidityStart = dateValidityStart;
	}

	/**
     * Returns the dateValidityEnd
     * @return The dateValidityEnd
     */
	public Date getDateValidityEnd() {
		return _dateValidityEnd;
	}
    
    /**
     * Returns the dateValidityStart
     * @return The dateValidityStart
     */
	@JsonProperty( ATTR_DATE_END )
    public String getDateValidityEndToString( ) {
		return _dateValidityEnd.toString( );
	}

	/**
     * Sets the dateValidityEnd
     * @param dateValidityEnd The dateValidityEnd
     */ 
    @JsonProperty( ATTR_DATE_END )
	public void setDateValidityEnd( Date dateValidityEnd ) {
		this._dateValidityEnd = dateValidityEnd;
	}

	/**
     * Returns the ValueMin
     * @return The ValueMin
     */
	@JsonProperty( ATTR_VALUE_WITHOUT_ARTICLE )
	public String getValueMin( ) {
		return _strValueMin;
	}

	/**
     * Sets the strValueMin
     * @param strValueMin The strValueMin
     */
	@JsonProperty( ATTR_VALUE_WITHOUT_ARTICLE )
	public void setValueMin( String strValueMin ) {
		this._strValueMin = strValueMin;
	}

	/**
     * Returns the ValueMinComplete
     * @return The ValueMinComplete
     */
	@JsonProperty( ATTR_VALUE )
	public String getValueMinComplete( ) {
		return _strValueMinComplete;
	}

	/**
     * Sets the strValueMinComplete
     * @param strValueMinComplete The strValueMinComplete
     */
	@JsonProperty( ATTR_VALUE )
	public void setValueMinComplete( String strValueMinComplete ) {
		this._strValueMinComplete = strValueMinComplete;
	}

	/**
     * Returns the dateLastUpdate
     * @return The dateLastUpdate
     */
	public Date getDateLastUpdate() {
		return _dateLastUpdate;
	}

	/**
     * Sets the dateLastUpdate
     * @param dateLastUpdate The dateLastUpdate
     */
	public void setDateLastUpdate(Date dateLastUpdate) {
		this._dateLastUpdate = dateLastUpdate;
	}
    
}
