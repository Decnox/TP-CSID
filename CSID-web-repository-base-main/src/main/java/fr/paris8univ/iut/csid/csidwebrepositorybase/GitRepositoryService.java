package fr.paris8univ.iut.csid.csidwebrepositorybase;



import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Optional;

@Service
public class GitRepositoryService {


    private final List<GitRepository> repositories = List.of(new GitRepository("name1", "owner"),
            new GitRepository("name2", "owner2"));


    public List<GitRepository> getRepositories(){
        return repositories;

    }


    public Optional<GitRepository> findOneRepository(String name){
        return repositories.stream()
                .filter(x -> x.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
