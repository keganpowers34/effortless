package dev.huskuraft.effortless.api.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.text.Text;

public interface Entity extends PlatformReference {

    UUID getId();

    boolean isDeadOrDying();

    Client getClient();

    Server getServer();

    World getWorld();

    Text getDisplayName();

    Vector3d getPosition();

    default Vector3d getEyePosition() {
        return getPosition().withY(getPosition().y() + getEyeHeight());
    }

    default Vector3d getEyeDirection() {
        return calculateViewVector(getXRot(), getYRot());
    }

    GameMode getGameMode();

    BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids);

    static Vector3d calculateViewVector(float xRot, float yRot) {
        var f = xRot * ((float)Math.PI / 180F);
        var f1 = -yRot * ((float)Math.PI / 180F);
        var f2 = MathUtils.cos(f1);
        var f3 = MathUtils.sin(f1);
        var f4 = MathUtils.cos(f);
        var f5 = MathUtils.sin(f);
        return new Vector3d(f3 * f4, -f5, f2 * f4);
    }

    float getEyeHeight();

    void setPosition(Vector3d position);

    float getXRot();

    float getYRot();

    void setXRot(float xRot);

    void setYRot(float yRot);

}
