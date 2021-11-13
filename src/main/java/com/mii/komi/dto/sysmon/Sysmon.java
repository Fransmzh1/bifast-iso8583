package com.mii.komi.dto.sysmon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author vinch
 */
public class Sysmon<T> {

    /**
     * @return the moduleDetail
     */
    public T getModuleDetail() {
        return moduleDetail;
    }

    /**
     * @param moduleDetail the moduleDetail to set
     */
    public void setModuleDetail(T moduleDetail) {
        this.moduleDetail = moduleDetail;
    }
    
    private String moduleName;
    
    private String moduleType;
    
    @JsonInclude(Include.NON_NULL)
    private T moduleDetail;
    
    public Sysmon(T moduleDetail) {
        this.moduleDetail = moduleDetail;
    }
    
    public Sysmon() {}
    
    public static Sysmon success(String moduleName, String moduleType, Object o) {
        Sysmon rr = new Sysmon(o);
        rr.setModuleName(moduleName);
        rr.setModuleType(moduleType);
        rr.setModuleDetail(o);
        return rr;
    }
    
    public static Sysmon success(String moduleName, String moduleType) {
        Sysmon rr = new Sysmon();
        rr.setModuleName(moduleName);
        rr.setModuleType(moduleType);
        return rr;
    }

    /**
     * @return the moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName the moduleName to set
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * @return the moduleType
     */
    public String getModuleType() {
        return moduleType;
    }

    /**
     * @param moduleType the moduleType to set
     */
    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
    
}
