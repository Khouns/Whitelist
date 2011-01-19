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

package com.bukkit.silence.whitelist;

import java.io.File;
import java.util.TimerTask;
        
public class FileWatcher extends TimerTask
{
  private File m_File;
  private long m_LastModified;
  private volatile  boolean m_WasChanged;
  
  FileWatcher(File file)
  {
    m_File = file;
    m_LastModified = m_File.lastModified();
  }

  @Override
  public void run()
  {
    if ( m_LastModified != m_File.lastModified() )
    {
      m_LastModified = m_File.lastModified();
      if ( !m_WasChanged )
      {
        m_WasChanged = true;
        System.out.println("Whitelist: Whitelist.txt was updated. Whitelist was scheduled for reloading.");
      }
    }
  }

  public boolean wasFileModified()
  {
    return m_WasChanged;
  }

  public void resetFileModifiedState()
  {
    m_WasChanged = false;
  }

}
