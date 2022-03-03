package com.bettingScanner.api.master;

public class SlaveTestResult {
    private Boolean active;
    private Boolean tipsportConnected;

    public SlaveTestResult(Boolean active, Boolean tipsportConnected) {
        this.active = active;
        this.tipsportConnected = tipsportConnected;
    }

    public SlaveTestResult() {
    }

    public Boolean getActive() {
        return this.active;
    }

    public Boolean getTipsportConnected() {
        return this.tipsportConnected;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setTipsportConnected(Boolean tipsportConnected) {
        this.tipsportConnected = tipsportConnected;
    }
}
