package com.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.database.UsersDatabaseServer;;

/**
 * Application Lifecycle Listener implementation class ServletContext
 *
 */
@WebListener
public class ServletContext implements ServletContextListener {
	private UsersDatabaseServer db;
    /**
     * Default constructor. 
     */
    public ServletContext() {
    	db = new UsersDatabaseServer();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         db.deleteAll();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	db.deleteAll();
    }
	
}
