package vm.java.io.file;

import java.util.List;
import java.util.Set;

public interface FileCollectionStore {
    void store(TaggableFile file);
    void delete(Long id);
    TaggableFile retrieveFileByName(String name);
    TaggableFile retrieveFileById(long id);
    Set<TaggableFile> retrieveAllFiles();
    Set<TaggableFile> retrieveFilesByTag(String tag);
    List<FileCollection> topNCollectionsBySize(int n);
    List<TaggableFile> topNTaggableFilesBySize(int n);
    List<FileCollection> listAllCollections();
}