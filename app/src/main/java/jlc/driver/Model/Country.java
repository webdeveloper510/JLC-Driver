package jlc.driver.Model;

public class Country {


    String id,name,phonecode;



    public Country() {

    }


    public Country(String id,String name,String phonecode) {

        this.id = id;
        this.name = name;
        this.phonecode =phonecode;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }
}
