package com.maxip.filestore.repository;

import com.maxip.filestore.entity.File;
import com.maxip.filestore.entity.HintFile;
import com.maxip.filestore.entity.SearchHint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HintFileRepo extends JpaRepository<HintFile, Long>
{
    HintFile findByFileAndSearchHintAndPageNumber(File file, SearchHint searchHint, int pageNumber);
}
