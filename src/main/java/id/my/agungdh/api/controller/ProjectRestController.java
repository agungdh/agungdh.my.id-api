/* (C) 2025 Agung DH */
package id.my.agungdh.api.controller;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.service.ProjectService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectRestController {
  private ProjectService projectService;

  @GetMapping
  public ResponseEntity<List<ProjectDTO>> index() {
    return ResponseEntity.ok(projectService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDTO> get(@PathVariable UUID id) {
    return ResponseEntity.ok(projectService.getProject(id));
  }
}
