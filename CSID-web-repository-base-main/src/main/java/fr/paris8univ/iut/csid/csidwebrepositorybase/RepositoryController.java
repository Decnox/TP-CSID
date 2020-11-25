package fr.paris8univ.iut.csid.csidwebrepositorybase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repositories")


public class RepositoryController {

    private final GitRepositoryService repositoryService;

    @Autowired
    public RepositoryController(GitRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }


    @GetMapping
    public List<GitRepository> getRepositories(){
        return repositoryService.getRepositories();

    }

    @GetMapping("/{name}")
    public ResponseEntity<GitRepository> findOneRepository(@PathVariable String name){
        return repositoryService.findOneRepository(name)
                .map(x->ResponseEntity.ok(x))
                .orElse(ResponseEntity.notFound().build());
    }
}
