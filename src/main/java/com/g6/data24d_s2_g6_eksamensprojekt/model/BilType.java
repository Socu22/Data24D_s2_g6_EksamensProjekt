package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class BilType
{
    private final int    bilType_Id;
    private String maerke, model, udstyr;
    private double staalPris, afgift, udledningCo2;

    public BilType(int bilType_Id){this.bilType_Id = bilType_Id;}

    public BilType(int bilType_Id, String maerke, String model, String udstyr, double staalPris, double afgift,
                   double udledningCo2)
    {
        this.bilType_Id    = bilType_Id;
        this.maerke       = maerke;
        this.model        = model;
        this.udstyr       = udstyr;
        this.staalPris    = staalPris;
        this.afgift       = afgift;
        this.udledningCo2 = udledningCo2;
    }


    public int getBilType_Id()
    {
        return bilType_Id;
    }
    public String getMaerke()
    {
        return maerke;
    }
    public String getModel()
    {
        return model;
    }
    public String getUdstyr()
    {
        return udstyr;
    }
    public double getStaalPris()
    {
        return staalPris;
    }
    public double getAfgift()
    {
        return afgift;
    }
    public double getUdledningCo2()
    {
        return udledningCo2;
    }

    public void setMaerke(String maerke)
    {
        this.maerke = maerke;
    }
    public void setModel(String model)
    {
        this.model = model;
    }
    public void setUdstyr(String udstyr)
    {
        this.udstyr = udstyr;
    }
    public void setStaalPris(double staalPris)
    {
        this.staalPris = staalPris;
    }
    public void setAfgift(double afgift)
    {
        this.afgift = afgift;
    }
    public void setUdledningCo2(double udledningCo2)
    {
        this.udledningCo2 = udledningCo2;
    }
}
