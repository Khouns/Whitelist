/*
 * Copyright (C) 2011 <silence@immortal-forces.net>
 *
 * This file is part of the Bukkit m_Plugin Whitelist.
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

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class WLPlayerListener extends PlayerListener
{
  private final Whitelist m_Plugin;
  
  public WLPlayerListener(Whitelist instance)
  {
    m_Plugin = instance;
  }

  @Override
  public void onPlayerLogin(PlayerLoginEvent event)
  {
    if ( m_Plugin.isWhitelistActive() )
    {
      //Check if whitelist.txt needs to be reloaded
      if ( m_Plugin.needReloadWhitelist() )
      {
        System.out.println("Whitelist: Executing scheduled whitelist reload.");
        m_Plugin.reloadSettings();
        m_Plugin.resetNeedReloadWhitelist();
      }

      String playerName = event.getPlayer().getName();
      System.out.print("Whitelist: Player " + playerName + " is trying to join...");
      if ( m_Plugin.isOnWhitelist(playerName) )
      {
        System.out.println("allow!");
      }
      else
      {
        System.out.println("kick!");
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, m_Plugin.getKickMessage());
      }
    }
  }

  @Override
  public void onPlayerCommand(PlayerChatEvent event)
  {
    //commands:
    // /whitelist reload
    // /whitelist add [PlayerName]
    // /whitelist remove [PlayerName]
    // /whitelist list
    if ( m_Plugin.isAdmin(event.getPlayer().getName()) )
    {
      String[] command = event.getMessage().split(" ");
      if ( command != null && command.length > 0 && command[0].compareToIgnoreCase("/whitelist") == 0 )
      {
        if ( command.length < 2 )
          event.getPlayer().sendMessage("Whitelist: Missing parameters!");
        if ( command[1].compareToIgnoreCase("help") == 0 )
        {
          event.getPlayer().sendMessage("Whitelist: Commands:");
          event.getPlayer().sendMessage("Whitelist: /whitelist reload  (reloads the whitelist and settings)");
          event.getPlayer().sendMessage("Whitelist: /whitelist add [player]  (adds a player to the whitelist)");
          event.getPlayer().sendMessage("Whitelist: /whitelist remove [player]  (removes a player from the whitelist)");
          event.getPlayer().sendMessage("Whitelist: /whitelist on|off  (actives/deactivates whitelist)");
          event.getPlayer().sendMessage("Whitelist: /whitelist list  (list whitelist entries)");
        }
        if ( command[1].compareToIgnoreCase("reload") == 0 )
        {
          if ( m_Plugin.reloadSettings() )
            event.getPlayer().sendMessage("Whitelist: Settings and whitelist reloaded");
          else
            event.getPlayer().sendMessage("Whitelist: Could not reload whitelist...");
        }
        else if(command[1].compareToIgnoreCase("add") == 0)
        {
          if ( command.length < 3 )
          {
            event.getPlayer().sendMessage("Whitelist: Parameter missing: Player name");
          }
          else
          {
            if ( m_Plugin.addPlayerToWhitelist( command[2] ) )
              event.getPlayer().sendMessage("Whitelist: Player \"" + command[2] + "\" added");
            else
              event.getPlayer().sendMessage("Whitelist: Could not add player \"" + command[2] + "\"");
          }
        }
        else if(command[1].compareToIgnoreCase("remove") == 0)
        {
          if ( command.length < 3 )
          {
            event.getPlayer().sendMessage("Whitelist: Parameter missing: Player name");
          }
          else
          {
            if ( m_Plugin.removePlayerFromWhitelist( command[2] ))
              event.getPlayer().sendMessage("Whitelist: Player \"" + command[2] + "\" removed");
            else
              event.getPlayer().sendMessage("Whitelist: Could not remove player \"" + command[2] + "\"");
          }
        }
        else if (command[1].compareToIgnoreCase("on") ==0)
        {
          m_Plugin.setWhitelistActive(true);
          event.getPlayer().sendMessage("Whitelist: Activated");
        }
        else if (command[1].compareToIgnoreCase("off") ==0)
        {
          m_Plugin.setWhitelistActive(false);
          event.getPlayer().sendMessage("Whitelist: Deactivated");
        }
        else if (command[1].compareToIgnoreCase("list") ==0 && !m_Plugin.isListCommandDisabled())
        {
          event.getPlayer().sendMessage("Players on whitelist: " + m_Plugin.getFormatedAllowList());
        }
      }
    }
  }
}