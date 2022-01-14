package com.mii.komi.dto.inbound;

import java.util.Optional;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class SettlementRequest extends BaseInboundRequestDTO {
    
    private String status;
    
    private String reason;
    
    private String originalNoRef;

    private String additionalInfo;

    private String bizMsgId;

    private String msgId;

    private String counterParty;

    public String getOriginalNoRef() {
        return Optional.ofNullable(originalNoRef).orElse("");
    }

    public void setOriginalNoRef(String originalNoRef) {
        this.originalNoRef = originalNoRef;
    }

    public String getAdditionalInfo() {
        return Optional.ofNullable(additionalInfo).orElse("");
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the bizMsgId
     */
    public String getBizMsgId() {
        return bizMsgId;
    }

    /**
     * @param bizMsgId the bizMsgId to set
     */
    public void setBizMsgId(String bizMsgId) {
        this.bizMsgId = bizMsgId;
    }

    /**
     * @return msgId
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * @param msgId the msgId to set
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /**
     * @return counterParty
     */
    public String getCounterParty() {
        return counterParty;
    }

    /**
     * @param the counterParty to set
     */
    public void setCounterParty(String counterParty) {
        this.counterParty = counterParty;
    }

}
