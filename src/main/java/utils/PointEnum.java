package main.java.utils;

import javafx.scene.paint.Color;

public enum PointEnum {
    RULER_1 ("Ruler1","D1",-25,-10,Color.ORANGE),
    RULER_2 ("Ruler2","D2",-25,-10,Color.ORANGE),
    NASION ("Nasion","N",-18,-20,Color.RED),
    SELLA ("Sella","S",-8,-20,Color.RED),
    ORBITALE ("Orbitale","Or",-8,-20,Color.RED),
    BASION ("Basion","Ba",-20,-5,Color.RED),
    PORION ("Porion","Po",-18,-20,Color.RED),
    ANS ("ANS","ANS",-8,-20,Color.RED),
    PNS ("PNS","PNS",-8,-20,Color.RED),
    A_POINT ("A point","A",5,-15,Color.RED),
    B_POINT ("B point","B",10,-10,Color.RED),
    POG ("Pogonion","Pog",8,-10,Color.RED),
    GNATHION ("Gnathion","Gn",8,3,Color.RED),
    MENTON ("Menton","Me",-8,8,Color.RED),
    GONION ("Gonion","Go",-23,-5,Color.RED),
    RAMUS_POINT ("Ramus point","R",-15,-8,Color.RED),
    ARTICULARE ("Articulare","Ar",-19,0,Color.RED),
    PT ("PT point","Pt",-8,-20,Color.RED), //17

    UPPER_INCISOR_CROWN_TIP ("Upper Incisor crown tip", Color.ORANGE),
    UPPER_INCISOR_GINGIVAL_BORDER ("Upper Incisor gingival border",Color.ORANGE),
    UPPER_INCISOR_ROOT_TIP ("Upper Incisor root tip",Color.ORANGE),
    LOWER_INCISOR_CROWN_TIP ("Lower Incisor crown tip",Color.ORANGE),
    LOWER_INCISOR_GINGIVAL_BORDER ("Lower Incisor gingival border",Color.ORANGE),
    LOWER_INCISOR_ROOT_TIP ("Lower Incisor root tip",Color.ORANGE),
    OCCLUSAL_PLANE_INCISOR_EDGE("Midpoint of U1 Tip - L1 Tip",Color.BLACK),//24  //renk farklı

    UPPER_MOLAR_DISTAL_SURFACE ("Upper Molar Distal Surface",Color.ORANGE),
    UPPER_MOLAR_DISTAL_ROOT_TIP ("Upper Molar Distal Root Tip",Color.ORANGE),
    UPPER_MOLAR_MESIAL_TIP("Upper Molar Mesial Cusp Tip",Color.ORANGE),//27

    LOWER_MOLAR_DISTAL_SURFACE ("Lower Molar Distal Surface",Color.ORANGE),
    LOWER_MOLAR_DISTAL_ROOT_TIP ("Lower Molar Distal Root Tip",Color.ORANGE),
    LOWER_MOLAR_MESIAL_TIP("Lower Molar Mesial Cusp Tip",Color.ORANGE),  //30

    ST_GLABELLA("Soft Tissue Glabella",Color.WHITE),
    ST_NASION("Soft Tissue Nasion",Color.WHITE),
    BRIDGE_OF_NOSE("Nose bridge",Color.WHITE),
    TIP_OF_NOSE("Tip of Nose",Color.WHITE),
    MIDPOINT_OF_COLUMELLA("Midpoint of Columella",Color.WHITE),
    SUBNASALE("Subnasale",Color.WHITE),
    ST_A_POINT("Soft Tissue A point",Color.WHITE),
    UPPER_LIP("Most forward position of upper Lip",Color.WHITE),
    STOMION_SUPERIUS ("Bottommost position of upper lip",Color.WHITE),//39

    STOMION_INFERIUS ("Topmost position of lower lip",Color.WHITE),
    LOWER_LIP("Most forward position of lower Lip",Color.WHITE),
    ST_B_POINT("Soft Tissue B point",Color.WHITE),
    ST_POGONION("Soft Tissue Pogonion",Color.WHITE),
    ST_GNATHION("Soft Tissue Gnathion",Color.WHITE),
    ST_MENTON("Soft Tissue Menton",Color.WHITE),//45
    CONTROL_BUTTON("Finished",Color.WHITE);

    private String pointType;
    private String abbreviation;
    private int xOffset = 0;
    private  int yOffset = 0;
    private Color color;

    PointEnum(String pointType,Color color){     // Bazı Pointlerde Sadece yazı ve renk olacak
        this.pointType = pointType;
        this.color = color;
    }

    PointEnum(String pointType, String abbreviation, int xOffset, int yOffset, Color color) {   //Bazısında Hepsi olacak
        this.pointType = pointType;
        this.abbreviation = abbreviation;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.color = color;
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

    public int getOffsetX() {
        return xOffset;
    }

    public int getOffsetY() {
        return yOffset;
    }

    public Color getColor() {
        return color;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getPointType() {
        return pointType;
    }
}
