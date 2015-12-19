/*
The MIT License (MIT)

Copyright (c) 2015 Renjith Mathew

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.github.e2point718.har2jmx;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class HAR2JMX {
	
	public static void main(String[] args) {
		try {
			parseCommandLine(args);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static final String CMD_SWITCH_jmeterHome="jmeterHome";
	private static CommandLine parseCommandLine(String[] args) throws ParseException{
		Options options = new Options();
		Option jMeterHome = Option.builder(CMD_SWITCH_jmeterHome).hasArg().required().desc("JMeter Home folder").build();
		options.addOption(jMeterHome);
		CommandLineParser parser = new DefaultParser();
		CommandLine line;
		try {
			line = parser.parse(options, args);
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "har2jmx", options );
			throw e;
		}
		File jMeterHomeFolder = new File(line.getOptionValue(CMD_SWITCH_jmeterHome));
		if(!jMeterHomeFolder.exists()){
			throw new ParseException(jMeterHomeFolder.getAbsolutePath()+" is not accessible");
		}
		File jMeterSaveServiceFile = new File(jMeterHomeFolder,"bin/saveservice.properties");
		if(!jMeterSaveServiceFile.exists()){
			throw new ParseException(jMeterSaveServiceFile.getAbsolutePath()+" is not accessible. Please verify jmeterHome location "+jMeterHomeFolder.getAbsolutePath());
		}		
		return line;
	}

}
