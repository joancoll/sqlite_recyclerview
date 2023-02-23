package cat.dam.andy.sqlite_recyclerview;


public class Item {
    private	int	id;
    private	String nom;
    private	String tel;

    public Item(String nom, String tel) {
        this.nom = nom;
        this.tel = tel;
    }

    public Item(int id, String nom, String tel) {
        this.id = id;
        this.nom = nom;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
