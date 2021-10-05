package main.java.utils;

public enum PointEnum {
    RULER_1 ("Ruler1"),
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
    POG ("Pogonion"),
    GNATHION ("Gnathion"),
    MENTON ("Menton"),
    GONION ("Gonion"),
    RAMUS_POINT ("Ramus point"),
    ARTICULARE ("Articulare"),
    PT ("PT point"), //17

    UPPER_INCISOR_CROWN_TIP ("Upper Incisor crown tip"),
    UPPER_INCISOR_GINGIVAL_BORDER ("Upper Incisor gingival border"),
    UPPER_INCISOR_ROOT_TIP ("Upper Incisor root tip"),
    LOWER_INCISOR_CROWN_TIP ("Lower Incisor crown tip"),
    LOWER_INCISOR_GINGIVAL_BORDER ("Lower Incisor gingival border"),
    LOWER_INCISOR_ROOT_TIP ("Lower Incisor root tip"),
    OCCLUSAL_PLANE_INCISOR_EDGE("Midpoint of U1 Tip - L1 Tip"),//24

    UPPER_MOLAR_DISTAL_SURFACE ("Upper Molar Distal Surface"),
    UPPER_MOLAR_DISTAL_ROOT_TIP ("Upper Molar Distal Root Tip"),
    UPPER_MOLAR_MESIAL_TIP("Upper Molar Mesial Cusp Tip"),//27

    LOWER_MOLAR_DISTAL_SURFACE ("Lower Molar Distal Surface"),
    LOWER_MOLAR_DISTAL_ROOT_TIP ("Lower Molar Distal Root Tip"),
    LOWER_MOLAR_MESIAL_TIP("Lower Molar Mesial Cusp Tip"),  //30

    ST_GLABELLA("Soft Tissue Glabella"),
    ST_NASION("Soft Tissue Nasion"),
    BRIDGE_OF_NOSE("Bridge of Nose"),
    TIP_OF_NOSE("Tip of Nose"),
    MIDPOINT_OF_COLUMELLA("Midpoint of Columella"),
    SUBNASALE("Subnasale"),
    ST_A_POINT("Soft Tissue A point"),
    UPPER_LIP("Most forward position of upper Lip"),
    STOMION_SUPERIUS ("Bottommost position of upper lip"),//39

    STOMION_INFERIUS ("Topmost position of lower lip"),
    LOWER_LIP("Most forward position of lower Lip"),
    ST_B_POINT("Soft Tissue B point"),
    ST_POGONION("Soft Tissue Pogonion"),
    ST_GNATHION("Soft Tissue Gnathion"),
    ST_MENTON("Soft Tissue Menton"),//45
    CONTROL_BUTTON("Finished");

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
