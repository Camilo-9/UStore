
package edu.unimagdalena.uStore.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateCategoryRequest{
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
