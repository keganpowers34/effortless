package dev.huskuraft.effortless.building.structure;

public enum PlaneFilling implements BuildFeature {
    PLANE_FULL("plane_full"),
    PLANE_HOLLOW("plane_hollow");

    private final String name;

    PlaneFilling(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BuildFeatures getType() {
        return BuildFeatures.PLANE_FILLING;
    }
}
