package com.supertomato.restaurant.common.util;

import com.supertomato.restaurant.common.exception.ApplicationException;
import com.supertomato.restaurant.controller.util.APIStatus;
import org.joda.time.DateTimeZone;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author DiGiEx
 */
public class DateUtil {
    public static final String _DATE_MAX = "9999-12-31 23:59:59";
    public static final Date MAX_DATE = new Date(9999, 1, 1);
    public static final String _DATE_MIN = "1000-01-02 00:00:00";
    private static TimeZone _tz = TimeZone.getTimeZone("UTC");

    /**
     * convert from MMM/dd/YYYY
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date getDatefromString(String dateStr) throws ParseException {
        String actualDate = "";
        String[] dates = dateStr.split("/");
        System.out.println("date leng" + dates.length);
        if (dates.length >= 3) {
            actualDate = dates[1] + "-" + dates[0] + "-" + dates[2];
            DateFormat mediumFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            return mediumFormat.parse(actualDate);
        }
        return MAX_DATE;
    }

    /**
     * This function is use to convert string date format to data format
     *
     * @param dateStr
     * @return
     */
    public static Date toDate(String dateStr) {
        return toDate(dateStr, null);
    }

    /**
     * Convert string date format with time zone
     *
     * @param dateStr
     * @param tz
     * @return
     */
    public static Date toDate(String dateStr, TimeZone tz) {
        if (tz == null) {
            tz = TimeZone.getDefault();
        }
        if (dateStr == null) {
            return null;
        }

        SimpleDateFormat fmt = null;
        if (dateStr.length() == 19) {
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fmt.setTimeZone(tz);
        } else {
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        }
        try {
            return fmt.parse(dateStr);
        } catch (ParseException e) {

            return null;
        }
    }

    /**
     * Convert date to XML format
     *
     * @param date
     * @return
     */
    public static String formatXml(Date date) {
        return formatXml(date, null);
    }

    /**
     * Convert date with time zone to XML format
     *
     * @param date
     * @param tz
     * @return
     */
    public static String formatXml(Date date, TimeZone tz) {
        if (tz == null) {
            tz = TimeZone.getDefault();
        }
        if (date.after(getMax())) {
            date = getMax();
        }
        SimpleDateFormat outputXmlFmt
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputXmlFmt.setTimeZone(tz);
        String result
                = outputXmlFmt.format(date) + " " + getGMTString(date, tz);
        return result;
    }

    public static String toStringWithoutGMT(Date date) {
        return toStringWithoutGMT(date, null);
    }

    public static String toStringWithoutGMT(Date date, TimeZone tz) {
        if (tz == null) {
            tz = TimeZone.getDefault();
        }
        if (date.after(getMax())) {
            date = getMax();
        }
        SimpleDateFormat outputXmlFmt
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputXmlFmt.setTimeZone(tz);
        return outputXmlFmt.format(date);
    }

    public static String toDbString(Date date) {
        return toDbString(date, (TimeZone) null);
    }

    public static String toDbString(Date date, String timeZone) {
        return toDbString(date, TimeZone.getTimeZone(timeZone));
    }

    public static String toDbString(Date date, TimeZone timezone) {
        if (date == null) {
            return null;
        }
        if (timezone == null) {
            timezone = TimeZone.getDefault();
        }
        SimpleDateFormat dbFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dbFmt.setTimeZone(timezone);
        return dbFmt.format(date);
    }

    public static String toDateString(Date date, TimeZone timezone) {
        if (date == null) {
            return null;
        }
        if (timezone == null) {
            timezone = TimeZone.getDefault();
        }
        SimpleDateFormat dbFmt = new SimpleDateFormat(Constant.API_FORMAT_DATE_TIME);
        dbFmt.setTimeZone(timezone);
        return dbFmt.format(date);
    }

    public static Date getMax() {
        return toDate(_DATE_MAX);
    }

    public static Date getMin() {
        return toDate(_DATE_MIN);
    }

    public static String toYYYYMM(Date date) {
        return toYYYYMM(date, null);
    }

