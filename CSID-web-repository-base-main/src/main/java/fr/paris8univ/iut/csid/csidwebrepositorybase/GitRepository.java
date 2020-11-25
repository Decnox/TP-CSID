package fr.paris8univ.iut.csid.csidwebrepositorybase;

public class GitRepository {

    private String name;
    private String owner;

    public GitRepository(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
