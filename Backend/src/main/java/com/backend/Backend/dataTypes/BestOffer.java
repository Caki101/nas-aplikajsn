package com.backend.Backend.dataTypes;

public class BestOffer {
    String description;
    String label;
    String img_link;

    public BestOffer(String description, String label, String img_link) {
        this.description = description;
        this.label = label;
        this.img_link = img_link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }
}
