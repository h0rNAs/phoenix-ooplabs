package ru.ssau.tk.phoenix.ooplabs.util;

public class Criteria {
    private String column;
    private Object[] params;
    private SortingType sortingType;

    public Criteria(String column, Object[] params, SortingType sortingType) {
        this.column = column;
        this.params = params;
        this.sortingType = sortingType;
    }

    public Criteria(String column, Object[] params) {
        this(column, params, null);
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParam(Object[] params) {
        this.params = params;
    }

    public SortingType getSortingType() {
        return sortingType;
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }
}
