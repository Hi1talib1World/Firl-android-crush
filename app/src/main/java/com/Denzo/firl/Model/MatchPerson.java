package com.Denzo.firl.Model;

public class MatchPerson {
    private final int id;
    private final String name;
    private String numberOfPersons;
    private final int imageResource;

    public MatchPerson(int id, String name, String numberOfPersons, int imageResource) {
        this.id = id;
        this.name = name;
        this.numberOfPersons = numberOfPersons;
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return "MatchPerson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfPersons='" + numberOfPersons + '\'' +
                ", imageResource=" + imageResource +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumberOfPersons() {
        return numberOfPersons;
    }

    public int getImageResource() {
        return imageResource;
    }
}
