package dev.sassine.simpleopenia.model;

public enum SizeImage {
    P("256x256"),
    M("512x512"),
    G("1024x1024");

    private final String size;

    SizeImage(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}