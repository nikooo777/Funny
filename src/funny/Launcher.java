package funny;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Launcher
{

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		// takes the path of a funny program, it compiles it, gets the program root nodesequence and evaluates it
		new Compiler(new FileReader("program.funny")).program().eval();
	}

}