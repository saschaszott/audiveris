//-----------------------------------------------------------------------//
//                                                                       //
//                   L o g B a s i c F o r m a t t e r                   //
//                                                                       //
//  Copyright (C) Herve Bitteur 2000-2006. All rights reserved.          //
//  This software is released under the terms of the GNU General Public  //
//  License. Please contact the author at herve.bitteur@laposte.net      //
//  to report bugs & suggestions.                                        //
//-----------------------------------------------------------------------//

package omr.util;

import omr.constant.Constant;
import omr.constant.ConstantSet;

import java.io.*;
import java.text.*;
import java.util.Date;
import java.util.logging.*;

/**
 * Class <code>LogBasicFormatter</code> formats a log record.
 *
 * @author Herv&eacute; Bitteur
 * @version $Id$
 */
public class LogBasicFormatter
    extends Formatter
{
    //~ Static variables/initializers -------------------------------------

    private static final Constants constants = new Constants();

    // Line separator string.  This is the value of the line.separator
    private static String lineSeparator = "\n";

    //~ Instance variables ------------------------------------------------

    private Date dat = new Date();
    private final static String format = "{0,time}";
    private MessageFormat formatter;

    private Object args[] = new Object[1];

    //~ Methods -----------------------------------------------------------

    /**
     * Format the given LogRecord.
     *
     * @param record the log record to be formatted.
     *
     * @return a formatted log record
     */
    public synchronized String format (LogRecord record)
    {
        StringBuffer sb = new StringBuffer(256);

        // First line (if any)

        if (constants.printTime.getValue()) {
            dat.setTime(record.getMillis());
            args[0] = dat;
            StringBuffer text = new StringBuffer();
            if (formatter == null) {
                formatter = new MessageFormat(format);
            }
            formatter.format(args, text, null);
            sb.append(text);
        }

        if (constants.printClass.getValue()) {
            sb.append(" ");
            if (record.getSourceClassName() != null) {
                sb.append(record.getSourceClassName());
            } else {
                sb.append(record.getLoggerName());
            }
        }

        if (constants.printMethod.getValue()) {
            if (record.getSourceMethodName() != null) {
                sb.append(" ");
                sb.append(record.getSourceMethodName());
            }
        }

        if (sb.length() > 0) {
            sb.append(lineSeparator);
        }

        // Second line

        String message = formatMessage(record);

        //sb.append(record.getLevel().getLocalizedName());
        sb.append(record.getLevel().getName());
        sb.append(": ");
        sb.append(message);
        sb.append(lineSeparator);

        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter ();
            PrintWriter pw = new PrintWriter (sw);
            record.getThrown ().printStackTrace (pw);
            pw.close ();
            sb.append (sw.toString ());
        }

        return sb.toString();
    }

    //~ Classes -----------------------------------------------------------

    //-----------//
    // Constants //
    //-----------//
    private static class Constants
        extends ConstantSet
    {
        Constant.Boolean printTime = new Constant.Boolean
                (false,
                 "Should we print time in log");

        Constant.Boolean printClass = new Constant.Boolean
                (false,
                 "Should we print Class name in log");

        Constant.Boolean printMethod = new Constant.Boolean
                (false,
                 "Should we print Method in log");

        Constants ()
        {
            initialize();
        }
    }
}
