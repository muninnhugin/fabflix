public class Star {
    String id = "";
    String name = "";
    int birthYear;

    Star(String newId, String newName, int newBirthYear)
    {
        id = newId;
        name = newName;
        birthYear = newBirthYear;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getBirthYear() { return birthYear; }

}
