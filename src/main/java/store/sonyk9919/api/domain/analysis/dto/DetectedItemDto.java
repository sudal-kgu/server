package store.sonyk9919.api.domain.analysis.dto;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetectedItemDto {
    private String category;
    private Double confidence;
    private String filename;

    private DetectedItemDto(String category){
        this.category = category;
    }
    public static DetectedItemDto from(String category) {
        return new DetectedItemDto(category);
    }

    public static DetectedItemDto of(String category, Double confidence, String filename) {
        return new DetectedItemDto(category, confidence, filename);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetectedItemDto)) return false;
        DetectedItemDto other = (DetectedItemDto) o;
        return Objects.equals(category, other.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }
}
