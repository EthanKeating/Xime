package club.mcgamer.xime.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MathUtil {

    public static List<Integer> distributeObjects(int totalPoints, int numObjects) {
        List<Integer> positions = new ArrayList<>();

        double step = (double) totalPoints / numObjects;
        for (int i = 0; i < numObjects; i++) {
            int position = (int) Math.round(i * step) % totalPoints;
            positions.add(position);
        }

        return positions;
    }

    public static double calculateAngle(double x1, double y1, double x2, double y2) {
        double dotProduct = x1 * x2 + y1 * y2;

        double magnitudeA = Math.sqrt(x1 * x1 + y1 * y1);
        double magnitudeB = Math.sqrt(x2 * x2 + y2 * y2);

        double cosTheta = dotProduct / (magnitudeA * magnitudeB);

        cosTheta = Math.max(-1.0, Math.min(1.0, cosTheta));

        double angleRadians = Math.acos(cosTheta);

        return Math.toDegrees(angleRadians);
    }

    public static Location lookAt(Location source, Location target) {
        double dx = target.getX()- 0.5 - source.getX();
        double dz = target.getZ()- 0.5 - source.getZ();

        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz)); // Bukkit uses negative X for yaw
        yaw = (yaw + 360) % 360;

        Location result = source.clone();
        result.setYaw(yaw);

        return result;
    }

    public static double calculateXZDistance(Location loc1, Location loc2) {

        double deltaX = loc1.getX() - loc2.getX();
        double deltaZ = loc1.getZ() - loc2.getZ();

        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

}
