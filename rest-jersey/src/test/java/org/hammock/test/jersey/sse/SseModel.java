package org.hammock.test.jersey.sse;

public class SseModel {
    private String name;

    public SseModel() {
    }

    public SseModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
