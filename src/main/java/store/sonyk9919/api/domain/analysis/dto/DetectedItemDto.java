package store.sonyk9919.api.domain.analysis.dto;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetectedItemDto {
    private String category;
    private String subcategory;
    private Confidence confidence;
    private String filename;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class Confidence {
        private Double object;
        private Double material;
    }

    public static DetectedItemDto of(String category, String subcategory) {
        DetectedItemDto item = new DetectedItemDto();
        item.category = category;
        item.subcategory = subcategory;
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetectedItemDto)) return false;
        DetectedItemDto other = (DetectedItemDto) o;
        return Objects.equals(category, other.category)
                && Objects.equals(subcategory, other.subcategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, subcategory);
    }

    public String getKey() {
        return TrashTaxonomy.generateKey(category, subcategory);
    }
}
