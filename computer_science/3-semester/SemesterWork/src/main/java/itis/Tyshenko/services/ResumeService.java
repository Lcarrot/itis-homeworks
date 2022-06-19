package itis.Tyshenko.services;

import itis.Tyshenko.dto.AdDTO;
import itis.Tyshenko.dto.ResumeDTO;

import java.util.List;
import java.util.Optional;

public interface ResumeService {

    List<ResumeDTO> getAll();
    void add(ResumeDTO resumeDTO, Long useId);
    Optional<ResumeDTO> getByUserId(Long UserId);
}
