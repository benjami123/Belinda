import TO_PA_SC.*;
import AST_P.*;


import javax.swing.*;


public class Compiler
{
    private static final String EXAMPLES_DIR = "exemples/01_Prog1.bl";


    public static void main( String args[] )
    {
        JFileChooser fc = new JFileChooser( EXAMPLES_DIR );

        if( fc.showOpenDialog( null ) == fc.APPROVE_OPTION ) {
            String sourceName = fc.getSelectedFile().getAbsolutePath();

            SourceFile in = new SourceFile( sourceName );
            Scanner s = new Scanner( in );
            Parser p = new Parser(s);
            Checker c = new Checker();
            Encoder e = new Encoder();

            Program program = (Program) p.parseProgram();
            c.check( program );
            e.encode( program );

            String targetName;
            if( sourceName.endsWith( ".txt" ) )
                targetName = sourceName.substring( 0, sourceName.length() - 4 ) + ".tam";
            else
                targetName = sourceName + ".tam";

            e.saveTargetProgram( targetName );
        }
    }
}