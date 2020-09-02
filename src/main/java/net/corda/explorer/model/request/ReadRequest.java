package net.corda.explorer.model.request;

import java.io.File;
import java.util.List;

public class ReadRequest {
    private int startIndex;
    private int stopIndex;
    private List<String> components;

    public int getStartIndex() { return startIndex; }

    public int getStopIndex() { return stopIndex; }

    public List<String> getComponents() { return components; }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setStopIndex(int stopIndex) {
        this.stopIndex = stopIndex;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public String path() {
        return components.stream().reduce(
                "", //System.getProperty("user.home"),
                (acc, next) -> acc + File.separator + next
        );
    }
}
