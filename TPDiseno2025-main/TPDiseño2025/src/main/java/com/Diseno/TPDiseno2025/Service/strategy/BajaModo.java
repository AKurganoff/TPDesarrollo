package com.Diseno.TPDiseno2025.Service.strategy;

public enum BajaModo {
    AUTO, LOGICA, FISICA;

    public static BajaModo from(String s) {
        if (s == null) return AUTO;
        try {
            return BajaModo.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return AUTO;
        }
    }
}
