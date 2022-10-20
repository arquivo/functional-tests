package pt.arquivo.tests.webapp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

public class DateUtils {

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDateFromTimestamp(String timestamp) {
		return DateUtils
				.asDate(LocalDateTime.of(fromTimestamp(0, 4).apply(timestamp), fromTimestamp(4, 6).apply(timestamp),
						fromTimestamp(6, 8).apply(timestamp), fromTimestamp(8, 10).apply(timestamp),
						fromTimestamp(10, 12).apply(timestamp), fromTimestamp(12, 14).apply(timestamp)));
	}

	private static Function<String, Integer> fromTimestamp(int from, int to) {
		return new Function<String, Integer>() {
			@Override
			public Integer apply(String timestamp) {
				if (from > timestamp.length()) {
					return 0;
				}
				if (to > timestamp.length()) {
					return 0;
				}
				return Integer.valueOf(timestamp.substring(from, to));
			}
		};
	}
}
