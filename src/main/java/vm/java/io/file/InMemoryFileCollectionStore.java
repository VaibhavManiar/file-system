package vm.java.io.file;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryFileCollectionStore implements FileCollectionStore {

    private final Set<TaggableFile> sortedFilesBySize;
    private final Map<Long, TaggableFile> idToFile = new ConcurrentHashMap<>();
    private final Map<String, Set<TaggableFile>> tagToFile = new ConcurrentHashMap<>();

    private final Map<String, FileCollection> tagToFileCollection = new ConcurrentHashMap<>();
    private final Set<FileCollection> collections;

    public InMemoryFileCollectionStore() {
        this.collections = new TreeSet<>(Comparator.comparing(FileCollection::size));
        this.sortedFilesBySize = new TreeSet<>(Comparator.comparing(TaggableFile::size));
    }

    @Override
    public void store(TaggableFile file) {
        sortedFilesBySize.add(file);
        idToFile.put(file.getId(), file);
        this.addFileToCollection(file);
    }

    private void addFileToCollection(TaggableFile file) {
        if(file.getTags().isEmpty()) {
            FileCollection defaultCollection = FileCollection.defaultCollection();
            defaultCollection.addFile(file);
            tagToFile.computeIfAbsent(defaultCollection.getTag(), k -> new HashSet<>()).add(file);
            collections.add(defaultCollection);
        } else {
            file.getTags().forEach(tag -> {
                FileCollection collection = tagToFileCollection.get(tag);
                if (collection == null) {
                    collection = new FileCollection(tag);
                    tagToFile.computeIfAbsent(tag, k -> new HashSet<>()).add(file);
                    tagToFileCollection.put(tag, collection);
                    collections.add(collection);
                }
                collection.addFile(file);
            });
        }
    }

    @Override
    public void delete(Long id) {
        TaggableFile file = this.retrieveFileById(id);
        if(file != null) {
            this.collections.forEach(collection -> collection.removeFile(file));
            file.getTags().forEach(tag -> this.tagToFile.get(tag).remove(file));
            this.sortedFilesBySize.remove(file);
        }
    }

    @Override
    public TaggableFile retrieveFileByName(String name) {
        return this.sortedFilesBySize.stream().filter(file -> file.getFile().getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public TaggableFile retrieveFileById(long id) {
        return this.idToFile.get(id);
    }

    @Override
    public Set<TaggableFile> retrieveAllFiles() {
        return new HashSet<>(this.sortedFilesBySize);
    }

    @Override
    public Set<TaggableFile> retrieveFilesByTag(String tag) {
        return this.tagToFile.get(tag);
    }

    @Override
    public List<FileCollection> topNCollectionsBySize(int n) {
        return this.collections.stream().limit(n).collect(Collectors.toList());
    }

    @Override
    public List<TaggableFile> topNTaggableFilesBySize(int n) {
        return this.sortedFilesBySize.stream().limit(n).collect(Collectors.toList());
    }

    @Override
    public List<FileCollection> listAllCollections() {
        return new ArrayList<>(this.collections);
    }

}