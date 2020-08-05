package com.remixtrainer;

public class FitnessEquipment {
    public String description;
    public String code;

    public FitnessEquipment()
    {
        description = "";
        code = "";
    }

    public FitnessEquipment(String newDescription)
    {
        description = newDescription;
        code = newDescription.substring(0, 2);
    }

    public FitnessEquipment(String newDescription, String newCode)
    {
        description = newDescription;
        code = newCode;
    }
}
