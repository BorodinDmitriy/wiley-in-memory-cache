package huawei_nlp;

public class Cell {
    Integer id;
    String key;
    Integer toxicity;
    Boolean fixed;

    public Cell() {
        this.id = -1;
        this.key = "";
        this.toxicity = 0;
        this.fixed = false;
    }

    public Cell(Integer id, String string) {
        this.id = id;
        this.key = string;
        this.toxicity = 0;
        this.fixed = false;
    }

    public Cell(Integer id, String string, Integer toxicity) {
        this.id = id;
        this.key = string;
        if (toxicity > 0) {
            this.toxicity = toxicity;
            this.fixed = true;
        } else {
            this.toxicity = 0;
            this.fixed = false;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getToxicity() {
        return toxicity;
    }

    public void setToxicity(Integer toxicity) {
        this.toxicity = toxicity;
    }

    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }
}
