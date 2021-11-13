package com.mii.komi.dto.sysmon;

/**
 *
 * @author vinch
 */
public class TxnMgr {
    
    private String tps;
    
    private float tpsAverage;
    
    private int tpsPeak;

    /**
     * @return the tps
     */
    public String getTps() {
        return tps;
    }

    /**
     * @param tps the tps to set
     */
    public void setTps(String tps) {
        this.tps = tps;
    }

    /**
     * @return the tpsAverage
     */
    public float getTpsAverage() {
        return tpsAverage;
    }

    /**
     * @param tpsAverage the tpsAverage to set
     */
    public void setTpsAverage(float tpsAverage) {
        this.tpsAverage = tpsAverage;
    }

    /**
     * @return the tpsPeak
     */
    public int getTpsPeak() {
        return tpsPeak;
    }

    /**
     * @param tpsPeak the tpsPeak to set
     */
    public void setTpsPeak(int tpsPeak) {
        this.tpsPeak = tpsPeak;
    }
    
}
