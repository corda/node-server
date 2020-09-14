package net.corda.explorer.model.response;

public class EntriesCountResponse {
    private int entriesCount;

    public int getEntriesCount() {
        return entriesCount;
    }

    public void setEntriesCount(int entriesCount) {
        this.entriesCount = entriesCount;
    }

    public EntriesCountResponse(int entriesCount) {
        this.entriesCount = entriesCount;
    }
}
