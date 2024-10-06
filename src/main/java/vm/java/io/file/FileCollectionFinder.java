package vm.java.io.file;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FileCollectionFinder {
    /**
     * Returns the top N files by size
     * @param int n
     * @return List<TaggableFile>
     */
    List<TaggableFile> topNFilesBySize(int n);

    /**
     * Returns the top N collections by size
     * @param int n
     * @return List<FileCollection>
     */
    List<FileCollection> topNCollectionBySize(int n);

    /**
     * Returns all collections that contain no files
     * @return Set<FileCollection>
     */
    List<FileCollection> collectionsContainsNoFiles();

    /**
     * Returns all files that have no tags
     * @return Set<TaggableFile>
     */
    Set<TaggableFile> filesWithNoTags();

    /**
     * Returns all files that have a specific tag
     * @param String tag
     * @return Set <TaggableFile>
     */
    Set<TaggableFile> filesByTag(String tag);

    /**
     * Returns all collections that have the same size
     * @return Map<Long, FileCollection>
     */
    Map<Long, List<FileCollection>> collectionsOfSameSize();

    /**
     * Returns all files that have the same size
     * @return Map<Long, TaggableFile>
     */
    Set<TaggableFile> filesByTags(Set<String> tags);
}
