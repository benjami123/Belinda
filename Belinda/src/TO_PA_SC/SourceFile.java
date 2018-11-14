package TO_PA_SC;/*
 * 16.08.2016 Minor editing
 * 25.09.2009 New package structure
 * 22.09.2006 Original version (adapted from Watt&Brown)
 */
 



import java.io.*;


public class SourceFile
{
	static final char EOL = '\n';
	static final char EOT = 0;
	
	
	private FileInputStream source;
	
	
	public SourceFile( String sourceFileName )
	{
		try {
			source = new FileInputStream( sourceFileName );
			System.out.println("success");
		} catch( FileNotFoundException ex ) {
			System.out.println( "*** FILE NOT FOUND *** (" + sourceFileName + ")" );
			System.exit( 1 );
		}
	}
	
	
	char getSource()
	{
		try {
			int c = source.read();
			if( c < 0 )
				return EOT;
			else
				return (char) c;
		} catch( IOException ex ) {
			return EOT;
		}
	}
}