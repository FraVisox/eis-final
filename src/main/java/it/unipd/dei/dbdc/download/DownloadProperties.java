package it.unipd.dei.dbdc.download;

import it.unipd.dei.dbdc.download.interfaces.APICaller;
import it.unipd.dei.dbdc.download.interfaces.APIManager;
import it.unipd.dei.dbdc.resources.PropertiesTools;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class DownloadProperties {

    private final static String caller_key = "library";
    private final static String default_properties = "download.properties";

    //Lancia IOException se non ci sono le properties di default o se le properties di default o passate sono fatte male
    public static HashMap<String, APIManager> readAPIContainerProperties(String out_properties) throws IOException {

        Properties downProps = PropertiesTools.getProperties(default_properties, out_properties);

        // 1. Cerco la property del caller, che è la classe che implementa APICaller
        String caller_name = downProps.getProperty(caller_key);
        Class<?> caller_class;
        try {
            caller_class = Class.forName(caller_name);
        } catch (ClassNotFoundException e) {
            throw new IOException("There is no " + caller_key + " property in the file of the download properties, or the value is not correct");
        }

        // 2. Creo un'istanza di questa classe che implementa APICaller
        APICaller caller;
        try {
            caller = (APICaller) caller_class.newInstance(); // Lancia ClassCastException o InstantiationException o IllegalAccessException
        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
            throw new IOException("The class " + caller_name + " is not in the correct format.\n" +
                    "It should have a constructor with no parameters and it should implement APICaller interface");
        }

        HashMap<String, APIManager> managers = new HashMap<>(1);
        // 3. Leggendo le properties, creo tutte le istanze degli APIManager di tutte le API specificate
        try {
            Enumeration<?> enumeration = downProps.propertyNames();
            while (enumeration.hasMoreElements()) {
                String prop = (String) enumeration.nextElement();
                if (prop.equals(caller_key)) {
                    continue;
                }
                String manager_name = downProps.getProperty(prop);
                Class<?> manager_class = Class.forName(manager_name);
                Constructor<?> constructor = manager_class.getConstructor(APICaller.class, String.class); // Lancia NoSuchMethodException
                managers.put(prop, (APIManager) constructor.newInstance(caller, prop)); // Lancia InvocationTargetException
            }
        } catch (InstantiationException | IllegalAccessException | ClassCastException | ClassNotFoundException |
                 NoSuchMethodException | InvocationTargetException ex) {
            throw new IOException("The format of the file is not correct.\n" +
                    "It should have a key string representing the name of the API and " +
                    "as a value the class that implements the manager of that API.\n" +
                    "This class should also implement APIManager interface and have a constructor that accepts an instance of an APICaller as argument.");
        }
        return managers;
    }
}