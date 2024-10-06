package vm.java.io.file;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FileCollectionFinderImpl implements FileCollectionFinder {

    private final FileCollectionStore fileCollectionStore;

    public FileCollectionFinderImpl(FileCollectionStore fileCollectionStore) {
        this.fileCollectionStore = fileCollectionStore;
    }

    @Override
    public List<TaggableFile> topNFilesBySize(int n) {
        return fileCollectionStore.topNTaggableFilesBySize(n);
    }

    @Override
    public List<FileCollection> topNCollectionBySize(int n) {
        return fileCollectionStore.topNCollectionsBySize(n);
    }

    @Override
    public List<FileCollection> collectionsContainsNoFiles() {
        List<FileCollection> collections = fileCollectionStore.listAllCollections();
        int length = collections.size();
        for(int i = length-1; i >= 0; i--) {
            if(collections.get(i).size() == 0) {
                collections.remove(i);
            } else {
                break;
            }
        }
        return collections;
    }

    @Override
    public Set<TaggableFile> filesWithNoTags() {
        return fileCollectionStore.retrieveFilesByTag("default");
    }

    @Override
    public Set<TaggableFile> filesByTag(String tag) {
        return fileCollectionStore.retrieveFilesByTag(tag);
    }

    @Override
    public Map<Long, List<FileCollection>> collectionsOfSameSize() {
        return fileCollectionStore.listAllCollections().stream().collect(Collectors.groupingBy(FileCollection::size));
    }

    @Override
    public Set<TaggableFile> filesByTags(Set<String> tags) {
        return tags.stream().map(fileCollectionStore::retrieveFilesByTag)
                .filter(Objects::nonNull).filter(s -> !s.isEmpty())
                .collect(Collectors.toSet()).stream().reduce((a, b) -> {
            a.addAll(b);
            return a;
        }).orElse(Set.of());
    }
}
