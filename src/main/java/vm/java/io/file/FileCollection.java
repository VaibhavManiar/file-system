package vm.java.io.file;

import java.util.HashSet;
import java.util.Set;

public class FileCollection implements Comparable<FileCollection> {
    private final Set<Long> fileIds;
    private long collectionSizeInBytes = 0;
    private final String tag;
    private final Set<FileCollection> childCollections = new HashSet<>();
    private FileCollection parentCollection = null;
    private static final String DEFAULT_TAG = "default";
    private static final FileCollection DEFAULT_COLLECTION = new FileCollection(DEFAULT_TAG);

    public static FileCollection defaultCollection() {
        return DEFAULT_COLLECTION;
    }

    public FileCollection(String tag) {
        this.tag = tag;
        this.fileIds = new HashSet<>();
    }

    public String getTag() {
        return tag;
    }

    public void removeFile(TaggableFile file) {
        fileIds.remove(file.getId());
        collectionSizeInBytes -= file.getFile().length();
    }

    public void addFile(TaggableFile file) {
        fileIds.add(file.getId());
        collectionSizeInBytes += file.getFile().length();
    }

    public Set<Long> getFileIds() {
        return fileIds;
    }

    public long size() {
        return collectionSizeInBytes;
    }

    public void addCollection(FileCollection fileCollection) {
        fileCollection.parentCollection = this;
        childCollections.add(fileCollection);
        collectionSizeInBytes += fileCollection.size();
    }

    public void removeCollection(FileCollection fileCollection) {
        childCollections.remove(fileCollection);
        collectionSizeInBytes -= fileCollection.size();
    }

    public Set<FileCollection> getChildCollections() {
        return childCollections;
    }

    public FileCollection getParentCollection() {
        return parentCollection;
    }

    // implement equals method and hashCode method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FileCollection that = (FileCollection) obj;
        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    @Override
    public int compareTo(FileCollection o) {
        return Long.compare(this.collectionSizeInBytes, o.size());
    }
}
