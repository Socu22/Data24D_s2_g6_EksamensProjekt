package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class BilType
{
    private final int    bilTypeId;
    private final String maerke, model, udstyr;
    private final double staalPris, afgift, udledningCo2;

    public BilType(int bilTypeId, String maerke, String model, String udstyr, double staalPris, double afgift,
                   double udledningCo2)
    {
        this.bilTypeId    = bilTypeId;
        this.maerke       = maerke;
        this.model        = model;
        this.udstyr       = udstyr;
        this.staalPris    = staalPris;
        this.afgift       = afgift;
        this.udledningCo2 = udledningCo2;
    }

    public int getBilTypeId()
    {
        return bilTypeId;
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
}
