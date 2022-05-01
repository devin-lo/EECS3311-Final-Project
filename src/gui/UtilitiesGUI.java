package gui;

public class UtilitiesGUI {
	public static int convertTimeToInt(String timeStr) {
		int time = 0;
		
		int hour = Integer.parseInt(timeStr.substring(0,timeStr.indexOf(':')));
		int min = Integer.parseInt(timeStr.substring(timeStr.indexOf(':') + 1, timeStr.indexOf(' ')));
		int modifier = 0;
		if (timeStr.indexOf('a') == -1 && hour != 12) {
			modifier = 12;
		}
		time = (hour + modifier) * 60 + min;
		
		return time;
	}
	
	public static String convertIntToTime(int time) {
		StringBuilder timeStr = new StringBuilder();
		int hour = time / 60;
		int min = time % 60;
		if (hour > 12)
			timeStr.append(hour-12);
		else if (hour == 0)
			timeStr.append("12");
		else
			timeStr.append(hour);
		timeStr.append(":");
		if (min < 10)
			timeStr.append("0");
		timeStr.append(min);
		if (hour > 12)
			timeStr.append(" p.m.");
		else
			timeStr.append(" a.m.");
		return timeStr.toString();
	}
}
