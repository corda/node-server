package net.corda.explorer.model.request;

import java.util.List;

public class EntriesCountRequest {
    private List<String> components;

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }
}
