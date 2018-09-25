package com.jumkid.media.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common service interface
 */

public abstract class AbstractCommonService implements ICommonService{

    //TODO: some comment service functions can be added. Like logging, performance monitoring, etc.

    public abstract ServiceCommand execute(ServiceCommand cmd);

    protected boolean isAction(ServiceCommand cmd, String action){
        if(cmd == null) return false;
        if(action.equals(cmd.getAction())) return true;
        return false;
    }

}
