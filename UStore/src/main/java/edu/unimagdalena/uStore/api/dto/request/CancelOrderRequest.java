
package edu.unimagdalena.uStore.api.dto.request;
import jakarta.validation.constraints.NotBlank;

public class CancelOrderRequest{
    @NotBlank
    private String notes;

    public String getNotes(){
        return notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }
}
