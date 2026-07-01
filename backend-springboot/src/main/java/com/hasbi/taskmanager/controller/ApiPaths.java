package com.hasbi.taskmanager.controller;

public final class ApiPaths {

    public static final String ID = "id";
    public static final String PROJECT_ID = "projectId";

    public static final String TASKS = "/tasks";
    public static final String TASKS_ADD = "/add";
    public static final String TASKS_BY_PROJECT = "/project/{" + PROJECT_ID + "}";
    public static final String TASKS_BY_ID = "/{" + ID + "}";
    public static final String TASKS_UPDATE = "/update/{" + ID + "}";
    public static final String TASKS_DELETE = "/delete/{" + ID + "}";

    public static final String PROJECTS = "/projects";
    public static final String PROJECTS_ADD = "/add";
    public static final String PROJECTS_ALL = "/all";
    public static final String PROJECTS_BY_ID = "/{" + ID + "}";
    public static final String PROJECTS_UPDATE = "/update/{" + ID + "}";
    public static final String PROJECTS_DELETE = "/delete/{" + ID + "}";

    private ApiPaths() {
    }
}
