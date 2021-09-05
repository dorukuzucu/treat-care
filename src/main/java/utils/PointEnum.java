package main.java.utils;

public enum PointEnum {
    RULER_1 ("Ruler 1"),
    RULER_2 ("Ruler2"),
    NASION ("Nasion"),
    SELLA ("Sella"),
    ORBITALE ("Orbitale"),
    BASION ("Basion"),
    PORION ("Porion"),
    ANS ("ANS"),
    PNS ("PNS"),
    A_POINT ("A point"),
    B_POINT ("B point"),
    POG ("Pog"),
    GNATHION ("Gnathion"),
    MENTON ("Menton"),
    GONION ("Gonion"),
    RAMUS_POINT ("Ramus point"),
    ARTICULARE ("Articulare"),
    PTM ("Ptm"),
    UPPER_INCISOR_CROWN_TIP ("Upper Incisor crown tip"),
    UPPER_INCISOR_ROOT_TIP ("Upper Incisor root tip"),
    LOWER_INCISOR_CROWN_TIP ("Lower Incisor crown tip"),
    LOWER_INCISOR_ROOT_TIP ("Lower Incisor root tip"),
    UPPER_MOLAR_CROWN_TIP ("Upper Molar Crown tip "),
    UPPER_MOLAR_ROOT_TIP ("Upper Molar root tip"),
    LOWER_MOLAR_CROWN_TIP ("Lower Molar Crown tip "),
    LOWER_MOLAR_ROOT_TIP ("Lower Molar root tip");

    private String pointType;

    PointEnum(String s) {
        this.pointType = s;
    }

    public static PointEnum fromString(String s){
        for (PointEnum pointEnum : PointEnum.values()) {
            if (pointEnum.toString().equalsIgnoreCase(s)) {
                return pointEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return pointType;
    }
}
