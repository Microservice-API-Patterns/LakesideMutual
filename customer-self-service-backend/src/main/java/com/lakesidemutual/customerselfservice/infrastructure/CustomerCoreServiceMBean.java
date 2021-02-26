package com.lakesidemutual.customerselfservice.infrastructure;

public interface CustomerCoreServiceMBean {

    public int getSuccessfullAttemptsCounter();
    public int getUnuccessfullAttemptsCounter();
    public int getFallbackMethodExecutionCounter();
    
    public void resetCounters();
}