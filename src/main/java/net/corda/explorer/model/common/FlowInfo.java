package net.corda.explorer.model.common;

import java.util.List;
import java.util.Map;

public class FlowInfo {
    private String flowName;
    private List<FlowParam> flowParams;
    private Map<String, List<FlowParam>> flowParamsMap;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public List<FlowParam> getFlowParams() {
        return flowParams;
    }

    public void setFlowParams(List<FlowParam> flowParams) {
        this.flowParams = flowParams;
    }

    public Map<String, List<FlowParam>> getFlowParamsMap() {
        return flowParamsMap;
    }

    public void setFlowParamsMap(Map<String, List<FlowParam>> flowParamsMap) {
        this.flowParamsMap = flowParamsMap;
    }
}