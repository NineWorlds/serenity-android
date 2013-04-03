package us.nineworlds.serenity.core.subtitles;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import us.nineworlds.serenity.core.subtitles.formats.FormatASS;
import us.nineworlds.serenity.core.subtitles.formats.FormatSCC;
import us.nineworlds.serenity.core.subtitles.formats.FormatSRT;
import us.nineworlds.serenity.core.subtitles.formats.FormatSTL;
import us.nineworlds.serenity.core.subtitles.formats.FormatTTML;
import us.nineworlds.serenity.core.subtitles.formats.TimedTextFileFormat;
import us.nineworlds.serenity.core.subtitles.formats.TimedTextObject;

public class Pruebas {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TimedTextObject tto;
		
		try {

			TimedTextFileFormat ttff;
			
			//To test the correct implementation of the SRT parser and writer.
			ttff = new FormatSRT();
			File file = new File("standards\\SRT\\Avengers.2012.Eng.Subs.srt");
			InputStream is = new FileInputStream(file);
			tto = ttff.parseFile(file.getName(), is);
			IOClass.escribirFicheroTxt("prueba.txt", tto.toSRT());
			
			//To test the correct implementation of the ASS/SSA parser and writer.
			ttff = new FormatASS();
			file = new File("standards\\ASS\\test.ssa");
			is = new FileInputStream(file);
			tto = ttff.parseFile(file.getName(), is);
			IOClass.escribirFicheroTxt("prueba.txt", tto.toASS());

			//To test the correct implementation of the TTML parser and writer.
			ttff = new FormatTTML();
			file = new File("standards\\XML\\Debate0_03-03-08.dfxp.xml");
			is = new FileInputStream(file);
			tto = ttff.parseFile(file.getName(), is);
			IOClass.escribirFicheroTxt("prueba.txt", tto.toTTML());

			//To test the correct implementation of the SCC parser and writer.
			ttff = new FormatSCC();
			file = new File("standards\\SCC\\sccTest.scc");
			is = new FileInputStream(file);
			tto = ttff.parseFile(file.getName(), is);
			IOClass.escribirFicheroTxt("prueba.txt", tto.toSCC());
			
			//To test the correct implementation of the STL parser and writer.
			ttff = new FormatSTL();
			file = new File("standards\\STL\\Alsalirdeclasebien.stl");
			is = new FileInputStream(file);
			tto = ttff.parseFile(file.getName(), is);
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream("prueba.txt"));
				output.write(tto.toSTL());
			} finally {
				output.close();
			}

	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
