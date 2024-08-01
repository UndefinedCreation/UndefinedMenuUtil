package com.undefined.menuutil

import com.undefined.api.UndefinedAPI
import org.bukkit.plugin.java.JavaPlugin

class MenuUtil : JavaPlugin() {

    companion object {
        lateinit var undefinedAPI: UndefinedAPI
    }

    override fun onEnable() {
        // Plugin startup logic
        undefinedAPI = UndefinedAPI(this)

        MenuUtilCommand()

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
