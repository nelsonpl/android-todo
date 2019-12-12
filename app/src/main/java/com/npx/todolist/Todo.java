package com.npx.todolist;

public class Todo {
    private long id;
    private String Title;
    private Long Create;
    private Long When;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Long getCreate() {
        return Create;
    }

    public void setCreate(Long create) {
        Create = create;
    }

    public Long getWhen() {
        return When;
    }

    public void setWhen(Long when) {
        When = when;
    }
}
