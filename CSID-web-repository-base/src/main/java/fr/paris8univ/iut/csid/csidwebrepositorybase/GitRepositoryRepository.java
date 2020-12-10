package fr.paris8univ.iut.csid.csidwebrepositorybase;

import org.apache.tomcat.jni.Time;
import org.hibernate.query.criteria.internal.expression.function.CurrentTimeFunction;
import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GitRepositoryRepository {
    private final GitRepositoryDao gitRepositoryDao;
    private final GithubRepositoryDAO githubRepositoyDAO;

    //private final List<GitRepository> repositories = List.of(new GitRepository("test", "jule",5,5),
    //new GitRepository("machin", "jean",5,5));

    public GitRepositoryRepository(GitRepositoryDao gitRepositoryDao, GithubRepositoryDAO githubRepositoryDAO) {

        this.gitRepositoryDao=gitRepositoryDao;
        this.githubRepositoyDAO=githubRepositoryDAO;
    }

    public List<GitRepository> getRepositories(){
        List<GitRepositoryEntity> repositoryEntities = gitRepositoryDao.findAll();
        return repositoryEntities.stream()
                .map(x -> new GitRepository(x.getName(),x.getOwner(),x.getIssues(),x.getForks(),x.getTime()))
                .collect(Collectors.toList());
    }

    public Optional<GitRepository> findOneRepository(String name) throws URISyntaxException {
        GitRepositoryEntity myGitRepoEntity = gitRepositoryDao.findById(name).get();
        GitRepository newGitRepoository = new GitRepository(myGitRepoEntity.getName(), myGitRepoEntity.getOwner(),myGitRepoEntity.getIssues(),myGitRepoEntity.getForks(), myGitRepoEntity.getTime());

        if ((Instant.now().getEpochSecond()-newGitRepoository.getTime())>500) {
            GitRepositoryDTO myGitRepoDTO;
            myGitRepoDTO = githubRepositoyDAO.GetGitRepositoryDTO(newGitRepoository.getName(), newGitRepoository.getOwner());
            newGitRepoository.setIssues(myGitRepoDTO.getOpen_issues());
            newGitRepoository.setForks(myGitRepoDTO.getForks());
            LocalTime lt = LocalTime.now();
            newGitRepoository.setTime((Instant.now().getEpochSecond()));
            patchRepository(newGitRepoository.getName(), newGitRepoository);
        }
        return Optional.of(newGitRepoository);
    }

    public void creatRepository(GitRepository gitRepository){
        gitRepositoryDao.save(new GitRepositoryEntity(gitRepository.getName(),gitRepository.getOwner(),gitRepository.getIssues(),gitRepository.getForks(),gitRepository.getTime()));
    }

    public void putRepository(String name,GitRepository gitRepository) {
        Optional<GitRepositoryEntity> gitRepositories =  gitRepositoryDao.findById(name);
        if (gitRepositories.isEmpty()){
            creatRepository(gitRepository);
        }
        else{
            GitRepositoryEntity setRepository = gitRepositories.get();
            setRepository.setOwner(gitRepository.getOwner());
            setRepository.setIssues(gitRepository.getIssues());
            setRepository.setForks(gitRepository.getForks());
            setRepository.setTime(gitRepository.getTime());
            gitRepositoryDao.save(setRepository);
        }
    }

    public void patchRepository(String name, GitRepository gitRepository) {
        Optional<GitRepositoryEntity> repository = gitRepositoryDao.findById(name);
        GitRepositoryEntity repositoryModified = repository.get();

        if(gitRepository.getOwner() != null)
            repositoryModified.setOwner(gitRepository.getOwner());
        if(gitRepository.getIssues() != 0 )
            repositoryModified.setIssues(gitRepository.getIssues());
        if(gitRepository.getForks() != 0)
            repositoryModified.setForks(gitRepository.getForks());

        gitRepositoryDao.save(repositoryModified);
    }

    public void deleteRepository(String name) {
        gitRepositoryDao.deleteById(name);
    }

}

