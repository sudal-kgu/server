package store.sonyk9919.api.global.file.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileDirectory {
    OUTPUTS("outputs/"),
    INPUTS("inputs/");

    private String directory;

    @Override
    public String toString() {
        return directory;
    }
}
