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
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class DriverProxy implements Driver
{
  //Attributes
  private Driver m_Driver;

  public DriverProxy( Driver driver )
  {
    m_Driver = driver;
  }

  public Connection connect(String url, Properties info) throws SQLException
  {
    return m_Driver.connect(url, info);
  }

  public boolean acceptsURL(String url) throws SQLException
  {
    return m_Driver.acceptsURL(url);
  }

  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
  {
    return m_Driver.getPropertyInfo(url, info);
  }

  public int getMajorVersion()
  {
    return m_Driver.getMajorVersion();
  }

  public int getMinorVersion()
  {
    return m_Driver.getMinorVersion();
  }

  public boolean jdbcCompliant()
  {
    return m_Driver.jdbcCompliant();
  }
}
