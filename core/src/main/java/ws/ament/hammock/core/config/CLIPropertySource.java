/*
 * Copyright 2016 John D. Ament
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ws.ament.hammock.core.config;

import org.apache.tamaya.core.propertysource.BasePropertySource;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * PropertySource that allows to add the programs main arguments as configuration entries. Unix syntax using '--' and
 * '-' params is supported.
 */
@ApplicationScoped
public class CLIPropertySource extends BasePropertySource {

    /** The original main arguments. */
    private static String[] args = new String[0];

    /** The map of parsed main arguments. */
    private static Map<String,String> mainArgs;

    /** Initializes the initial state. */
    static{
        initMainArgs(args);
    }


    /**
     * Creates a new instance.
     */
    public CLIPropertySource(){}

    /**
     * Configure the main arguments, herby parsing and mapping the main arguments into
     * configuration properties.
     * @param args the main arguments, not null.
     * @returns the parsed main arguments as key/value pairs.
     */
    public static void initMainArgs(String... args){
        CLIPropertySource.args = Objects.requireNonNull(args);
        // TODO is there a way to figure out the args?
        String argsProp = System.getProperty("main.args");
        if(argsProp!=null){
            CLIPropertySource.args = argsProp.split("\\s");
        }
        Map<String,String> result = null;
        if(CLIPropertySource.args==null){
            result = Collections.emptyMap();
        }else{
            result = new HashMap<>();
            String prefix = System.getProperty("main.args.prefix");
            if(prefix==null){
                prefix="";
            }
            String key = null;
            for(String arg:CLIPropertySource.args){
                if(arg.startsWith("--")){
                    arg = arg.substring(2);
                    int index = arg.indexOf('=');
                    if(index>0){
                        key = arg.substring(0,index).trim();
                        result.put(prefix+key, arg.substring(index+1).trim());
                        key = null;
                    }else{
                        result.put(prefix+arg, arg);
                    }
                }else if(arg.startsWith("-")){
                    key = arg.substring(1);
                }else{
                    if(key!=null){
                        result.put(prefix+key, arg);
                        key = null;
                    }else{
                        result.put(prefix+arg, arg);
                    }
                }
            }
        }
        CLIPropertySource.mainArgs = Collections.unmodifiableMap(result);
    }

    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(mainArgs);
    }
}