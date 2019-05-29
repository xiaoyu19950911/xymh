package com.example.demo.entity.project;

import java.io.Serializable;

public class Table implements Serializable {

    private static final long serialVersionUID = 8173227066207496516L;

    private String TableJson;//表格

    public String getTableJson() {
        return TableJson;
    }

    public void setTableJson(String tableJson) {
        TableJson = tableJson;
    }

    @Override
    public String toString() {
        return "Table{" +
                "TableJson='" + TableJson + '\'' +
                '}';
    }
}
