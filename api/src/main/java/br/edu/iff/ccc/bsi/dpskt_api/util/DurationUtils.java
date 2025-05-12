
package br.edu.iff.ccc.bsi.dpskt_api.util;

import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component("durationUtils")
public class DurationUtils {

  public String formatDistinct(long minutes) {
    if (minutes >= 60) {
      long hours = minutes / 60;
      long remainingMinutes = minutes % 60;

      if (remainingMinutes > 0) {
        return String.format("%dh %dmin", hours, remainingMinutes);
      }

      return String.format("%d horas", hours);
    }

    if (minutes < 1) {
      return "Alguns segundos";
    }

    if (minutes == 1) {
      return "1 minuto";
    }

    return String.format("%d minutos", minutes);
  }

  public String formatDuration(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null)
      return "Em andamento...";

    Duration duration = Duration.between(start, end);
    long minutes = duration.toMinutes();
    return formatDistinct(minutes);
  }
}
