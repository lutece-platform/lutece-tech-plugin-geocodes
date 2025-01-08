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
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This is the business class for the object Country
 */ 
public class Country implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    protected int _nId;
    
    @NotEmpty( message = "#i18n{geocodes.validation.country.Code.notEmpty}" )
    @Size( max = 255 , message = "#i18n{geocodes.validation.country.Code.size}" ) 
    protected String _strCode;
    
    @NotEmpty( message = "#i18n{geocodes.validation.country.Value.notEmpty}" )
    @Size( max = 255 , message = "#i18n{geocodes.validation.country.Value.size}" )
    protected String _strValue;

    protected boolean _bAttached;

    protected Date _dateValidityStart;

    protected Date _dateValidityEnd;

    protected boolean _bDeprecated;

    protected String _strValueMinComplete;

    private List<CountryChanges> listChanges;

    private int pendingChanges;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }
    
    /**
     * Returns the Code
     * @return The Code
     */
    public String getCode( )
    {
        return _strCode;
    }

    /**
     * Sets the Code
     * @param strCode The Code
     */ 
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
     * Returns the boolean attached
     * @return attached
     */
	public boolean isAttached() {
		return _bAttached;
	}

	/**
     * Sets the boolean attached
     * @param _bAttached The boolean attached
     */ 
	public void setAttached(boolean _bAttached) {
		this._bAttached = _bAttached;
	}

    /**
     * Returns the date attached
     * @return attached
     */
    public Date getDateValidityEnd()
    {
        return _dateValidityEnd;
    }

    /**
     * Sets the date attached
     * @param _dateValidityEnd The date attached
     */
    public void setDateValidityEnd(Date _dateValidityEnd)
    {
        this._dateValidityEnd = _dateValidityEnd;
    }

    /**
     * Returns the date attached
     * @return attached
     */
    public Date getDateValidityStart()
    {
        return _dateValidityStart;
    }

    /**
     * Sets the date attached
     * @param _dateValidityStart The date attached
     */
    public void setDateValidityStart(Date _dateValidityStart)
    {
        this._dateValidityStart = _dateValidityStart;
    }

	/**
     * Returns the boolean deprecated
     * @return deprecated
     */
	public boolean isDeprecated() {
		return _bDeprecated;
	}

	/**
     * Sets the boolean deprecated
     * @param _bDeprecated The boolean deprecated
     */
	public void setDeprecated(boolean _bDeprecated) {
		this._bDeprecated = _bDeprecated;
	}

    public String getValueMinComplete()
    {
        return _strValueMinComplete;
    }

    public void setValueMinComplete(String _strValueMinComplete)
    {
        this._strValueMinComplete = _strValueMinComplete;
    }

    public List<CountryChanges> getListChanges()
    {
        return listChanges;
    }

    public void setListChanges(List<CountryChanges> listChanges)
    {
        this.listChanges = listChanges;
    }

    public int getPendingChanges()
    {
        return pendingChanges;
    }

    public void setPendingChanges(int pendingChanges)
    {
        this.pendingChanges = pendingChanges;
    }
    
}
