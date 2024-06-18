package com.maxip.codeupload.persistence.repository.springdatajpa;

import com.maxip.codeupload.persistence.entity.Algorithm;
import com.maxip.codeupload.persistence.entity.AlgorithmSubStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlgorithmSubStepRepository extends JpaRepository<AlgorithmSubStep, Long>
{
    public AlgorithmSubStep findByAlgorithmAndStepNumberAndDescription(Algorithm algorithm, Long stepNumber, String description);
}
