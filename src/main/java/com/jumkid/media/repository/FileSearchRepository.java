package com.jumkid.media.repository;

import com.jumkid.media.model.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Created at Sep2018$
 *
 * @author chooliyip
 **/
public interface FileSearchRepository extends ElasticsearchRepository<MediaFile, String> {

    /**
     * find file by name
     *
     * @param filename
     * @return
     */
    MediaFile findByFilenameAndModule(String filename, String module);

    /**
     * Search files by name
     *
     * @param keyword
     * @return
     */
    Page<MediaFile> findByFilenameContainingAndModule(String keyword, String module, Pageable pager);

    /**
     * fetch all files
     *
     * @param module
     * @param pager
     * @return
     */
    Page<MediaFile> findByModule(String module, Pageable pager);

}
