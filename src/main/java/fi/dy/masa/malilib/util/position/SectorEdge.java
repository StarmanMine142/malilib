package fi.dy.masa.malilib.util.position;

import org.jetbrains.annotations.ApiStatus;

import fi.dy.masa.malilib.util.MathUtils;

/**
 * Post-ReWrite code
 */
@ApiStatus.Experimental
public enum SectorEdge
{
    START_ANGLE (false),
    END_ANGLE   (false),
    INNER_RING  (true),
    OUTER_RING  (true);

    private final boolean isRadius;

    SectorEdge(boolean isRadius)
    {
        this.isRadius = isRadius;
    }

    public boolean isRadius()
    {
        return this.isRadius;
    }

    public static SectorEdge getClosestEdge(int mouseX, int mouseY,
                                            double centerX, double centerY,
                                            double innerRadius, double outerRadius,
                                            double startAngle, double endAngle)
    {
        double line1X1 = centerX + innerRadius * Math.cos(startAngle);
        double line1Y1 = centerY + innerRadius * Math.sin(startAngle);
        double line1X2 = centerX + outerRadius * Math.cos(startAngle);
        double line1Y2 = centerY + outerRadius * Math.sin(startAngle);
        double line2X1 = centerX + innerRadius * Math.cos(endAngle);
        double line2Y1 = centerY + innerRadius * Math.sin(endAngle);
        double line2X2 = centerX + outerRadius * Math.cos(endAngle);
        double line2Y2 = centerY + outerRadius * Math.sin(endAngle);

        double distStart = MathUtils.distanceFromPointToLine(mouseX, mouseY, line1X1, line1Y1, line1X2, line1Y2);
        double distEnd   = MathUtils.distanceFromPointToLine(mouseX, mouseY, line2X1, line2Y1, line2X2, line2Y2);
        double distMouse = Math.sqrt((mouseX - centerX) * (mouseX - centerX) +
                                             (mouseY - centerY) * (mouseY - centerY));
        double distInner = Math.abs(distMouse - innerRadius);
        double distOuter = Math.abs(distMouse - outerRadius);

        if (distStart < distEnd)
        {
            if (distInner < distOuter)
            {
                return distInner < distStart ? SectorEdge.INNER_RING : SectorEdge.START_ANGLE;
            }
            else
            {
                return distOuter < distStart ? SectorEdge.OUTER_RING : SectorEdge.START_ANGLE;
            }
        }
        else
        {
            if (distInner < distOuter)
            {
                return distInner < distEnd ? SectorEdge.INNER_RING : SectorEdge.END_ANGLE;
            }
            else
            {
                return distOuter < distEnd ? SectorEdge.OUTER_RING : SectorEdge.END_ANGLE;
            }
        }
    }
}
