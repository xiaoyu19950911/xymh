package com.example.demo.web.request;

import java.util.List;

public class ImportWordsRequest {

    private List<String> documentIdList;

    public List<String> getDocumentIdList() {
        return documentIdList;
    }

    public void setDocumentIdList(List<String> documentIdList) {
        this.documentIdList = documentIdList;
    }

    @Override
    public String toString() {
        return "ImportWordsRequest{" +
                "documentIdList=" + documentIdList +
                '}';
    }
}
