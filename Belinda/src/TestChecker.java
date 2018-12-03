/*
 * 30.08.2016 IParse gone, IScanner gone, minor editing
 * 21.09.2012 Default directory changed
 * 24.10.2009 IScanner and IParser
 * 07.10.2009 New package structure
 * 28.09.2006 Original version
 */
  



import AST_P.AST;
import AST_P.Program;
import TO_PA_SC.*;

import javax.swing.*;


public class TestChecker
{
	private static final String EXAMPLES_DIR = "exemples/01_Prog1.bl";
	
	
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
		Checker c = new Checker();
		c.check((Program) programTree);
		Encoder e = new Encoder();
		e.encode((Program) programTree);


	}
}