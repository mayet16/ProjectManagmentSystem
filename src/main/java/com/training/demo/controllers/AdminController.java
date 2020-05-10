package com.training.demo.controllers;

import com.training.demo.dto.ArtifactDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.service.ArtifactService;
import com.training.demo.service.ProjectService;
import com.training.demo.service.TaskService;
import com.training.demo.service.WorkerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@Controller
public class AdminController {
    private final ArtifactService artifactService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final WorkerService workerService;

    public AdminController(ArtifactService artifactService, ProjectService projectService, TaskService taskService,
                           WorkerService workerService) {
        this.artifactService = artifactService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.workerService = workerService;
    }

    @RequestMapping({"/{id}", "/{id}/create_artifact"})
    public String testAdmin(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Worker worker,
                            @ModelAttribute("newArtifact") ArtifactDTO artifactDTO) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("tasks", taskService.findByProjectAndWorkers(project, worker));
        model.addAttribute("artifacts", artifactService.findArtifactsByProjectId(id));
        model.addAttribute("workers", workerService.findWorkersByProjectId(project));
        return "admin/projects";
    }

    @RequestMapping("/{project}/delete_artifact/{id}")
    public String deleteArtifact(Model model, @PathVariable("id") Long id,
                                 @PathVariable("project") Long projectId) {

        artifactService.deleteArtifact(id);
        return "redirect:/admin/{project}";
    }

    @PostMapping("/{project}/create_artifact")
    public String newArtifact(@ModelAttribute("newArtifact") ArtifactDTO artifactDTO, Model model,
                              @PathVariable("project") Long projectId) throws Exception {

        Project project = projectService.findProjectById(projectId);
        model.addAttribute("project", project);
        artifactService.addArtifact(artifactDTO, project);
        return "redirect:/admin/{project}";
    }

    @RequestMapping("/{project}/delete_worker/{id}")
    public String deleteWorkerFromProject(Model model, @PathVariable("id") Long id,
                                          @PathVariable("project") Long projectId) {

        projectService.deleteWorkerFromProject(projectId, id);
        return "redirect:/admin/{project}";
    }


}
