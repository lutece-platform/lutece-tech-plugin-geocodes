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

/**
 * SQL fragments shared by the geocodes DAOs.
 */
public final class GeocodesSqlUtils
{
    private static final String ACCENTS = "àâäéèêëîïôöùûüÿçñ";
    private static final String PLAIN = "aaaeeeeiioouuuycn";

    private GeocodesSqlUtils( )
    {
    }

    /**
     * Wraps a SQL expression in the accent folding the searches compare on: lower case, the two ligatures spelled
     * out, and every accented letter replaced by its plain one.
     *
     * Written with REPLACE only. The former expression used TRANSLATE, which MariaDB and MySQL do not have: the
     * statement failed at the server and the driver reported it as a null pointer while closing the statement,
     * so every search answered an internal error whose message named nothing.
     *
     * @param strExpression
     *            the SQL expression to fold, a column name or a placeholder
     * @return the folded expression
     */
    public static String fold( String strExpression )
    {
        StringBuilder sbFolded = new StringBuilder( "REPLACE( REPLACE( LOWER( " ).append( strExpression ).append( " ), 'œ', 'oe' ), 'æ', 'ae' )" );

        for ( int i = 0; i < ACCENTS.length( ); i++ )
        {
            sbFolded.insert( 0, "REPLACE( " ).append( ", '" ).append( ACCENTS.charAt( i ) ).append( "', '" ).append( PLAIN.charAt( i ) ).append( "' )" );
        }

        return sbFolded.toString( );
    }
}
