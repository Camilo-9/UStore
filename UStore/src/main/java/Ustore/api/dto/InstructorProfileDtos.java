
package Ustore.api.dto;

import java.io.Serializable;

public class InstructorProfileDtos{
    public record InstructorProfileCreateRequest(String phone, String bio, Long instructorId) implements Serializable{}
    public record InstructorProfileResponse(Long id, String phone, String bio, Long instructorId) implements Serializable{}
    public record InstructorProfileUpdateRequest(String phone, String bio) implements Serializable{}
}
