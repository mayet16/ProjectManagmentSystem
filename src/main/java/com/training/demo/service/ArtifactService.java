package com.training.demo.service;

import com.training.demo.controllers.exception.CanNotFoundException;
import com.training.demo.controllers.exception.DeleteException;
import com.training.demo.dto.ArtifactDTO;
import com.training.demo.entity.Artifact;
import com.training.demo.entity.Project;
import com.training.demo.repository.ArtifactRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArtifactService {
    private final ArtifactRepository artifactRepository;
    private final ProjectService projectService;

    public ArtifactService(ArtifactRepository artifactRepository, ProjectService projectService) {
        this.artifactRepository = artifactRepository;
        this.projectService = projectService;
    }

    public void addArtifact(ArtifactDTO artifact, Project project) throws Exception {

        Artifact updatedArtifact = Artifact.builder()
                .name(artifact.getName())
                .project(project)
                .type(artifact.getType())
                .content(artifact.getContent())
                .build();
        try {
            artifactRepository.save(updatedArtifact);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Помилка додавання артефакту");
        }
    }

    public void deleteArtifact(Artifact artifact) throws DeleteException {

        try {
            artifactRepository.delete(artifact);
        } catch (DataIntegrityViolationException e) {
            throw new DeleteException("Помилка видалення артефакту");
        }
    }


    public List<Artifact> findArtifactsByProjectId(Long id) throws CanNotFoundException {

        Project project = projectService.findProjectById(id);
        return artifactRepository.findByProject(project);
    }

    public void deleteArtifact(Long id) {

        Artifact artifact = artifactRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("no artifact"));

        artifactRepository.delete(artifact);
    }

}
