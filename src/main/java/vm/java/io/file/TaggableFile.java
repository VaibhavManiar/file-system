package vm.java.io.file;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class TaggableFile {
    private final long id;
    private Set<String> tags;
    private final File file;
    private static final String DEFAULT_TAG = "default";

    public TaggableFile(File file) {
        this.file = file;
        this.tags = new HashSet<>();
        this.tags.add(DEFAULT_TAG);
        this.id = generateId();
    }

    public TaggableFile(Set<String> tags, File file) {
        this.tags = tags;
        this.file = file;
        this.id = generateId();
    }

    public void addTag(String tag) {
        tags.remove(DEFAULT_TAG);
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
        if(tags.isEmpty()) {
            tags.add(DEFAULT_TAG);
        }
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags.remove(DEFAULT_TAG);
        tags.remove(DEFAULT_TAG);
        tags.stream().filter(String::isEmpty).forEach(this.tags::add);
    }

    public File getFile() {
        return file;
    }

    public long getId() {
        return id;
    }

    public long size() {
        return file.length();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TaggableFile that = (TaggableFile) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    private long generateId() {
        // Generate ID of a file using hash code of the file and tags
        return (long) this.file.hashCode() + tags.stream().map(String::hashCode).map(Long::valueOf).reduce(0L, Long::sum);
    }
}
