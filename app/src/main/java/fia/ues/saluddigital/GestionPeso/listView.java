package fia.ues.saluddigital.GestionPeso;

public class listView {
    public String color;
    public int iconResourceId;
    public String name;
    public String description;
    public String status;

    public listView(String color, int iconResourceId, String name, String description, String status) {
        this.color = color;
        this.iconResourceId = iconResourceId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

