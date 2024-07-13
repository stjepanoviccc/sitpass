package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.Image;
import sitpass.sitpassbackend.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDTO {

    private Long id;
    @NotBlank
    private String path;
    private Facility facility;
    private User user;
    private Boolean isDeleted;

    public static ImageDTO convertToDto(Image image) {
        return ImageDTO.builder()
                .id(image.getId())
                .path(image.getPath())
                .facility(image.getFacility())
                .user(image.getUser())
                .isDeleted(image.getIsDeleted())
                .build();
    }

    public Image convertToModel() {
        return Image.builder()
                .id(getId())
                .path(getPath())
                .facility(getFacility())
                .user(getUser())
                .isDeleted(getIsDeleted())
                .build();
    }

}
