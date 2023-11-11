package entity;

public class Preferences {
    private boolean showBirthday;
    private boolean showFullName;
    private boolean showGender;

    public boolean showBirthday() {
        return showBirthday;
    }

    public void setBirthdayOption(boolean showBirthday) {
        this.showBirthday = showBirthday;
    }

    public boolean showFullName() {
        return showFullName;
    }

    public void setFullNameOption(boolean showFullName) {
        this.showFullName = showFullName;
    }

    public boolean showGender() {
        return showGender;
    }

    public void setGenderOption(boolean showGender) {
        this.showGender = showGender;
    }
}
