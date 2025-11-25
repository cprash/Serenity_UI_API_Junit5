package helpers;


import utils.ConfigReader;

import java.util.Objects;

public class EnvironmentHelper {

    private static String environment;

    public static synchronized String getEnvironment(){
        if(Objects.isNull(environment)){
            environment = ConfigReader.getEnvironment();
        }
        return  environment;
    }
}
