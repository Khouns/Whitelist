/*
 * Copyright (C) 2011 <silence@immortal-forces.net>
 *
 * This file is part of the Bukkit plugin Whitelist.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307, USA.
 */

package net.immortal_forces.silence.plugin.whitelist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection
{
  //Attributes
  final String m_strQuery;
  final String m_strQueryAdd;
  final String m_strQueryRemove;
  final String m_strConnection;
  Connection m_Connection;

  public SQLConnection(String strDriver, String strConnection, String strQuery, String strQueryAdd, String strQueryRemove) throws Exception
  {
    m_strQuery = strQuery;
    m_strQueryAdd = strQueryAdd;
    m_strQueryRemove = strQueryRemove;
    m_strConnection = strConnection;
    m_Connection = null;
    
    try
    {
      Class.forName(strDriver).newInstance();
      m_Connection = DriverManager.getConnection(strConnection);
    }
    catch (SQLException ex)
    {
      System.out.println("Whitelist: SQLException: " + ex.getMessage());
      System.out.println("Whitelist: SQLState: " + ex.getSQLState());
      System.out.println("Whitelist: VendorError: " + ex.getErrorCode());
      throw ex;
    }
    catch (Exception ex)
    {
      System.out.println("Whitelist: Exception: " + ex.toString() + " - missing connector?" );
      throw ex;
    }
  }

  public boolean isOnWhitelist(String playerName, boolean bRetry)
  {
    try
    {
      if ( m_Connection == null )
        m_Connection = DriverManager.getConnection(m_strConnection);

      Statement stmt = m_Connection.createStatement();
      ResultSet rst = stmt.executeQuery(m_strQuery.replace("<%USERNAME%>", playerName));
      if ( rst.first() )
        return true;
      else
        return false;
    }
    catch (SQLException ex)
    {
      m_Connection = null;
      if ( bRetry )
      {
        return isOnWhitelist(playerName, false);
      }
      else
      {
        System.out.println("Whitelist: SQLException: " + ex.getMessage());
        System.out.println("Whitelist: SQLState: " + ex.getSQLState());
        System.out.println("Whitelist: VendorError: " + ex.getErrorCode());
      }
    }
    catch (Exception ex)
    {
      System.out.println("Whitelist: Exception: " +ex.getMessage() );
    }
    return false;
  }

  public boolean addPlayerToWhitelist(String playerName, boolean bRetry)
  {
    if ( m_strQueryAdd != null && !m_strQueryAdd.isEmpty() )
    {
      try
      {
        if ( m_Connection == null )
          m_Connection = DriverManager.getConnection(m_strConnection);
        Statement stmt = m_Connection.createStatement();
        stmt.execute(m_strQueryAdd.replace("<%USERNAME%>", playerName));
        return true;
      }
      catch (SQLException ex)
      {
        m_Connection = null;
        if ( bRetry )
        {
          return addPlayerToWhitelist(playerName, false);
        }
        else
        {
          System.out.println("Whitelist: SQLException: " + ex.getMessage());
          System.out.println("Whitelist: SQLState: " + ex.getSQLState());
          System.out.println("Whitelist: VendorError: " + ex.getErrorCode());
        }
      }
      catch (Exception ex)
      {
        System.out.println("Whitelist: Exception: " +ex.getMessage() );
      }
    }
    return false;
  }

  public boolean removePlayerFromWhitelist(String playerName, boolean bRetry)
  {
    if ( m_strQueryRemove != null && !m_strQueryRemove.isEmpty() )
    {
      try
      {
        if ( m_Connection == null )
          m_Connection = DriverManager.getConnection(m_strConnection);
        Statement stmt = m_Connection.createStatement();
        stmt.execute(m_strQueryRemove.replace("<%USERNAME%>", playerName));
        return true;
      }
      catch (SQLException ex)
      {
        m_Connection = null;
        if ( bRetry )
        {
          return removePlayerFromWhitelist(playerName, false);
        }
        else
        {
          System.out.println("Whitelist: SQLException: " + ex.getMessage());
          System.out.println("Whitelist: SQLState: " + ex.getSQLState());
          System.out.println("Whitelist: VendorError: " + ex.getErrorCode());
        }
      }
      catch (Exception ex)
      {
        System.out.println("Whitelist: Exception: " +ex.getMessage() );
      }
    }
    return false;
  }
}
