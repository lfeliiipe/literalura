package br.com.alura.literalura.model;

public enum Idioma {
    ES("Espanhol"),
    EN("Inglês"),
    FR("Francês"),
    PT("Português");

    private String idiomaCompleto;

    Idioma(String idiomaCompleto) {
        this.idiomaCompleto = idiomaCompleto;
    }

    public static String getCompleto(String text) {
        for (Idioma i : Idioma.values()) {
            if(i.toString().equalsIgnoreCase(text)){
                return i.idiomaCompleto;
            }
        }
        throw new IllegalArgumentException("Nenhum idioma encontrado para a string: "+ text);
    }
}
