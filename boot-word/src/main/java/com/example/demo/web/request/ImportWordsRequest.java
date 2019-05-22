package com.example.demo.web.request;

import java.util.List;

public class ImportWordsRequest {

    private List<String> pathList;

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    @Override
    public String toString() {
        return "ImportWordsRequest{" +
                "pathList=" + pathList +
                '}';
    }
}
