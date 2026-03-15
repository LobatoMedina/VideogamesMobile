package com.lobato.videogamesmobile.DTOs;

public class EsrbDTO {
    private Integer id;
    private String name;
    private Integer EdadMin;

    public EsrbDTO() {
    }

    public EsrbDTO(Integer id, String name, Integer edadMin) {
        this.id = id;
        this.name = name;
        this.EdadMin = edadMin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEdadMin() {
        return EdadMin;
    }

    public void setEdadMin(Integer edadMin) {
        EdadMin = edadMin;
    }
}