    public static String toYYYYMM(Date date, TimeZone timezone) {
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }
        if (timezone == null) {
            timezone = TimeZone.getDefault();
        }
        SimpleDateFormat dbFmt = new SimpleDateFormat("yyyyMM");
        dbFmt.setTimeZone(timezone);
        return dbFmt.format(date);
    }

    private static final SimpleDateFormat _fmtYear = new SimpleDateFormat("yyyy");

    public static int getYear(Date date, TimeZone timeZone) {
        if (date == null) {
            throw new IllegalArgumentException("date is required.");
        }
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        _fmtYear.setTimeZone(timeZone);
        try {
            return Integer.parseInt(_fmtYear.format(date));
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Failed to format date : " + e.getMessage());
        }
    }

    private static final SimpleDateFormat _fmtMonth = new SimpleDateFormat("MM");

    public static int getMonth(Date date, TimeZone timeZone) {
        if (date == null) {
            throw new IllegalArgumentException("date is required.");
        }
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        _fmtMonth.setTimeZone(timeZone);
        try {
            return Integer.parseInt(_fmtMonth.format(date));
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Failed to format date : " + e.getMessage());
        }
    }

    /**
     * Get UTC time now
     *
     * @return
     */
    public static Date getUTCNow() {
        int offset = TimeZone.getDefault().getOffset(new Date().getTime());
        return new Date(new Date().getTime() - offset);
    }

    /**
     * Convert date to specified time zone
     *
     * @param date
     * @param tz
     * @return
     */
    public static Date getConvertDate(Date date, TimeZone tz) {
        if (date == null) {
            return null;
        }
        int offset = tz.getRawOffset();
        if (tz.inDaylightTime(date)) {
            offset += 3600000;
        }
        return new Date(date.getTime() + offset);
    }

    public static Date convertLocalToUTC(Date date, ZoneId timeZoneLocal) {
        OffsetDateTime offsetDateTime = ZonedDateTime
                .ofInstant(date.toInstant(), ZoneOffset.UTC) // set Date to ZonedDateTime
                .withZoneSameLocal(timeZoneLocal) // set zone local
                .toOffsetDateTime() // parse offset
                .withOffsetSameInstant(ZoneOffset.UTC); // set to UTC

        return Date.from(offsetDateTime.toInstant());
    }

    public static Date getUTCDate(Date date, TimeZone tz) {
        if (date == null) {
            return null;
        }
        int offset = tz.getRawOffset();
        if (tz.inDaylightTime(date)) {
            offset += 3600000;
        }
        return new Date(date.getTime() - offset);
    }

    public static String toString(Date date) {
        return getConvertDateString(date, _tz, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getConvertDateString(Date date, TimeZone tz, String fmt) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        Date newDete = getConvertDate(date, tz);
        return format.format(newDete);
    }

    public static Date addDate(Date date, TimeZone tz, int add) {
        Calendar cal = Calendar.getInstance(tz);
        cal.setTime(date);
        cal.add(Calendar.DATE, add);
        Date toDate = cal.getTime();
        return toDate;
    }

    ////////////////////////////////////////////////////////////
    // Private methods.
    private static String getGMTString(Date date, TimeZone tz) {
        DecimalFormat fmt = new DecimalFormat("'GMT'+00':00';'GMT'-00':00'");
        int off = tz.getRawOffset();
        if (tz.inDaylightTime(date)) {
            off += 3600000;
        }
        off = off / 3600000;
        return fmt.format(off);
    }

    /**
     * Generate an rfc822 date for use in the Date HTTP header.
     */
    public static String gethttpDate() {
        final String DateFormat = "EEE, dd MMM yyyy HH:mm:ss ";
        SimpleDateFormat format = new SimpleDateFormat(DateFormat, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(new Date()) + "GMT";
    }

    //We use Joda API time to convert

    /**
     * Convert date to UTC standard based on local TimeZone
     *
     * @param date to be converted
     * @return converted date
     */
    public static Date convertToUTC(Date date) {
        DateTimeZone d = DateTimeZone.getDefault();
        return new Date(d.convertLocalToUTC(date.getTime(), false));
    }

    /**
     * Convert date to UTC standard based on specified TimeZone offset
     *
     * @param date to be converted
     * @return converted date
     */
    public static Date convertLocalToUTC(Date date, int userTimeZone) {
        DateTimeZone d = DateTimeZone.forOffsetHours(userTimeZone);
        //DateTimeZone d = DateTimeZone.getDefault();
        return new Date(d.convertLocalToUTC(date.getTime(), false));
    }

    /**
     * Convert UTC time to local time based on specified TimeZone offset
     *
     * @param date to be converted
     * @return converted date
     */
    public static Date convertToLocalTime(Date date, int hour) {
        DateTimeZone d = DateTimeZone.forOffsetHours(hour);
        return new Date(d.convertUTCToLocal(date.getTime()));
    }

    public static String convertToLocalTimeString(Date date, int hour) {
        String dateString = "";
        Date _date = convertToLocalTime(date, hour);
        String dateFormat = "yyyy/MM/dd hh:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
        dateString = format.format(_date);
        return dateString;
    }


    public static String getDefaultExpiryDocumentDate() {
        //Date newExpiryDate = new Date(2000, 1, 1);
        //DateFormat fortmatDate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        //return fortmatDate.format(newExpiryDate);
        return "2012-01-01 23:59:59";
    }

    /**
     * Checking the date of the end of Month
     *
     * @return true/false
     */
    public static boolean isLastDayOfMonth() {
        try {

            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            Date lastDayOfMonth = calendar.getTime();

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (sdf.format(today).equals(sdf.format(lastDayOfMonth))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Checking isLastDayOfMonth error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checking the date of the end of Month
     *
     * @return true/false
     */
    public static Date getLastDayOfNextMonth() {
        Date lastDateOfNextMonth = null;
        try {

            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.MONTH, 2);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            lastDateOfNextMonth = calendar.getTime();

        } catch (Exception e) {
            System.err.println("getLastDayOfNextMonth error: " + e.getMessage());
        }

        return lastDateOfNextMonth;
    }

    public static Date getStartDateTime(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndDateTime(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static String convertToLocalTimeString(Date date, int hour, String formatStr) {
        String dateString = "";
        Date _date = convertToLocalTime(date, hour);
        String dateFormat = (formatStr != null && !"".equals(formatStr)) ? formatStr : "yyyy/MM/dd hh:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
        dateString = format.format(_date);
        return dateString;
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date stringToDate(String dateStr, SimpleDateFormat formatter) {
        Date date;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "Date Format must be " + formatter.toPattern());
        }
        return date;
    }

    public static Date addMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        // Add 3 months to current date
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }


    public static Date convertStringToDate(String dateStr, SimpleDateFormat formatter) {
        Date date;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            throw new ApplicationException(APIStatus.BAD_REQUEST, "Date Format must be " + formatter.toPattern());
        }

        return date;
    }

    public static Date setDate(Date date, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static ZoneId getTimezone(String timezone){
        ZoneId zoneId;
        try {
             zoneId = ZoneId.of(timezone);
        }catch (Exception e){
            throw new ApplicationException(APIStatus.ERR_TIME_ZONE_INVALID);
        }
        return zoneId;
    }
}
