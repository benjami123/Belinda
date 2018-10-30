/*
 * 30.08.2016 IParse gone, IScanner gone, minor editing
 * 21.09.2012 Default directory changed
 * 24.10.2009 IScanner and IParser
 * 07.10.2009 New package structure
 * 28.09.2006 Original version
 */
  



import AST_P.AST;
import TO_PA_SC.Parser;
import TO_PA_SC.Scanner;
import TO_PA_SC.SourceFile;

import javax.swing.*;


public class TestDriverParser
{
	private static final String EXAMPLES_DIR = "exemples/prog1.txt";
	
	
	public static void main( String args[] )
	{
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );
		AST programTree = null;
		if( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			SourceFile in = new SourceFile( fc.getSelectedFile().getAbsolutePath() );
			Scanner s = new Scanner( in );
			Parser p = new Parser( s );
		
			programTree = p.parseProgram();
		}
		System.out.println(programTree);
		System.out.println("fin");
	}
}