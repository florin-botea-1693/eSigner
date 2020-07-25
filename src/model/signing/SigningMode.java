package model.signing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface SigningMode {
	public void sign(File file) throws FileNotFoundException, IOException;
}
